package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.GameAlreadyStartedException;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Start Game intent
 */
public class StartGameIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(StartGameIntentHandler.class);

	private static final PontoonIntent HANDLED_INTENT = PontoonIntent.START_GAME_INTENT;

	private static final String ACE_VALUE_SLOTNAME = "aceValue";

	private final PontoonGameActions pontoonGameActions;

	private final SessionSupport sessionSupport;

	@Inject
	public StartGameIntentHandler(final PontoonGameActions pontoonGameActions, final SessionSupport sessionSupport) {
		this.pontoonGameActions = pontoonGameActions;
		this.sessionSupport = sessionSupport;
	}

	@Override
	public boolean handles(final AlexaIntent alexaIntent) {
		return HANDLED_INTENT == alexaIntent;
	}

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();

		if (isGameAlreadyStarted(session)) {
			throw new GameAlreadyStartedException();
		}

		boolean aceIsHigh = "high".equals(getSlotValueFromRequest(requestEnvelope.getRequest(), ACE_VALUE_SLOTNAME));
		sessionSupport.setAceIsHighOnSession(session, aceIsHigh);

		return pontoonGameActions.dealInitialHand(session);
	}

	private String getSlotValueFromRequest(IntentRequest request, String slotName) {
		Slot slot = request.getIntent().getSlot(slotName);
		return slot != null ? slot.getValue() : null;
	}

	private boolean isGameAlreadyStarted(final Session session) {
		return pontoonGameActions.isGameAlreadyStarted(session);
	}

}
