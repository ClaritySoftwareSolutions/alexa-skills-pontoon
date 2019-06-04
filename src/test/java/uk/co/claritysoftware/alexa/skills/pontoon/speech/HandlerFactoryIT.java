package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.co.claritysoftware.alexa.skills.pontoon.dagger.DaggerPontoonComponent;
import uk.co.claritysoftware.alexa.skills.pontoon.dagger.PontoonComponent;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Integration test class for {@link HandlerFactory}
 */
public class HandlerFactoryIT {

	private final PontoonComponent pontoonComponent = DaggerPontoonComponent.create();

	private final HandlerFactory handlerFactory = pontoonComponent.buildHandlerFactory();

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
