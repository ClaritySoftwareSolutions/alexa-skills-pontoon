package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue;
import uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert;

/**
 * Unit test class for {@link PontoonGameActions}
 */
public class PontoonGameActionsTest {

	private static final Session SESSION = Session.builder()
			.withSessionId("1234")
			.build();

	private PontoonGameActions pontoonGameActions = PontoonGameActions.getInstance();

	@Test
	public void shouldDealInitialHandGivenHandWithNoAces() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.FIVE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.QUEEN, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));
		SESSION.setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Five of CLUBS, and the Queen of HEARTS. Your score is 15. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(SESSION, false);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		RepromptAssert.assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

	@Test
	public void shouldDealInitialHandGivenHandWithAceAndAceIsLow() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.ACE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.QUEEN, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));
		SESSION.setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Queen of HEARTS. Ace is low. Your score is 11. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(SESSION, false);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		RepromptAssert.assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

	@Test
	public void shouldHDealInitialHandGivenHandWithAceAndAceIsHigh() {
		// Given
		CardDeck cardDeck = new CardDeck();
		Card card1 = new Card(CardValue.ACE, CardSuit.CLUBS);
		Card card2 = new Card(CardValue.QUEEN, CardSuit.HEARTS);
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(card1, card2)));
		SESSION.setAttribute("cardDeck", cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Queen of HEARTS. Ace is high. Your score is 20. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(SESSION, true);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		RepromptAssert.assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}

}
