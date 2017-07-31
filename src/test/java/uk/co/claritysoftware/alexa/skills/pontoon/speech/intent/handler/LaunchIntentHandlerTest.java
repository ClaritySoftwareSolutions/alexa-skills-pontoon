package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue;

/**
 * Unit test class for {@link LaunchIntentHandler}
 */
public class LaunchIntentHandlerTest {

	private LaunchIntentHandler intentHandler = new LaunchIntentHandler();

	@Test
	public void shouldHandleIntent() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.FIVE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.QUEEN, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();
		requestEnvelope.getSession().setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Five of CLUBS, and the Queen of HEARTS. Your score is 15. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = intentHandler.handleIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

	@Test
	public void shouldHandleIntentGivenALowAce() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.FIVE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.ACE_LOW, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();
		requestEnvelope.getSession().setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Five of CLUBS, and the Ace of HEARTS. Ace is low. Your score is 6. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = intentHandler.handleIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

	@Test
	public void shouldHandleIntentGivenTwoAces() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.ACE_LOW, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.ACE_LOW, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();
		requestEnvelope.getSession().setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Ace of HEARTS. Ace is low. Your score is 2. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = intentHandler.handleIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

}
