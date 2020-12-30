package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.SpeechletResponseAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;

import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.dagger.DaggerPontoonComponent;
import uk.co.claritysoftware.alexa.skills.pontoon.dagger.PontoonComponent;

/**
 * Integration test class for {@link PontoonGameActions}
 */
public class PontoonGameActionsIT {

	private final PontoonComponent pontoonComponent = DaggerPontoonComponent.create();

	private final PontoonGameActions pontoonGameActions = pontoonComponent.buildPontoonGameActions();

	private final PontoonSpeechlet pontoonSpeechlet = pontoonComponent.buildPontoonSpeechlet();

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
