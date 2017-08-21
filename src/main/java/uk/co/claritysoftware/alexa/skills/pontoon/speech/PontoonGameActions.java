package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static com.amazon.speech.speechlet.SpeechletResponse.newTellResponse;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardValue.ACE;
import static uk.co.claritysoftware.alexa.skills.speech.factory.RepromptFactory.reprompt;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;

/**
 * Singleton class to perform the actions of a game of pontoon
 */
public class PontoonGameActions {

	private static final Logger LOG = LoggerFactory.getLogger(PontoonGameActions.class);

	private static final PontoonGameActions INSTANCE = new PontoonGameActions(SessionSupport.getInstance());

	private final SessionSupport sessionSupport;

	private PontoonGameActions(final SessionSupport sessionSupport) {
		this.sessionSupport = sessionSupport;
	}

	public static PontoonGameActions getInstance() {
		return INSTANCE;
	}

	/**
	 * Deals the initial hand of 2 cards from the {@link CardDeck} in the specified {@link Session}
	 *
	 * @param session the {@link Session} containing the {@link CardDeck} and ace is high flag
	 * @return a {@link SpeechletResponse} describing the dealt hand and the current score
	 */
	public SpeechletResponse dealInitialHand(final Session session) {
		boolean aceIsHigh = sessionSupport.getAceIsHighFromSession(session);
		return dealInitialHand(session, aceIsHigh);
	}

	/**
	 * Performs the twist action by dealing another card from the {@ink CardDeck}
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}, the current {@link Hand} and ace is high flag
	 * @return a {@link SpeechletResponse} describing the dealt hand and the current score
	 */
	public SpeechletResponse twist(final Session session) {
		LOG.debug("twist for session id {}", session.getSessionId());

		boolean aceIsHigh = sessionSupport.getAceIsHighFromSession(session);
		CardDeck cardDeck = sessionSupport.getCardDeckFromSession(session);
		Hand hand = sessionSupport.getHandFromSession(session);

		Card card = cardDeck.deal();
		hand.addCard(card);

		sessionSupport.setCardDeckOnSession(session, cardDeck);
		sessionSupport.setHandOnSession(session, hand);

		LOG.debug("There are {} cards left in the deck after twist operation", cardDeck.getCards().size());
		return twistSpeechletResponse(hand, aceIsHigh);
	}

	/**
	 * Performs the stick action by ending the game
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}, the current {@link Hand} and ace is high flag
	 * @return a {@link SpeechletResponse} describing the dealt hand and the current score
	 */
	public SpeechletResponse stick(final Session session) {
		LOG.debug("stick for session id {}", session.getSessionId());

		boolean aceIsHigh = sessionSupport.getAceIsHighFromSession(session);
		Hand hand = sessionSupport.getHandFromSession(session);

		return stickSpeechletResponse(hand, aceIsHigh);
	}

	private SpeechletResponse dealInitialHand(final Session session, final boolean aceIsHigh) {
		LOG.debug("dealInitialHand for session id {}, with aceIsHigh {}", session.getSessionId(), aceIsHigh);

		CardDeck cardDeck = sessionSupport.getCardDeckFromSession(session);
		Hand dealtHand = dealInitialHand(cardDeck);
		sessionSupport.setCardDeckOnSession(session, cardDeck);
		sessionSupport.setHandOnSession(session, dealtHand);

		LOG.debug("There are {} cards left in the deck ofter dealInitialHand operation", cardDeck.getCards().size());
		return initialDealSpeechletResponse(dealtHand, aceIsHigh);
	}

	private Hand dealInitialHand(final CardDeck cardDeck) {
		Card card1 = cardDeck.deal();
		Card card2 = cardDeck.deal();

		return new Hand(Arrays.asList(card1, card2));
	}

	private boolean handContainsAnAce(final List<Card> cards) {
		return cards.stream()
				.anyMatch(card -> card.getCardValue() == ACE);
	}

	private boolean handContainsAnAce(final Card... cards) {
		return handContainsAnAce(Arrays.asList(cards));
	}

