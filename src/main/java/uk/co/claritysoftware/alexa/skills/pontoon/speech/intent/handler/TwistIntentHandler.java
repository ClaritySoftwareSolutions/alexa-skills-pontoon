package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Twist intent
 */
@Component
public class TwistIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TwistIntentHandler.class);

	private static final PontoonIntent HANDLED_INTENT = PontoonIntent.TWIST_INTENT;

	private final PontoonGameActions pontoonGameActions;

	@Autowired
	public TwistIntentHandler(final PontoonGameActions pontoonGameActions) {
		this.pontoonGameActions = pontoonGameActions;
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
		return pontoonGameActions.twist(session);
	}
}
