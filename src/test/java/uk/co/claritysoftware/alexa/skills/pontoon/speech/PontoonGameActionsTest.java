package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardValue;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Unit test class for {@link PontoonGameActions}
 */
@RunWith(MockitoJUnitRunner.class)
public class PontoonGameActionsTest {

	private static final Card FIVE_OF_CLUBS = new Card(CardValue.FIVE, CardSuit.CLUBS);

	private static final Card SIX_OF_CLUBS = new Card(CardValue.SIX, CardSuit.CLUBS);

	private static final Card QUEEN_OF_HEARTS = new Card(CardValue.QUEEN, CardSuit.HEARTS);

	private static final Card TEN_OF_HEARTS = new Card(CardValue.TEN, CardSuit.HEARTS);

	private static final Card ACE_OF_CLUBS = new Card(CardValue.ACE, CardSuit.CLUBS);

	private static final Card TWO_OF_SPADES = new Card(CardValue.TWO, CardSuit.SPADES);

	private static final Card THREE_OF_SPADES = new Card(CardValue.THREE, CardSuit.SPADES);

	private static final Card ACE_OF_SPADES = new Card(CardValue.ACE, CardSuit.SPADES);

	@Mock
	private SessionSupport sessionSupport;

	@Mock
	private Configuration configuration;

	@InjectMocks
	private PontoonGameActions pontoonGameActions;

	@Test
	public void shouldDealInitialHandGivenHandWithNoAces() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Five of CLUBS, and the Queen of HEARTS. Your score is 15. You can twist or stick. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldDealInitialHandGivenHandWithAceAndAceIsLow() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		String expectedPlainTextOutputSpeech = "I have dealt you the Ace of CLUBS, and the Queen of HEARTS. Ace is low. Your score is 11. You can twist or stick. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(ACE_OF_CLUBS, QUEEN_OF_HEARTS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldDealInitialHandGivenBustHandWithAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, ACE_OF_SPADES);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		String expectedPlainTextOutputSpeech = "Bad luck buster! I have dealt you the Ace of CLUBS, and the Ace of SPADES; and ace was high. Your score is 22, and you are bust!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(ACE_OF_CLUBS, ACE_OF_SPADES);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldDealInitialHandGivenWinningHandWithAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		String expectedPlainTextOutputSpeech = "Winner winner, chicken dinner! I have dealt you the Ace of CLUBS, and the Queen of HEARTS; and ace was high. Your score is 21. That's pontoon!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(ACE_OF_CLUBS, QUEEN_OF_HEARTS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.dealInitialHand(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenHandWithNoAces() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(TWO_OF_SPADES);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Queen of HEARTS, and the Two of SPADES. Your score is 17. You can twist or stick. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS, TWO_OF_SPADES);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenHandWithAcesAndAceIsLow() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Queen of HEARTS, and the Ace of CLUBS. Ace is low. Your score is 16. You can twist or stick. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenHandWithAcesAndAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TWO_OF_SPADES);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your hand is now the Five of CLUBS, the Two of SPADES, and the Ace of CLUBS. Ace is high. Your score is 18. You can twist or stick. What would you like to do?";
		String expectedPlainTextReprompt = "What would you like to do?";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, TWO_OF_SPADES, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeWinningHandWithAcesAndAceIsLow() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(TEN_OF_HEARTS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Congratulations, you have a winning hand! Your cards are the Ten of HEARTS, the Queen of HEARTS, and the Ace of CLUBS. Ace is low. Nice one!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(TEN_OF_HEARTS, QUEEN_OF_HEARTS, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeWinningHandWithAcesAndAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TWO_OF_SPADES, THREE_OF_SPADES);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Congratulations, you have a winning hand! Your cards are the Five of CLUBS, the Two of SPADES, the Three of SPADES, and the Ace of CLUBS. Ace is high. Nice one!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, TWO_OF_SPADES, THREE_OF_SPADES, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeWinningHand() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, SIX_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Congratulations, you have a winning hand! Your cards are the Five of CLUBS, the Six of CLUBS, and the Queen of HEARTS. Nice one!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, SIX_OF_CLUBS, QUEEN_OF_HEARTS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeHandBustWithAcesAndAceIsLow() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(TEN_OF_HEARTS, FIVE_OF_CLUBS, SIX_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Bad times! You've bust! Your hand is the Ten of HEARTS, the Five of CLUBS, the Six of CLUBS, and the Ace of CLUBS. Ace is low. Your score is 22. That was one twist too far. Better luck next time!!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(TEN_OF_HEARTS, FIVE_OF_CLUBS, SIX_OF_CLUBS, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeHandBustWithAcesAndAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Bad times! You've bust! Your hand is the Five of CLUBS, the Ten of HEARTS, and the Ace of CLUBS. Ace is high. Your score is 26. That was one twist too far. Better luck next time!!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS, ACE_OF_CLUBS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldTwistGivenCardToMakeHandBust() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Bad times! You've bust! Your hand is the Five of CLUBS, the Ten of HEARTS, and the Queen of HEARTS. Your score is 25. That was one twist too far. Better luck next time!!";

		CardDeck expectedCardDeckOnSession = cardDeck();
		Hand expectedHandOnSession = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS, QUEEN_OF_HEARTS);

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.twist(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport).setCardDeckOnSession(session, expectedCardDeckOnSession);
		verify(sessionSupport).setHandOnSession(session, expectedHandOnSession);
	}

