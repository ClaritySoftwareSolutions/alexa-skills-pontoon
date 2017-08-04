package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * @author Nathan Russell
 */
public class LaunchHandler {

	private static final Logger LOG = LoggerFactory.getLogger(LaunchHandler.class);

	private final PontoonGameActions pontoonGameActions;

	public LaunchHandler(final PontoonGameActions pontoonGameActions) {
		this.pontoonGameActions = pontoonGameActions;
	}

	public SpeechletResponse handle(final SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		LOG.debug("handle requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		boolean aceIsHigh = false;

		return pontoonGameActions.dealInitialHand(session, aceIsHigh);
	}
}
