package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.AbstractSpeechlet;

/**
 * Dice implementation of {@link SpeechletV2}
 */
public class PontoonSpeechlet extends AbstractSpeechlet {

	private static final Logger LOG = LoggerFactory.getLogger(PontoonSpeechlet.class);

	private final SessionSupport sessionSupport;

	public PontoonSpeechlet(final SessionSupport sessionSupport) {
		this.sessionSupport = sessionSupport;
	}

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		LOG.debug("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		CardDeck cardDeck = shuffledDeckOfCards();
		sessionSupport.setCardDeckOnSession(requestEnvelope.getSession(), cardDeck);
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		LOG.debug("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		return new LaunchHandler(PontoonGameActions.getInstance(), SessionSupport.getInstance())
				.handle(requestEnvelope);
	}

	@Override
	public SpeechletResponse onIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		final String intentName = getIntentName(requestEnvelope);
		return PontoonIntent.from(intentName)
				.orElseThrow(() -> new IllegalArgumentException("No intent with name " + intentName))
				.getIntentHandler()
				.handleIntent(requestEnvelope);
	}

	private String getIntentName(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		return requestEnvelope.getRequest().getIntent().getName();
	}

	private CardDeck shuffledDeckOfCards() {
		boolean shuffleDeck = true;
		return new CardDeck(shuffleDeck);
	}
}
