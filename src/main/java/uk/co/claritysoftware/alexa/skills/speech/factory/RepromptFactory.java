package uk.co.claritysoftware.alexa.skills.speech.factory;

import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;

/**
 * Simple factory providing static methods to return {@link Reprompt} instances
 */
public class RepromptFactory {

	/**
	 * @param prompt the prompt text
	 * @return a {@link Reprompt} with the specified text
	 */
	public static Reprompt reprompt(final String prompt) {
		final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(prompt);

		final Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(outputSpeech);

		return reprompt;
	}

	/**
	 * @return a {@link Reprompt} with text asking what the user would like to do next
	 */
	public static Reprompt whatNextReprompt() {
		return reprompt("What would you like me to do next?");
	}
}
