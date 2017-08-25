package uk.co.claritysoftware.alexa.skills.speech.intent;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Intent Handler interface
 */
public interface IntentHandler {

	/**
	 * Handles the intent
	 *
	 * @param requestEnvelope the {@link SpeechletRequestEnvelope} encapsulating the {@link IntentRequest} and {@link Session}
	 * @return a {@link SpeechletResponse} that is the result of handling the intent
	 */
	SpeechletResponse handleIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);

	/**
	 * Determines if this instance of {@link IntentHandler} can be used to handle the specified {@link AlexaIntent}
	 *
	 * @param alexaIntent the {@link AlexaIntent} that should be handled
	 * @return true if the {@link IntentHandler} handles the specified {@link AlexaIntent}
	 */
	boolean handles(AlexaIntent alexaIntent);
}
