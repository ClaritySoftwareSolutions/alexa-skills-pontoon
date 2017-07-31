package uk.co.claritysoftware.alexa.skills.testsupport.assertj;

import org.assertj.core.api.AbstractAssert;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * Assertj {@link AbstractAssert} for making assertions on {@link Reprompt} instances
 */
public class RepromptAssert extends AbstractAssert<RepromptAssert, Reprompt> {

	private RepromptAssert(Reprompt reprompt) {
		super(reprompt, RepromptAssert.class);
	}

	public static RepromptAssert assertThat(Reprompt actual) {
		return new RepromptAssert(actual);
	}

	/**
	 * Assert that the {@link Reprompt} has the specified speech ssml
	 *
	 * @param expectedSpeechText the expected speech ssml
	 * @return this {@link RepromptAssert} for further assertion chaining
	 */
	public RepromptAssert hasPlainTextOutputSpeech(String expectedSpeechText) {
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) this.actual.getOutputSpeech();
		PlainTextOutputSpeechAssert.assertThat(outputSpeech).hasText(expectedSpeechText);

		return this;
	}

	/**
	 * Assert that the {@link Reprompt} has the specified speech ssml
	 *
	 * @param expectedSpeechSsml the expected speech ssml
	 * @return this {@link RepromptAssert} for further assertion chaining
	 */
	public RepromptAssert hasSsmlOutputSpeech(String expectedSpeechSsml) {
		SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) this.actual.getOutputSpeech();
		SsmlOutputSpeechAssert.assertThat(outputSpeech).hasSsml(expectedSpeechSsml);

		return this;
	}

}
