package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;

/**
 * Class proving a method to handle the launch event
 */
public class LaunchHandler {

	private static final Logger LOG = LoggerFactory.getLogger(LaunchHandler.class);

	private final PontoonGameActions pontoonGameActions;

	private final SessionSupport sessionSupport;

	public LaunchHandler(final PontoonGameActions pontoonGameActions, final SessionSupport sessionSupport) {
		this.pontoonGameActions = pontoonGameActions;
		this.sessionSupport = sessionSupport;
	}

	public SpeechletResponse handle(final SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		LOG.debug("handle requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		boolean aceIsHigh = false;
		sessionSupport.setAceIsHighOnSession(session, aceIsHigh);

		return pontoonGameActions.dealInitialHand(session);
	}

}
