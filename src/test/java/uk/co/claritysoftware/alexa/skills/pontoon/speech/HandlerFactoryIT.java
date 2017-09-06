package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Integration test class for {@link HandlerFactory}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
public class HandlerFactoryIT {

	@Autowired
	private HandlerFactory handlerFactory;

	@Test
	public void shouldGetLaunchHandler() {
		// Given

		// When
		LaunchHandler launchHandler = handlerFactory.getLaunchHandler();

		// Then
		assertThat(launchHandler).isNotNull();
	}

	@Test
	public void shouldGetIntentHandlerForIntent() {
		// Given
		PontoonIntent pontoonIntent = PontoonIntent.START_GAME_INTENT;
		// When
		IntentHandler intentHandler = handlerFactory.getIntentHandlerForIntent(pontoonIntent);

		// Then
		assertThat(intentHandler).isNotNull();
	}

}
