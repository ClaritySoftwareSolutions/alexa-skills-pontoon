package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Integration test class for {@link HandlerFactory}
 */
@QuarkusTest
public class HandlerFactoryIT {

	@Inject
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
