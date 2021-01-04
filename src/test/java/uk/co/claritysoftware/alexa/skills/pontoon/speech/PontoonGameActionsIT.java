package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.SpeechletResponseAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;

import java.util.regex.Pattern;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Integration test class for {@link PontoonGameActions}
 */
@QuarkusTest
public class PontoonGameActionsIT {

	@Inject
	private PontoonGameActions pontoonGameActions;

	@Inject
	private PontoonSpeechlet pontoonSpeechlet;

	private Session session;

	@BeforeEach
	public void sessionSetup() {
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope();
		pontoonSpeechlet.onSessionStarted(requestEnvelope);
		session = requestEnvelope.getSession();
	}

	@Test
	public void shouldHelpGivenGameNotYetStarted() {
		// Given
		Pattern expectedPlainTextOutputSpeech = Pattern.compile("Pontoon is a card game.*\nTo start the game");

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.help(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedPlainTextOutputSpeech);
	}

	@Test
	public void shouldHelpGivenGameStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("StartGameIntent")
								.build())
						.build())
				.build();
		pontoonSpeechlet.onIntent(requestEnvelope);
		Pattern expectedPlainTextOutputSpeech = Pattern.compile("Pontoon is a card game.*\nYour game is already started");

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.help(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedPlainTextOutputSpeech);
	}
}
