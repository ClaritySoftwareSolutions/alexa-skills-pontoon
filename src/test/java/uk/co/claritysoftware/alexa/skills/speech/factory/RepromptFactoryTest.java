package uk.co.claritysoftware.alexa.skills.speech.factory;

import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.RepromptAssert.assertThat;

import org.junit.jupiter.api.Test;
import com.amazon.speech.ui.Reprompt;

/**
 * Unit test class for {@link RepromptFactory}
 */
public class RepromptFactoryTest {

	@Test
	public void shouldReprompt() {
		// Given
		String repromptText = "What now?";

		// When
		Reprompt reprompt = RepromptFactory.reprompt(repromptText);

		// Then
		assertThat(reprompt).hasPlainTextOutputSpeechWithText(repromptText);
	}

	@Test
	public void shouldWhatNextReprompt() {
		// Given
		String expectedRepromptText = "What would you like me to do next?";

		// When
		Reprompt reprompt = RepromptFactory.whatNextReprompt();

		// Then
		assertThat(reprompt).hasPlainTextOutputSpeechWithText(expectedRepromptText);
	}
}
