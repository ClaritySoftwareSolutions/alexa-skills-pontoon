package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ApplicationScoped
public class TwistIntentHandler extends MidGameIntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TwistIntentHandler.class);

	private static final PontoonIntent HANDLED_INTENT = PontoonIntent.TWIST_INTENT;

	@Inject
	public TwistIntentHandler(final PontoonGameActions pontoonGameActions) {
		super(pontoonGameActions);
	}

	@Override
	public boolean handles(final AlexaIntent alexaIntent) {
		return HANDLED_INTENT == alexaIntent;
	}

	@Override
	SpeechletResponse doIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("doIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		return pontoonGameActions.twist(session);
	}
}
