package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Twist intent
 */
public class TwistIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TwistIntentHandler.class);

	private final PontoonGameActions pontoonGameActions;

	public TwistIntentHandler(final PontoonGameActions pontoonGameActions) {
		this.pontoonGameActions = pontoonGameActions;
	}

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		return pontoonGameActions.twist(session);
	}
}
