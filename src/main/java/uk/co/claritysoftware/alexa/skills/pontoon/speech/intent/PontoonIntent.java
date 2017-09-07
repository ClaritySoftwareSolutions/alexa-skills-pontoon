package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;

/**
 * Enum of Dice Roller {@link AlexaIntent Alexa Intents}
 */
public enum PontoonIntent implements AlexaIntent {

	START_GAME_INTENT("StartGameIntent"),

	TWIST_INTENT("TwistIntent"),

	STICK_INTENT("StickIntent"),

	HELP_INTENT("AMAZON.HelpIntent"),

	STOP_INTENT("AMAZON.StopIntent");

	private final String value;

	PontoonIntent(final String value) {
		this.value = value;
	}

	public static Optional<PontoonIntent> from(final String value) {
		return Stream.of(PontoonIntent.values())
				.filter(pontoonIntent -> pontoonIntent.value.equals(value))
				.findFirst();
	}
}
