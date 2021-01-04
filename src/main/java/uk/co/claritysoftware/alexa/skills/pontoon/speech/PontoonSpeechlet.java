package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.GameAlreadyStartedException;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.GameNotStartedException;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.InvalidGameStateException;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.AbstractSpeechlet;

/**
 * Pontoon implementation of {@link SpeechletV2}
 */
@ApplicationScoped
public class PontoonSpeechlet extends AbstractSpeechlet {

	private static final Logger LOG = LoggerFactory.getLogger(PontoonSpeechlet.class);

	private final SessionSupport sessionSupport;

	private final HandlerFactory handlerFactory;

	@Inject
	public PontoonSpeechlet(final SessionSupport sessionSupport, final HandlerFactory handlerFactory) {
		this.sessionSupport = sessionSupport;
		this.handlerFactory = handlerFactory;
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

		return handlerFactory.getLaunchHandler()
				.handle(requestEnvelope);
	}

	@Override
	public SpeechletResponse onIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		LOG.debug("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		final String intentName = getIntentName(requestEnvelope);
		final PontoonIntent pontoonIntent = PontoonIntent.from(intentName)
				.orElseThrow(() -> new IllegalArgumentException("No intent with name " + intentName));

		try {
			return handlerFactory.getIntentHandlerForIntent(pontoonIntent)
					.handleIntent(requestEnvelope);
		} catch (InvalidGameStateException e) {
			LOG.warn("Cannot perform action {} as game {}", pontoonIntent,
					e.getClass() == GameNotStartedException.class ? "not started yet" :
							e.getClass() == GameAlreadyStartedException.class ? "already started" :
									"");
			return handlerFactory.getIntentHandlerForIntent(PontoonIntent.HELP_INTENT)
					.handleIntent(requestEnvelope);
		}
	}

	private String getIntentName(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		return requestEnvelope.getRequest().getIntent().getName();
	}

	private CardDeck shuffledDeckOfCards() {
		boolean shuffleDeck = true;
		return new CardDeck(shuffleDeck);
	}
}
