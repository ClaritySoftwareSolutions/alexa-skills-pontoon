package uk.co.claritysoftware.alexa.skills.pontoon.configuration;

import static java.util.Arrays.asList;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.HelpIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StartGameIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StickIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StopIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.TwistIntentHandler;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * @author Nathan Russell
 */
public class IntentHandlerConfiguration {

	@ApplicationScoped
	@Inject
	public List<IntentHandler> intentHandlers(final StartGameIntentHandler startGameIntentHandler,
			final HelpIntentHandler helpIntentHandler,
			final TwistIntentHandler twistIntentHandler,
			final StickIntentHandler stickIntentHandler,
			final StopIntentHandler stopIntentHandler) {
		return asList(startGameIntentHandler, helpIntentHandler, twistIntentHandler, stickIntentHandler, stopIntentHandler);
	}
}
