package uk.co.claritysoftware.alexa.skills.pontoon.dagger;

import static freemarker.template.Configuration.VERSION_2_3_23;
import static freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER;
import static java.util.Arrays.asList;

import java.util.List;
import uk.co.claritysoftware.alexa.skills.pontoon.PontoonRequestStreamHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.HelpIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StartGameIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StickIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StopIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.TwistIntentHandler;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

import dagger.Module;
import dagger.Provides;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;

/**
 * Dagger 2 {@link Module} to provide the Pontoon dependencies
 */
@Module
public class PontoonModule {

	@Provides
	public Configuration providesConfiguration() {
		Configuration configuration = new Configuration(VERSION_2_3_23);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLogTemplateExceptions(false);
		configuration.setTemplateExceptionHandler(RETHROW_HANDLER);
		configuration.setTemplateLoader(new ClassTemplateLoader(PontoonRequestStreamHandler.class, "/uk/co/claritysoftware/alexa/skills/pontoon/speech/templates"));
		return configuration;
	}

	@Provides
	public SessionSupport providesSessionSupport() {
		return new SessionSupport();
	}

	@Provides
	public List<IntentHandler> providesIntentHandlers(final StartGameIntentHandler startGameIntentHandler,
			final HelpIntentHandler helpIntentHandler,
			final TwistIntentHandler twistIntentHandler,
			final StickIntentHandler stickIntentHandler,
			final StopIntentHandler stopIntentHandler) {
		return asList(startGameIntentHandler, helpIntentHandler, twistIntentHandler, stickIntentHandler, stopIntentHandler);
	}
}