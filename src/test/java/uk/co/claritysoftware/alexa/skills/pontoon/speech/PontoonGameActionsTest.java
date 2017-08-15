package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.Arrays;
import org.junit.Test;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;

/**
 * Unit test class for {@link PontoonGameActions}
 */
public class PontoonGameActionsTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final Card FIVE_OF_CLUBS = new Card(CardValue.FIVE, CardSuit.CLUBS);

	private static final Card QUEEN_OF_HEARTS = new Card(CardValue.QUEEN, CardSuit.HEARTS);

	private static final Card ACE_OF_CLUBS = new Card(CardValue.ACE, CardSuit.CLUBS);

	private static final Card TWO_OF_SPADES = new Card(CardValue.TWO, CardSuit.SPADES);

	private PontoonGameActions pontoonGameActions = PontoonGameActions.getInstance();

	@Test
	public void shouldDealInitialHandGivenHandWithNoAces() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(false);

		String expectedPlainTextOutputSpeech = "I have dealt you the Five of CLUBS, and the Queen of HEARTS. Your score is 15. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
		verify(session).setAttribute("hand", handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS));
	}

	@Test
	public void shouldDealInitialHandGivenHandWithAceAndAceIsLow() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(ACE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(false);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Queen of HEARTS. Ace is low. Your score is 11. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
		verify(session).setAttribute("hand", handJson(ACE_OF_CLUBS, QUEEN_OF_HEARTS));
	}

	@Test
	public void shouldHDealInitialHandGivenHandWithAceAndAceIsHigh() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(ACE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(true);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Queen of HEARTS. Ace is high. Your score is 21. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
		verify(session).setAttribute("hand", handJson(ACE_OF_CLUBS, QUEEN_OF_HEARTS));
	}

	@Test
	public void shouldTwistGivenHandWithNoAces() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(TWO_OF_SPADES);
		String serializedHand = handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(false);
		given(session.getAttribute("hand")).willReturn(serializedHand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Queen of HEARTS, and the Two of SPADES. Your score is 17. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("hand", handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS, TWO_OF_SPADES));
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
	}

	@Test
	public void shouldTwistGivenHandWithAcesAndAceIsLow() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(ACE_OF_CLUBS);
		String serializedHand = handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(false);
		given(session.getAttribute("hand")).willReturn(serializedHand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Queen of HEARTS, and the Ace of CLUBS. Ace is low. Your score is 16. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("hand", handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS, ACE_OF_CLUBS));
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
	}

	@Test
	public void shouldTwistGivenHandWithAcesAndAceIsHigh() throws Exception {
		// Given
		Session session = session();

		String serializedCardDeck = cardDeckJson(ACE_OF_CLUBS);
		String serializedHand = handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);
		given(session.getAttribute("aceIsHigh")).willReturn(true);
		given(session.getAttribute("hand")).willReturn(serializedHand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Queen of HEARTS, and the Ace of CLUBS. Ace is high. Your score is 26. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(session).setAttribute("hand", handJson(FIVE_OF_CLUBS, QUEEN_OF_HEARTS, ACE_OF_CLUBS));
		verify(session).setAttribute("cardDeck", emptyCardDeckJson());
	}

	private Session session() {
		Session session = mock(Session.class);
		given(session.getSessionId()).willReturn("1234");
		return session;
	}

	private String emptyCardDeckJson() throws Exception {
		return cardDeckJson();
	}

	private String cardDeckJson(final Card... cards) throws Exception {
		CardDeck cardDeck = cardDeck(cards);
		return OBJECT_MAPPER.writeValueAsString(cardDeck);
	}

	private CardDeck cardDeck(final Card... cards) {
		CardDeck cardDeck = new CardDeck();
		setInternalState(cardDeck, "cards", Arrays.asList(cards));
		return cardDeck;
	}

	private Hand hand(final Card... cards) {
		return new Hand(Arrays.asList(cards));
	}

	private String handJson(final Card... cards) throws Exception {
		Hand hand = hand(cards);
		return OBJECT_MAPPER.writeValueAsString(hand);
	}
}
