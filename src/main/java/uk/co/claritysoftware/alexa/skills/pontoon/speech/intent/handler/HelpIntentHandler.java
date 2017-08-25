package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static uk.co.claritysoftware.alexa.skills.speech.factory.RepromptFactory.whatNextReprompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Help intent
 */
@Component
public class HelpIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(HelpIntentHandler.class);

	private static final PontoonIntent HANDLED_INTENT = PontoonIntent.HELP_INTENT;

	@Override
	public boolean handles(final AlexaIntent alexaIntent) {
		return HANDLED_INTENT == alexaIntent;
	}

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		final String speechText = "You can play Pontoon with me. What would you like me to do?";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return newAskResponse(speech, whatNextReprompt());
	}
}
