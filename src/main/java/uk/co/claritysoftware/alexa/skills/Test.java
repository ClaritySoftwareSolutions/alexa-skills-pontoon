package uk.co.claritysoftware.alexa.skills;

import java.util.UUID;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.Application;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet;

/**
 * @author Nathan Russell
 */
public class Test {

	public static void main(String[] args) {

		PontoonSpeechlet pontoonSpeechlet = Application.getBean(PontoonSpeechlet.class);

		// Start Session
		SpeechletRequestEnvelope requestEnvelope = sessionStartedSpeechletRequestEnvelope();
		pontoonSpeechlet.onSessionStarted(requestEnvelope);
		Session session = requestEnvelope.getSession();

		// Launch
		requestEnvelope = launchSpeechletRequestEnvelopeWithSession(session);
		SpeechletResponse speechletResponse = pontoonSpeechlet.onLaunch(requestEnvelope);

		System.out.println("OutputSpeech = " + ((PlainTextOutputSpeech)speechletResponse.getOutputSpeech()).getText());

	}

	private static SpeechletRequestEnvelope<SessionStartedRequest> sessionStartedSpeechletRequestEnvelope() {
		return SpeechletRequestEnvelope.<SessionStartedRequest>builder()
				.withVersion("1.0")
				.withContext(Context.builder().build())
				.withSession(Session.builder().withSessionId(UUID.randomUUID().toString()).build())
				.withRequest(SessionStartedRequest.builder()
						.withRequestId(UUID.randomUUID().toString())
						.build())
				.build();
	}

	private static SpeechletRequestEnvelope<LaunchRequest> launchSpeechletRequestEnvelopeWithSession(final Session session) {
		return SpeechletRequestEnvelope.<LaunchRequest>builder()
				.withVersion("1.0")
				.withContext(Context.builder().build())
				.withSession(session)
				.withRequest(LaunchRequest.builder()
						.withRequestId(UUID.randomUUID().toString())
						.build())
				.build();
	}
}
