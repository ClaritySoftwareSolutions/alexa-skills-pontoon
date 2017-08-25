package uk.co.claritysoftware.alexa.skills.testsupport;

import java.util.Map;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;

/**
 * Test Data Factory for {@link SpeechletRequestEnvelope} instances
 */
public class SpeechletRequestEnvelopeTestDataFactory {

	public static SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope() {
		return SpeechletRequestEnvelope.<IntentRequest> builder()
				.withVersion("1.0")
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.build())
				.withSession(Session.builder()
						.withSessionId("67890")
						.build())
				.withContext(Context.builder()
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelopeWithIntentName(final String intentName) {
		return SpeechletRequestEnvelope.<IntentRequest> builder()
				.withVersion("1.0")
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName(intentName)
								.build())
						.build())
				.withSession(Session.builder()
						.withSessionId("67890")
						.build())
				.withContext(Context.builder()
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelopeWithIntentNameAndSlots(final String intentName, final Map<String, Slot> slots) {
		return SpeechletRequestEnvelope.<IntentRequest> builder()
				.withVersion("1.0")
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName(intentName)
								.withSlots(slots)
								.build())
						.build())
				.withSession(Session.builder()
						.withSessionId("67890")
						.build())
				.withContext(Context.builder()
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<SessionStartedRequest> sessionStartedSpeechletRequestEnvelope() {
		return SpeechletRequestEnvelope.<SessionStartedRequest> builder()
				.withVersion("1.0")
				.withRequest(SessionStartedRequest.builder()
						.withRequestId("12345")
						.build())
				.withSession(Session.builder()
						.withSessionId("67890")
						.build())
				.withContext(Context.builder()
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<LaunchRequest> launchSpeechletRequestEnvelope() {
		return SpeechletRequestEnvelope.<LaunchRequest> builder()
				.withVersion("1.0")
				.withRequest(LaunchRequest.builder()
						.withRequestId("12345")
						.build())
				.withSession(Session.builder()
						.withSessionId("67890")
						.build())
				.withContext(Context.builder()
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<LaunchRequest> launchSpeechletRequestEnvelopeWithSession(final Session session) {
		return SpeechletRequestEnvelope.<LaunchRequest> builder()
				.withVersion("1.0")
				.withRequest(LaunchRequest.builder()
						.withRequestId("12345")
						.build())
				.withSession(session)
				.withContext(Context.builder()
						.build())
				.build();
	}

}
