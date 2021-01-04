package uk.co.claritysoftware.alexa.skills.pontoon;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet;

/**
 * Main {@link RequestStreamHandler} for the Pontoon Alexa Skill
 */
public final class PontoonRequestStreamHandler extends SpeechletRequestStreamHandler {

	private static final Logger LOG = LoggerFactory.getLogger(PontoonRequestStreamHandler.class);

	private static final String APPLICATION_IDS = "com_amazon_speech_speechlet_servlet_supportedApplicationIds";

	@Inject
	public PontoonRequestStreamHandler(final PontoonSpeechlet pontoonSpeechlet) {
		super(pontoonSpeechlet, applicationIds());
	}

	private static Set<String> applicationIds() {
		final String appIds = System.getenv(APPLICATION_IDS) != null ? System.getenv(APPLICATION_IDS) : "";
		final Set<String> applicationIds = Arrays.stream(appIds.split(",\\s*"))
				.map(String::trim)
				.filter(applicationId -> applicationId.length() > 0)
				.collect(Collectors.toSet());

		if (applicationIds.isEmpty()) {
			throw new IllegalStateException("Cannot instantiate PontoonRequestStreamHandler with null or empty " + APPLICATION_IDS + " system property");
		}

		LOG.trace("Returning application ids {}", applicationIds);
		return applicationIds;
	}
}
