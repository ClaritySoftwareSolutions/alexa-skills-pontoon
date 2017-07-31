package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue;

/**
 * Unit test class for {@link PontoonSpeechlet}
 */
public class PontoonSpeechletTest {

	private PontoonSpeechlet speechlet = new PontoonSpeechlet();

	@Test
	public void shouldOnSessionStarted() {
		// Given
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope();

		// When
		speechlet.onSessionStarted(requestEnvelope);

		// Then
		assertThat(requestEnvelope.getSession().getAttributes()).hasSize(1);
		assertThat(requestEnvelope.getSession().getAttribute("cardDeck")).isOfAnyClassIn(CardDeck.class);
	}

	@Test
	public void shouldOnLaunch() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.FIVE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.QUEEN, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();
		requestEnvelope.getSession().setAttribute("cardDeck", cardDeck);

		Pattern expectedPlainTextOutputSpeech = Pattern.compile("^I have dealt you the [A-Z][a-z]+ of [A-Z]+, and the [A-Z][a-z]* of [A-Z]+. Your score is \\d\\d?. What would you like to do\\?");

		// When
		SpeechletResponse speechletResponse = speechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
	}

	@Test
	public void shouldOnIntentGivenValidIntentName() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("AMAZON.HelpIntent");

		String expectedPlainTextOutputSpeech = "You can play Pontoon with me. What would you like me to do?";

		// When
		SpeechletResponse speechletResponse = speechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
	}

	@Test
	public void shouldFailToOnIntentGivenUnknownIntentName() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("some-unknown-intent");

		// When
		try {
			speechlet.onIntent(requestEnvelope);

			fail("Was expecting an IllegalArgumentException");
		}
		// Then
		catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("No intent with name some-unknown-intent");
		}
	}
}
