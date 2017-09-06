package uk.co.claritysoftware.alexa.skills.pontoon;

import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert;

/**
 * Integration test to assert flow and state control
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
public class StateControlIT {

	@Autowired
	private PontoonSpeechlet pontoonSpeechlet;

	@Autowired
	private SessionSupport sessionSupport;

	private Session session;

	@Before
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

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^Welcome to Pontoon");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
		RepromptAssert.assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeech(expectedReprompt);
	}

	@Test
	public void shouldGetTwistResponseGivenTwistIntentAndGameStarted() {
		// Given
		startGame();
		ensureHandCannotWinAfterTwist(); // This enssure we always get a hand in play response, rather than a win or bust

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("TwistIntent")
								.build())
						.build())
				.build();

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^Your hand is now the");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
		RepromptAssert.assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeech(expectedReprompt);
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

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^Welcome to Pontoon");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
		RepromptAssert.assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeech(expectedReprompt);
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

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^Your final score is");

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
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

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^Pontoon is a card game");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
		RepromptAssert.assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeech(expectedReprompt);
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
