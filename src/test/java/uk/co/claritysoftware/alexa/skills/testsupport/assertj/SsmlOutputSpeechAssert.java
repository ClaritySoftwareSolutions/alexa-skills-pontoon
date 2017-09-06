package uk.co.claritysoftware.alexa.skills.testsupport.assertj;

import org.assertj.core.api.AbstractAssert;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * Assertj {@link AbstractAssert} for making assertions on {@link SsmlOutputSpeech} instances
 */
public class SsmlOutputSpeechAssert extends AbstractAssert<SsmlOutputSpeechAssert, SsmlOutputSpeech> {

	private SsmlOutputSpeechAssert(SsmlOutputSpeech outputSpeech) {
		super(outputSpeech, SsmlOutputSpeechAssert.class);
	}

	public static SsmlOutputSpeechAssert assertThat(SsmlOutputSpeech actual) {
		return new SsmlOutputSpeechAssert(actual);
	}

	/**
	 * Assert that the {@link SsmlOutputSpeech} has the specified speech ssml
	 *
	 * @param expectedSpeechSsml the expected speech ssml
	 * @return this {@link SsmlOutputSpeechAssert} for further assertion chaining
	 */
	public SsmlOutputSpeechAssert hasSsml(String expectedSpeechSsml) {
		final String actualSpeechSsml = this.actual.getSsml();

		if (!actualSpeechSsml.equals(expectedSpeechSsml)) {
			failWithMessage("Expected SsmlOutputSpeech to have ssml of <%s> but was <%s>", expectedSpeechSsml, actualSpeechSsml);
		}

		return this;
	}
}
