package uk.co.claritysoftware.alexa.skills.pontoon;

import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.SpeechletResponseAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;

import java.util.Arrays;
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
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardValue;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Integration test to assert flow and state control
 */
@QuarkusTest
public class StateControlIT {

	@Inject
	private PontoonSpeechlet pontoonSpeechlet;

	@Inject
	private SessionSupport sessionSupport;

	private Session session;

	@BeforeEach
	public void sessionSetup() {
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope();
		pontoonSpeechlet.onSessionStarted(requestEnvelope);
		session = requestEnvelope.getSession();
	}

	@Test
	public void shouldGetHelpResponseGivenTwistIntentAndGameNotStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("TwistIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Welcome to Pontoon");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeechWithText(expectedReprompt);
	}

	@Test
	public void shouldGetTwistResponseGivenTwistIntentAndGameStarted() {
		// Given
		startGame();
		ensureHandCannotWinAfterTwist(); // This ensurea we always get a hand in play response, rather than a win or bust

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("TwistIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Your hand is now the");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeechWithText(expectedReprompt);
	}

	@Test
	public void shouldGetHelpResponseGivenStickIntentAndGameNotStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("StickIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Welcome to Pontoon");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeechWithText(expectedReprompt);
	}

	@Test
	public void shouldGetStickResponseGivenStickIntentAndGameStarted() {
		// Given
		startGame();

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("StickIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Your final score is");

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
	}

	@Test
	public void shouldGetHelpResponseGivenStartGameIntentAndGameAlreadyStarted() {
		// Given
		startGame();

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("StartGameIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Pontoon is a card game");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeechWithText(expectedReprompt);
	}

	@Test
	public void shouldGetStickResponseGivenStopIntentAndGameStarted() {
		// Given
		startGame();

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("AMAZON.StopIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^Your final score is");

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
	}

	@Test
	public void shouldGetStopResponseGivenStopIntentAndGameNotStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("AMAZON.StopIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern.compile("^OK, goodbye");

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
	}

	private void startGame() {
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
	}

	private void ensureHandCannotWinAfterTwist() {
		Hand deliberatelyLowHand = new Hand(Arrays.asList(
				new Card(CardValue.TWO, CardSuit.CLUBS),
				new Card(CardValue.THREE, CardSuit.CLUBS)
		));
		sessionSupport.setHandOnSession(session, deliberatelyLowHand);
	}

}
