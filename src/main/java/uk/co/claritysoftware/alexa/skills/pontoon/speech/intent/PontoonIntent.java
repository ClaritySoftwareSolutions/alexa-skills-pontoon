package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.HelpIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.LaunchIntentHandler;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Enum of Dice Roller {@link AlexaIntent Alexa Intents}
 */
public enum PontoonIntent implements AlexaIntent {

	LAUNCH_INTENT("LaunchIntent", new LaunchIntentHandler()),

	HELP_INTENT("AMAZON.HelpIntent", new HelpIntentHandler());

	private final String value;

	private final IntentHandler intentHandler;

	PontoonIntent(final String value, final IntentHandler intentHandler) {
		this.value = value;
		this.intentHandler = intentHandler;
	}

	@Override
	public IntentHandler getIntentHandler() {
		return intentHandler;
	}

	public static Optional<PontoonIntent> from(final String value) {
		return Stream.of(PontoonIntent.values())
				.filter(pontoonIntent -> pontoonIntent.value.equals(value))
				.findFirst();
	}
}
