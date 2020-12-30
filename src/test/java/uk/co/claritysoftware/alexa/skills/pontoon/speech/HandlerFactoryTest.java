package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Unit test class for {@link HandlerFactory}
 */
public class HandlerFactoryTest {

	private HandlerFactory handlerFactory;

	private LaunchHandler launchHandler;

	private List<IntentHandler> intentHandlers;

	private IntentHandler intentHandler;

	@BeforeEach
	public void setup() {
		launchHandler = mock(LaunchHandler.class);
		intentHandler = mock(IntentHandler.class);
		intentHandlers = Collections.singletonList(intentHandler);
		handlerFactory = new HandlerFactory(launchHandler, intentHandlers);
	}

	@Test
	public void shouldGetLaunchHandler() {
		// Given
		LaunchHandler expectedLaunchHandler = launchHandler;
		// When
		LaunchHandler returnedLaunchHandler = handlerFactory.getLaunchHandler();

		// Then
		assertThat(returnedLaunchHandler).isEqualTo(expectedLaunchHandler);
	}

	@Test
	public void shouldGetIntentHandlerForIntent() {
		// Given
		PontoonIntent pontoonIntent = PontoonIntent.START_GAME_INTENT;
		given(intentHandler.handles(pontoonIntent)).willReturn(true);
		IntentHandler expectedIntentHandler = intentHandler;

		// When
		IntentHandler returnedIntentHandler = handlerFactory.getIntentHandlerForIntent(pontoonIntent);

		// Then
		assertThat(returnedIntentHandler).isEqualTo(expectedIntentHandler);
	}

	@Test
	public void shouldFailToGetIntentHandlerForIntentGivenNoRegidteredHandlersForIntent() {
		// Given
		PontoonIntent pontoonIntent = PontoonIntent.START_GAME_INTENT;
		given(intentHandler.handles(pontoonIntent)).willReturn(false);

		// When
		Throwable e = catchThrowable(() -> handlerFactory.getIntentHandlerForIntent(pontoonIntent));

		// Then
		assertThat(e)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("No intent handler registered for START_GAME_INTENT");
	}

}
