package uk.co.claritysoftware.alexa.skills.speech;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;

/**
 * Abstract class implementing {link SpeechletV2} providing no-op implementations of methods
 */
public abstract class AbstractSpeechlet implements SpeechletV2 {

	public abstract SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope);

	public abstract SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {

	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

	}
}
