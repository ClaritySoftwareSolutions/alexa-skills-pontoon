package uk.co.claritysoftware.alexa.skills.pontoon.session;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.speechlet.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;

/**
 * Singleton class providing utility methods concerning @{link S}
 */
public class SessionSupport {

	private static final Logger LOG = LoggerFactory.getLogger(SessionSupport.class);

	public static final String CARD_DECK = "cardDeck";

	private static final String HAND = "hand";

	private static final String ACE_IS_HIGH = "aceIsHigh";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final SessionSupport INSTANCE = new SessionSupport();

	private SessionSupport() {

	}

	public static SessionSupport getInstance() {
		return INSTANCE;
	}

	public void setCardDeckOnSession(final Session session, final CardDeck cardDeck) {
		session.setAttribute(CARD_DECK, serializeCardDeck(cardDeck));
	}

	public CardDeck getCardDeckFromSession(final Session session) {
		return deserializeJsonToCardDeck((String) session.getAttribute(CARD_DECK));
	}

	public void setHandOnSession(final Session session, final Hand hand) {
		session.setAttribute(HAND, serializeHand(hand));
	}

	public Hand getHandFromSession(final Session session) {
		return deserializeJsonToHand((String) session.getAttribute(HAND));
	}

	public void setAceIsHighOnSession(final Session session, final boolean aceIsHigh) {
		session.setAttribute(ACE_IS_HIGH, aceIsHigh);
	}

	public boolean getAceIsHighFromSession(final Session session) {
		return (boolean) session.getAttribute(ACE_IS_HIGH);
	}

	private String serializeCardDeck(final CardDeck cardDeck) {
		try {
			return OBJECT_MAPPER.writeValueAsString(cardDeck);
		} catch (JsonProcessingException e) {
			LOG.error("Error serializing CardDeck {}, {}", cardDeck, e.getMessage());
			return null;
		}
	}

	private CardDeck deserializeJsonToCardDeck(final String json) {
		try {
			return OBJECT_MAPPER.readValue(json, CardDeck.class);
		} catch (IOException e) {
			LOG.error("Error deserializing json into CardDeck {}, {}", json, e.getMessage());
			return null;
		}
	}

	private String serializeHand(final Hand hand) {
		try {
			return OBJECT_MAPPER.writeValueAsString(hand);
		} catch (JsonProcessingException e) {
			LOG.error("Error serializing Hand {}, {}", hand, e.getMessage());
			return null;
		}
	}

	private Hand deserializeJsonToHand(final String json) {
		try {
			return OBJECT_MAPPER.readValue(json, Hand.class);
		} catch (IOException e) {
			LOG.error("Error deserializing json into Hand {}, {}", json, e.getMessage());
			return null;
		}
	}
}
