package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.GameNotStartedException;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Abstract {@link IntentHandler} for intents that rely on the game having been started.
 * Any intent handler that needs to ensure the game has started should extend this base class.
 */
public abstract class MidGameIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(MidGameIntentHandler.class);

	protected final PontoonGameActions pontoonGameActions;

	protected MidGameIntentHandler() {
		pontoonGameActions = null;
	}

	public MidGameIntentHandler(final PontoonGameActions pontoonGameActions) {
		this.pontoonGameActions = pontoonGameActions;
	}

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();

		if (!isGameAlreadyStarted(session)) {
			throw new GameNotStartedException();
		}

		return doIntent(requestEnvelope);
	}

	/**
	 * Performs the intent
	 *
	 * @param requestEnvelope the {@link SpeechletRequestEnvelope} encapsulating the {@link IntentRequest} and {@link Session}
	 * @return a {@link SpeechletResponse} that is the result of handling the intent
	 */
	abstract SpeechletResponse doIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope);

	private boolean isGameAlreadyStarted(final Session session) {
		return pontoonGameActions.isGameAlreadyStarted(session);
	}
}
