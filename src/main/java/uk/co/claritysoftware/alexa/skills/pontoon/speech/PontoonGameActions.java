package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE;
import static uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet.CARD_DECK;
import static uk.co.claritysoftware.alexa.skills.speech.factory.RepromptFactory.reprompt;

import java.util.Arrays;
import java.util.stream.Stream;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;

/**
 * Class to perform the actions of a game of pontoon
 */
public class PontoonGameActions {

	private static final PontoonGameActions INSTANCE = new PontoonGameActions();

	private PontoonGameActions() {

	}

	public static PontoonGameActions getInstance() {
		return INSTANCE;
	}

	/**
	 * @param session
	 * @param aceIsHigh
	 * @return
	 */
	public SpeechletResponse dealInitialHand(final Session session, final boolean aceIsHigh) {
		CardDeck cardDeck = getCardDeckFromSession(session);

		Hand dealtHand = dealInitialHand(cardDeck, aceIsHigh);

		Card card1 = dealtHand.getCards().get(0);
		Card card2 = dealtHand.getCards().get(1);
		int score = dealtHand.getScore();

		final String speechText = "I have dealt you the %s of %s, and the %s of %s. %sYour score is %d. What would you like to do?";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit(),
				handContainsAnAce(card1, card2) ? String.format("Ace is %s. ", aceIsHigh ? "high" : "low") : "",
				score
		));

		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private Hand dealInitialHand(final CardDeck cardDeck, final boolean aceIsHIgh) {
		Card card1 = cardDeck.deal();
		Card card2 = cardDeck.deal();

		int score = card1.getValue(aceIsHIgh) + card2.getValue(aceIsHIgh);

		return new Hand(score, Arrays.asList(card1, card2));
	}

	private CardDeck getCardDeckFromSession(Session session) {
		return (CardDeck) session.getAttribute(CARD_DECK);
	}

	private boolean handContainsAnAce(Card... cards) {
		return Stream.of(cards)
				.filter(card -> card.getCardValue() == ACE)
				.findAny()
				.isPresent();
	}

}
