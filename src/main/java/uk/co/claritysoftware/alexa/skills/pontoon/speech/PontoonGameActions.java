package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static com.amazon.speech.speechlet.SpeechletResponse.newAskResponse;
import static com.amazon.speech.speechlet.SpeechletResponse.newTellResponse;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardValue.ACE;
import static uk.co.claritysoftware.alexa.skills.speech.factory.RepromptFactory.reprompt;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Class to perform the actions of a game of pontoon
 */
@Service
public class PontoonGameActions {

	private static final Logger LOG = LoggerFactory.getLogger(PontoonGameActions.class);

	private final SessionSupport sessionSupport;

	private final Configuration configuration;

	@Autowired
	public PontoonGameActions(final SessionSupport sessionSupport, final Configuration configuration) {
		this.sessionSupport = sessionSupport;
		this.configuration = configuration;
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
	 * Performs the twist action by dealing another card from the {@link CardDeck}
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

	/**
	 * Performs the help action
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}.
	 * @return a {@link SpeechletResponse} containing the help
	 */
	public SpeechletResponse help(final Session session) {
		LOG.debug("help for session id {}", session.getSessionId());

		return helpSpeechletResponse(isGameAlreadyStarted(session));
	}

	/**
	 * Performs the stop action
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}.
	 * @return a {@link SpeechletResponse} containing the help
	 */
	public SpeechletResponse stop(final Session session) {
		LOG.debug("stop for session id {}", session.getSessionId());

		if (isGameAlreadyStarted(session)) {
			return stick(session);
		} else {
			return stopSpeechletResponse();
		}
	}

	/**
	 * Determines if there is already a game in play by looking for the required attributes on the session
	 *
	 * @param session the {@link Session} containing the {@link CardDeck}, the current {@link Hand} and ace is high flag
	 * @return true if there is already a game in play
	 */
	public boolean isGameAlreadyStarted(final Session session) {
		return sessionSupport.getAceIsHighFromSession(session) != null &&
				sessionSupport.getHandFromSession(session) != null &&
				sessionSupport.getCardDeckFromSession(session) != null;
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
		List<Card> cards = Arrays.asList(card1, card2);
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(initialHandStillInPlayContent(score, cardListSentence(cards), handContainsAnAce(cards), aceIsHigh));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private String initialHandStillInPlayContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("initialHandStillInPlay.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse initialHandWinResponse(final Card card1, final Card card2) {
		List<Card> cards = Arrays.asList(card1, card2);
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(initialHandWinContent(21, cardListSentence(cards), true, true));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String initialHandWinContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("initialHandWin.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse initialHandBustResponse(final Card card1, final Card card2) {
		List<Card> cards = Arrays.asList(card1, card2);
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(initialHandBustContent(22, cardListSentence(cards), true, true));

		LOG.debug("dealInitialHand response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String initialHandBustContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("initialHandBust.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse twistSpeechletResponse(final Hand hand, final boolean aceIsHigh) {
		List<Card> cardsInHand = hand.getCards();
		int score = hand.getScore(aceIsHigh);

		return hand.isWin(aceIsHigh) ? handWinResponse(cardsInHand, aceIsHigh) :
				hand.isBust(aceIsHigh) ? handBustResponse(cardsInHand, score, aceIsHigh) :
						handStillInPlayResponse(cardsInHand, score, aceIsHigh);
	}

	private SpeechletResponse handStillInPlayResponse(final List<Card> cards, final int score, final boolean aceIsHigh) {
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(handStillInPlayContent(score, cardListSentence(cards), handContainsAnAce(cards), aceIsHigh));

		LOG.debug("twist response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private String handStillInPlayContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("handStillInPlay.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse handWinResponse(final List<Card> cards, final boolean aceIsHigh) {
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(handWinContent(21, cardListSentence(cards), handContainsAnAce(cards), aceIsHigh));

		LOG.debug("twist response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String handWinContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("win.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse handBustResponse(final List<Card> cards, final int score, final boolean aceIsHigh) {
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(bustContent(score, cardListSentence(cards), handContainsAnAce(cards), aceIsHigh));

		LOG.debug("twist response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String bustContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("bust.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse stickSpeechletResponse(final Hand hand, final boolean aceIsHigh) {
		List<Card> cardsInHand = hand.getCards();
		int score = hand.getScore(aceIsHigh);

		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(stickContent(score, cardListSentence(cardsInHand), handContainsAnAce(cardsInHand), aceIsHigh));

		LOG.debug("stick response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String stickContent(final int score, final String cardListSentence, final boolean handContainsAnAce, final boolean aceIsHigh) {
		return freemarkerTemplateContent("stick.ftl", true, score, cardListSentence, handContainsAnAce, aceIsHigh);
	}

	private SpeechletResponse helpSpeechletResponse(boolean gameAlreadyStarted) {
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(helpContent(gameAlreadyStarted));

		LOG.debug("help response {}", speech.getText());
		return newAskResponse(speech, reprompt("What would you like to do?"));
	}

	private String helpContent(boolean gameAlreadyStarted) {
		return freemarkerTemplateContent("help.ftl", gameAlreadyStarted, 0, "", false, false);
	}

	private SpeechletResponse stopSpeechletResponse() {
		final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(stopContent());

		LOG.debug("stop response {}", speech.getText());
		return newTellResponse(speech);
	}

	private String stopContent() {
		return freemarkerTemplateContent("stop.ftl", false, 0, "", false, false);
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
				sentence.append(cardText).append(", ");
			} else {
				sentence.append("and ").append(cardText);
			}
		}

		return sentence.toString();
	}

	private String freemarkerTemplateContent(final String templateName, final boolean gameAlreadyStarted, final int score, final String cardListSentence, final boolean handContainsAnAce,
			final boolean aceIsHigh) {
		Map<String, Object> parameters = standardParametersMapForTemplates(gameAlreadyStarted, score, cardListSentence, handContainsAnAce, aceIsHigh);

		Writer writer = new StringWriter();
		try {
			Template template = configuration.getTemplate(templateName);
			template.process(parameters, writer);
		} catch (IOException | TemplateException e) {
			LOG.error("Error processing " + templateName + " template", e);
		}
		return writer.toString();
	}

	public Map<String, Object> standardParametersMapForTemplates(final boolean gameAlreadyStarted, final int score, final String cardListSentence, final boolean handContainsAnAce,
			final boolean aceIsHigh) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("gameAlreadyStarted", gameAlreadyStarted);
		parameters.put("score", score);
		parameters.put("hand", cardListSentence);
		parameters.put("handContainsAnAce", handContainsAnAce);
		parameters.put("aceIsHigh", aceIsHigh);
		return parameters;
	}
}
