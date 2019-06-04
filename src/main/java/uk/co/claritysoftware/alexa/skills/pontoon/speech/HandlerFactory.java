package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import java.util.List;
import javax.inject.Inject;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Simple class to return registered Handlers
 */
public class HandlerFactory {

	private final LaunchHandler launchHandler;

	private final List<IntentHandler> intentHandlers;

	@Inject
	public HandlerFactory(final LaunchHandler launchHandler, final List<IntentHandler> intentHandlers) {
		this.launchHandler = launchHandler;
		this.intentHandlers = intentHandlers;
	}

	/**
	 * @return the {@link LaunchHandler}
	 */
	public LaunchHandler getLaunchHandler() {
		return launchHandler;
	}

	public IntentHandler getIntentHandlerForIntent(final PontoonIntent pontoonIntent) {
		return intentHandlers.stream()
				.filter(intentHandler -> intentHandler.handles(pontoonIntent))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No intent handler registered for " + pontoonIntent));
	}

}