	@Test
	public void shouldStickGivenHandWithAcesAndAceIsLow() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		Hand hand = hand(FIVE_OF_CLUBS, ACE_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your final score is 6 with a hand of the Five of CLUBS, and the Ace of CLUBS. Ace is low.";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.stick(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport, never()).setAceIsHighOnSession(any(), anyBoolean());
		verify(sessionSupport, never()).setHandOnSession(any(), any());
		verify(sessionSupport, never()).setCardDeckOnSession(any(), any());
	}

	@Test
	public void shouldStickGivenHandWithAcesAndAceIsHigh() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		Hand hand = hand(FIVE_OF_CLUBS, ACE_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your final score is 16 with a hand of the Five of CLUBS, and the Ace of CLUBS. Ace is high.";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.stick(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport, never()).setAceIsHighOnSession(any(), anyBoolean());
		verify(sessionSupport, never()).setHandOnSession(any(), any());
		verify(sessionSupport, never()).setCardDeckOnSession(any(), any());
	}

	@Test
	public void shouldStick() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		String expectedPlainTextOutputSpeech = "Your final score is 15 with a hand of the Five of CLUBS, and the Ten of HEARTS.";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.stick(session);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		verify(sessionSupport, never()).setAceIsHighOnSession(any(), anyBoolean());
		verify(sessionSupport, never()).setHandOnSession(any(), any());
		verify(sessionSupport, never()).setCardDeckOnSession(any(), any());
	}

	@Test
	public void shouldHelp() throws Exception {
		// Given
		Session session = session();
		Template template = mock(Template.class);
		given(configuration.getTemplate("help.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The help content");
			return null;
		}).when(template).process(eq(Collections.EMPTY_MAP), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The help content";
		String expectedPlainTextReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.help(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
		assertThat(speechletResponse.getReprompt()).hasPlainTextOutputSpeech(expectedPlainTextReprompt);
	}
	private Session session() {
		Session session = mock(Session.class);
		given(session.getSessionId()).willReturn("1234");
		return session;
	}

	private CardDeck cardDeck(final Card... cards) {
		CardDeck cardDeck = new CardDeck();
		setInternalState(cardDeck, "cards", new ArrayList(Arrays.asList(cards)));
		return cardDeck;
	}

	private Hand hand(final Card... cards) {
		return new Hand(new ArrayList(Arrays.asList(cards)));
	}
}
