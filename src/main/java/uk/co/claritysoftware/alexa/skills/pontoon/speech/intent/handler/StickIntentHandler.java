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
 * {@link IntentHandler} for the Stick intent
 */
@Component
public class StickIntentHandler extends MidGameIntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(StickIntentHandler.class);

	private static final PontoonIntent HANDLED_INTENT = PontoonIntent.STICK_INTENT;

	@Override
	public boolean handles(final AlexaIntent alexaIntent) {
		return HANDLED_INTENT == alexaIntent;
	}

	@Autowired
	public StickIntentHandler(final PontoonGameActions pontoonGameActions) {
		super(pontoonGameActions);
	}

	@Override
	SpeechletResponse doIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("doIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		return pontoonGameActions.stick(session);
	}
}
