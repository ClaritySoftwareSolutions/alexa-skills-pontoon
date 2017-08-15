package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE;
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
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
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
	 * Performs the twist action by dealing another card from the {@ink CardDeck}
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}, the current {@link Hand} and ace is high flag
	 * @param session
	 * @return a {@link SpeechletResponse} describing the dealt hand and the current score
	 */
	public SpeechletResponse twist(final Session session) {
		LOG.debug("twist for session id {}", session.getSessionId());

		boolean aceIsHigh = sessionSupport.getAceIsHighFromSession(session);
		CardDeck cardDeck = sessionSupport.getCardDeckFromSession(session);
		LOG.debug("There are {} cards in the deck before twist operation", cardDeck.getCards().size());

		Hand hand = sessionSupport.getHandFromSession(session);
		LOG.debug("Before twist operation the hand is {}", hand);

		Card card = cardDeck.deal();
		hand.addCard(card);
		int score = hand.getScore(aceIsHigh);
		List<Card> cardsInHand = hand.getCards();

		sessionSupport.setCardDeckOnSession(session, cardDeck);
		sessionSupport.setHandOnSession(session, hand);

		StringBuffer speechText = new StringBuffer("Your hand is now");

		Iterator<String> cardsText = cardsInHand.stream()
				.map(currentCard -> String.format("the %s of %s", currentCard.getCardValue().getName(), currentCard.getCardSuit()))
				.collect(Collectors.toList())
				.iterator();

		while (cardsText.hasNext()) {
			String cardText = cardsText.next();

			if (cardsText.hasNext()) {
				speechText.append(" ").append(cardText).append(",");
			} else {
				speechText.append(" and ").append(cardText).append(". ");
			}
		}

		speechText.append(handContainsAnAce(cardsInHand) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "")
				.append("Your score is ").append(score).append(". What would you like to do?");

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText.toString());

		LOG.debug("There are {} cards left in the deck after twist operation", cardDeck.getCards().size());
		LOG.debug("twist response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
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

	private SpeechletResponse dealInitialHand(final Session session, final boolean aceIsHigh) {
		LOG.debug("dealInitialHand for session id {}, with aceIsHigh {}", session.getSessionId(), aceIsHigh);

		CardDeck cardDeck = sessionSupport.getCardDeckFromSession(session);

		Hand dealtHand = dealInitialHand(cardDeck);

		sessionSupport.setCardDeckOnSession(session, cardDeck);
		sessionSupport.setHandOnSession(session, dealtHand);

		Card card1 = dealtHand.getCards().get(0);
		Card card2 = dealtHand.getCards().get(1);
		int score = dealtHand.getScore(aceIsHigh);

		final String speechText = "I have dealt you the %s of %s, and the %s of %s. %sYour score is %d. What would you like to do?";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit(),
				handContainsAnAce(dealtHand.getCards()) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "",
				score
		));

		LOG.debug("There are {} cards left in the deck ofter dealInitialHand operation", cardDeck.getCards().size());
		LOG.debug("dealInitialHand response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private Hand dealInitialHand(final CardDeck cardDeck) {
		Card card1 = cardDeck.deal();
		Card card2 = cardDeck.deal();

		return new Hand(Arrays.asList(card1, card2));
	}

	private boolean handContainsAnAce(List<Card> cards) {
		return cards.stream()
				.anyMatch(card -> card.getCardValue() == ACE);
	}

}
