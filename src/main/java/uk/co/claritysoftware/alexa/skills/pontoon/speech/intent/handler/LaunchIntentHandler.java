package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE_HIGH;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE_LOW;
import static uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet.CARD_DECK;
import static uk.co.claritysoftware.alexa.skills.speech.factory.RepromptFactory.reprompt;

import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.CoreSpeechletRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * {@link IntentHandler} for the Launch intent
 */
public class LaunchIntentHandler implements IntentHandler {

	private static final Logger LOG = LoggerFactory.getLogger(LaunchIntentHandler.class);

	@Override
	public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<? extends CoreSpeechletRequest> requestEnvelope) {
		LOG.debug("handleIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		CardDeck cardDeck = getCardDeckFromSession(requestEnvelope.getSession());

		Card card1 = cardDeck.deal();
		Card card2 = cardDeck.deal();

		int score = card1.getValue() + card2.getValue();

		final String speechText = "I have dealt you the %s of %s, and the %s of %s. %sYour score is %d. What would you like to do?";

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(speechText,
				card1.getCardValue().getName(), card1.getCardSuit(),
				card2.getCardValue().getName(), card2.getCardSuit(),
				handContainsAnAce(card1, card2) ? "Ace is low. " : "",
				score
		));

		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private CardDeck getCardDeckFromSession(Session session) {
		return (CardDeck) session.getAttribute(CARD_DECK);
	}

	private boolean handContainsAnAce(Card... cards) {
		return Stream.of(cards)
				.filter(card -> card.getCardValue() == ACE_LOW || card.getCardValue() == ACE_HIGH)
				.findAny()
				.isPresent();
	}

}
