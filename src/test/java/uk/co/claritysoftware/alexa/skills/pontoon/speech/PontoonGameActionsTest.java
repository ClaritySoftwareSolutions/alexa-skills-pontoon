package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.HashMap;
import java.util.Map;
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
	public void shouldDealInitialHandGivenHandWithNoAces() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 15);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "the Five of CLUBS, and the Queen of HEARTS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("initialHandStillInPlay.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The initial deal content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The initial deal content";
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
	public void shouldDealInitialHandGivenHandWithAceAndAceIsLow() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 11);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Ace of CLUBS, and the Queen of HEARTS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("initialHandStillInPlay.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The initial deal content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The initial deal content";
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
	public void shouldDealInitialHandGivenBustHandWithAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, ACE_OF_SPADES);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 22);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Ace of CLUBS, and the Ace of SPADES");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("initialHandBust.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The initial deal content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The initial deal content";

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
	public void shouldDealInitialHandGivenWinningHandWithAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 21);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Ace of CLUBS, and the Queen of HEARTS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("initialHandWin.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The initial deal content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The initial deal content";

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
	public void shouldTwistGivenHandWithNoAces() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(TWO_OF_SPADES);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 17);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "the Five of CLUBS, the Queen of HEARTS, and the Two of SPADES");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("handStillInPlay.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";
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
	public void shouldTwistGivenHandWithAcesAndAceIsLow() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 16);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, the Queen of HEARTS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("handStillInPlay.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";
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
	public void shouldTwistGivenHandWithAcesAndAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TWO_OF_SPADES);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 18);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, the Two of SPADES, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("handStillInPlay.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";
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
	public void shouldTwistGivenCardToMakeWinningHandWithAcesAndAceIsLow() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(TEN_OF_HEARTS, QUEEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 21);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Ten of HEARTS, the Queen of HEARTS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("win.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";

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
	public void shouldTwistGivenCardToMakeWinningHandWithAcesAndAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TWO_OF_SPADES, THREE_OF_SPADES);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 21);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, the Two of SPADES, the Three of SPADES, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("win.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";

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
	public void shouldTwistGivenCardToMakeWinningHand() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, SIX_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 21);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "the Five of CLUBS, the Six of CLUBS, and the Queen of HEARTS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("win.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The twist content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The twist content";

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
	public void shouldTwistGivenCardToMakeHandBustWithAcesAndAceIsLow() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(TEN_OF_HEARTS, FIVE_OF_CLUBS, SIX_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 22);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Ten of HEARTS, the Five of CLUBS, the Six of CLUBS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("bust.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The bust content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The bust content";

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
	public void shouldTwistGivenCardToMakeHandBustWithAcesAndAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(ACE_OF_CLUBS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 26);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, the Ten of HEARTS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("bust.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The bust content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The bust content";

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
	public void shouldTwistGivenCardToMakeHandBust() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		CardDeck cardDeck = cardDeck(QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 25);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "the Five of CLUBS, the Ten of HEARTS, and the Queen of HEARTS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("bust.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The bust content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The bust content";

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
	public void shouldStickGivenHandWithAcesAndAceIsLow() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		Hand hand = hand(FIVE_OF_CLUBS, ACE_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 6);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("stick.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The stick content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The stick content";

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
	public void shouldStickGivenHandWithAcesAndAceIsHigh() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		Hand hand = hand(FIVE_OF_CLUBS, ACE_OF_CLUBS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 16);
		expectedParameters.put("handContainsAnAce", true);
		expectedParameters.put("hand", "the Five of CLUBS, and the Ace of CLUBS");
		expectedParameters.put("aceIsHigh", true);

		Template template = mock(Template.class);
		given(configuration.getTemplate("stick.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The stick content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The stick content";

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
	public void shouldStick() throws Exception {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(false);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", true);
		expectedParameters.put("score", 15);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "the Five of CLUBS, and the Ten of HEARTS");
		expectedParameters.put("aceIsHigh", false);

		Template template = mock(Template.class);
		given(configuration.getTemplate("stick.ftl")).willReturn(template);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The stick content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

		String expectedPlainTextOutputSpeech = "The stick content";

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
	public void shouldDetermineIsGameAlreadyStartedGivenAllRequiredSessionAttributes() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(true);

		CardDeck cardDeck = cardDeck(QUEEN_OF_HEARTS);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(cardDeck);

		Hand hand = hand(FIVE_OF_CLUBS, TEN_OF_HEARTS);
		given(sessionSupport.getHandFromSession(session)).willReturn(hand);

		// When
		boolean gameAlreadyStarted = pontoonGameActions.isGameAlreadyStarted(session);

		// Then
		assertThat(gameAlreadyStarted).isTrue();
	}

	@Test
	public void shouldDetermineIsGameAlreadyStartedGivenNoRequiredAttributes() {
		// Given
		Session session = session();
		given(sessionSupport.getAceIsHighFromSession(session)).willReturn(null);
		given(sessionSupport.getCardDeckFromSession(session)).willReturn(null);
		given(sessionSupport.getHandFromSession(session)).willReturn(null);

		// When
		boolean gameAlreadyStarted = pontoonGameActions.isGameAlreadyStarted(session);

		// Then
		assertThat(gameAlreadyStarted).isFalse();
	}

	@Test
	public void shouldHelp() throws Exception {
		// Given
		Session session = session();
		Template template = mock(Template.class);
		given(configuration.getTemplate("help.ftl")).willReturn(template);

		Map expectedParameters = new HashMap();
		expectedParameters.put("gameAlreadyStarted", false);
		expectedParameters.put("score", 0);
		expectedParameters.put("handContainsAnAce", false);
		expectedParameters.put("hand", "");
		expectedParameters.put("aceIsHigh", false);

		doAnswer((InvocationOnMock invocationOnMock) -> {
			Writer writer = invocationOnMock.getArgumentAt(1, Writer.class);
			writer.append("The help content");
			return null;
		}).when(template).process(eq(expectedParameters), any(Writer.class));

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
