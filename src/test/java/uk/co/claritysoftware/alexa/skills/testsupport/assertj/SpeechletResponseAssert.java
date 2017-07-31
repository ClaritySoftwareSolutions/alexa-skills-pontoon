package uk.co.claritysoftware.alexa.skills.testsupport.assertj;

import java.util.regex.Pattern;
import org.assertj.core.api.AbstractAssert;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * Assertj {@link AbstractAssert} for making assertions on {@link SpeechletResponse} instances
 */
public class SpeechletResponseAssert extends AbstractAssert<SpeechletResponseAssert, SpeechletResponse> {

	private SpeechletResponseAssert(SpeechletResponse reprompt) {
		super(reprompt, SpeechletResponseAssert.class);
	}

	public static SpeechletResponseAssert assertThat(SpeechletResponse actual) {
		return new SpeechletResponseAssert(actual);
	}

	/**
	 * Assert that the {@link SpeechletResponse} is an Ask Response
	 *
	 * @return this {@link SpeechletResponseAssert} for further assertion chaining
	 */
	public SpeechletResponseAssert isAnAskResponse() {
		if (this.actual.getReprompt() == null) {
			failWithMessage("Was expecting a Reprompt object, but there was none. This is likely to be a Tell Response");
		}
		if (this.actual.getShouldEndSession()) {
			failWithMessage("Was expecting the session to be set to not end, but it was. This is likely to be a Tell Response");
		}
		return this;
	}

	/**
	 * Assert that the {@link SpeechletResponse} is a Tell Response
	 *
	 * @return this {@link SpeechletResponseAssert} for further assertion chaining
	 */
	public SpeechletResponseAssert isATellResponse() {
		if (this.actual.getReprompt() != null) {
			failWithMessage("Was expecting not to have a Reprompt object, but there was one. This is likely to be an Ask Response");
		}
		if (!this.actual.getShouldEndSession()) {
			failWithMessage("Was expecting the session to be set to end, but it wasn't. This is likely to be an Ask Response");
		}
		return this;
	}

	/**
	 * Assert that the {@link SpeechletResponse} has the specified speech text
	 *
	 * @param expectedSpeechText the expected speech text
	 * @return this {@link SpeechletResponseAssert} for further assertion chaining
	 */
	public SpeechletResponseAssert hasPlainTextOutputSpeech(String expectedSpeechText) {
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) this.actual.getOutputSpeech();
		PlainTextOutputSpeechAssert.assertThat(outputSpeech).hasText(expectedSpeechText);

		return this;
	}

	/**
	 * Assert that the {@link SpeechletResponse} text matches the specified pattern
	 *
	 * @param expectedSpeechTextPattern the pattern for the expected speech text
	 * @return this {@link SpeechletResponseAssert} for further assertion chaining
	 */
	public SpeechletResponseAssert hasPlainTextOutputSpeech(Pattern expectedSpeechTextPattern) {
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) this.actual.getOutputSpeech();
		PlainTextOutputSpeechAssert.assertThat(outputSpeech).hasText(expectedSpeechTextPattern);

		return this;
	}

	/**
	 * Assert that the {@link SpeechletResponse} has the specified speech ssml
	 *
	 * @param expectedSpeechSsml the expected speech ssml
	 * @return this {@link SpeechletResponseAssert} for further assertion chaining
	 */
	public SpeechletResponseAssert hasSsmlOutputSpeech(String expectedSpeechSsml) {
		SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) this.actual.getOutputSpeech();
		SsmlOutputSpeechAssert.assertThat(outputSpeech).hasSsml(expectedSpeechSsml);

		return this;
	}

}