	private SpeechletResponse initialDealSpeechletResponse(final Hand dealtHand, final boolean aceIsHigh) {
		Card card1 = dealtHand.getCards().get(0);
		Card card2 = dealtHand.getCards().get(1);
		int score = dealtHand.getScore(aceIsHigh);

		return dealtHand.isWin(aceIsHigh) ? initialHandWinResponse(card1, card2) :
				dealtHand.isBust(aceIsHigh) ? initialHandBustResponse(card1, card2) :
						initialHandStillInPlayResponse(card1, card2, score, aceIsHigh);
	}

	private SpeechletResponse initialHandStillInPlayResponse(final Card card1, final Card card2, final int score, final boolean aceIsHigh) {
		final String speechText = "I have dealt you the %s of %s, and the %s of %s. %sYour score is %d. What would you like to do?";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit(),
				handContainsAnAce(card1, card2) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "",
				score
		));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private SpeechletResponse initialHandWinResponse(final Card card1, final Card card2) {
		final String speechText = "Winner winner, chicken dinner! I have dealt you the %s of %s, and the %s of %s; and ace was high. Your score is 21. That's pontoon!";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit()
		));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private SpeechletResponse initialHandBustResponse(final Card card1, final Card card2) {
		final String speechText = "Bad luck buster! I have dealt you the %s of %s, and the %s of %s; and ace was high. Your score is 22, and you are bust!";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit()
		));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private SpeechletResponse twistSpeechletResponse(final Hand hand, final boolean aceIsHigh) {
		List<Card> cardsInHand = hand.getCards();
		int score = hand.getScore(aceIsHigh);

		return hand.isWin(aceIsHigh) ? handWinResponse(cardsInHand, aceIsHigh) :
				hand.isBust(aceIsHigh) ? handBustResponse(cardsInHand, score, aceIsHigh) :
						handStillInPlayResponse(cardsInHand, score, aceIsHigh);
	}

	private SpeechletResponse handStillInPlayResponse(final List<Card> cards, final int score, final boolean aceIsHigh) {
		StringBuffer speechText = new StringBuffer("Your hand is now")
				.append(cardListSentence(cards));

		speechText.append(handContainsAnAce(cards) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "")
				.append("Your score is ").append(score)
				.append(". What would you like to do?");

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText.toString());

		LOG.debug("twist response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private SpeechletResponse handWinResponse(final List<Card> cards, final boolean aceIsHigh) {
		StringBuffer speechText = new StringBuffer("Congratulations, you have a winning hand! Your cards are")
				.append(cardListSentence(cards));

		speechText.append(handContainsAnAce(cards) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "")
				.append("Nice one!");

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText.toString());

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private SpeechletResponse handBustResponse(final List<Card> cards, final int score, final boolean aceIsHigh) {
		StringBuffer speechText = new StringBuffer("Bad times! You've bust! Your hand is")
				.append(cardListSentence(cards));

		speechText.append(handContainsAnAce(cards) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "")
				.append("Your score is ").append(score)
				.append(". That was one twist too far. Better luck next time!!");

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText.toString());

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private SpeechletResponse stickSpeechletResponse(final Hand hand, final boolean aceIsHigh) {
		List<Card> cardsInHand = hand.getCards();
		int score = hand.getScore(aceIsHigh);

		StringBuffer speechText = new StringBuffer("Your final score is ").append(score)
				.append(" with a hand of")
				.append(cardListSentence(cardsInHand));

		speechText.append(handContainsAnAce(cardsInHand) ? String.format("Ace is %s.", aceIsHigh ? "high" : "low") : "");

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText.toString().trim());

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String cardListSentence(final List<Card> cards) {
		Iterator<String> cardsText = cards.stream()
				.map(currentCard -> String.format("the %s of %s", currentCard.getCardValue().getName(), currentCard.getCardSuit()))
				.collect(Collectors.toList())
				.iterator();

		StringBuffer sentence = new StringBuffer();
		while (cardsText.hasNext()) {
			String cardText = cardsText.next();

			if (cardsText.hasNext()) {
				sentence.append(" ").append(cardText).append(",");
			} else {
				sentence.append(" and ").append(cardText).append(". ");
			}
		}

		return sentence.toString();
	}
}
