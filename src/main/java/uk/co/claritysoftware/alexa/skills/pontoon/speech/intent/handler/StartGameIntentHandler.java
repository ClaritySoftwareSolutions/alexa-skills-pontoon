package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Start Game intent
 */
public class StartGameIntentHandler implements IntentHandler {

	private static final String ACE_VALUE_SLOTNAME = "aceValue";

	private static final Logger LOG = LoggerFactory.getLogger(StartGameIntentHandler.class);

	private final PontoonGameActions pontoonGameActions;

	private final SessionSupport sessionSupport;

	public StartGameIntentHandler(final PontoonGameActions pontoonGameActions, final SessionSupport sessionSupport) {
		this.pontoonGameActions = pontoonGameActions;
		this.sessionSupport = sessionSupport;
	}

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		boolean aceIsHigh = "high".equals(getSlotValueFromRequest(requestEnvelope.getRequest(), ACE_VALUE_SLOTNAME));
		sessionSupport.setAceIsHighOnSession(session, aceIsHigh);

		return pontoonGameActions.dealInitialHand(session);
	}

	private String getSlotValueFromRequest(IntentRequest request, String slotName) {
		Slot slot = request.getIntent().getSlot(slotName);
		return slot != null ? slot.getValue() : null;
	}

}
