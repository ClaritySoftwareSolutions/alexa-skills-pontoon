package uk.co.claritysoftware.alexa.skills.pontoon.configuration;

import static freemarker.template.Configuration.VERSION_2_3_23;
import static freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER;

import javax.enterprise.context.ApplicationScoped;
import uk.co.claritysoftware.alexa.skills.pontoon.PontoonRequestStreamHandler;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;

/**
 * Class defining Freemarker CDI beans.
 */
public class FreemarkerConfiguration {

	@ApplicationScoped
	public Configuration configuration() {
		Configuration configuration = new Configuration(VERSION_2_3_23);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLogTemplateExceptions(false);
		configuration.setTemplateExceptionHandler(RETHROW_HANDLER);
		configuration.setTemplateLoader(new ClassTemplateLoader(PontoonRequestStreamHandler.class, "/uk/co/claritysoftware/alexa/skills/pontoon/speech/templates"));
		return configuration;
	}
}
