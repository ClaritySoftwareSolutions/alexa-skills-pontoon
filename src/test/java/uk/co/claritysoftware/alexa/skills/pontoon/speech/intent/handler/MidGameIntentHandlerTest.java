package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static com.amazon.speech.speechlet.SpeechletResponse.newTellResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import uk.co.claritysoftware.alexa.skills.pontoon.exception.GameNotStartedException;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.speech.intent.AlexaIntent;

/**
 * Unit test class for {@link MidGameIntentHandler}
 */
public class MidGameIntentHandlerTest {

	private PontoonGameActions pontoonGameActions;

	@Test
	public void shouldHandleIntentGivenGameAlreadyStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("SomeIntent");
		Session session = requestEnvelope.getSession();

		pontoonGameActions = mock(PontoonGameActions.class);
		given(pontoonGameActions.isGameAlreadyStarted(session)).willReturn(true);

		MidGameIntentHandler intentHandler = new TestIntentHandler(pontoonGameActions);

		String expectedPlainTextOutputSpeech = "SpeechletResponse for TestIntentHandler";

		// When
		SpeechletResponse speechletResponse = intentHandler.handleIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isATellResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
	}

	@Test
	public void shouldFailToHandleIntentGivenGameNotStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("SomeIntent");
		Session session = requestEnvelope.getSession();

		pontoonGameActions = mock(PontoonGameActions.class);
		given(pontoonGameActions.isGameAlreadyStarted(session)).willReturn(false);

		MidGameIntentHandler intentHandler = new TestIntentHandler(pontoonGameActions);

		// When
		try {
			intentHandler.handleIntent(requestEnvelope);

			fail("Was expecting a GameNotStartedException");
		}
		// Then
		catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(GameNotStartedException.class);
		}
	}

	/**
	 * Test implementation of {@link MidGameIntentHandler} so that we can test methods of {@link MidGameIntentHandler}
	 */
	private static class TestIntentHandler extends MidGameIntentHandler {

		public TestIntentHandler(final PontoonGameActions pontoonGameActions) {
			super(pontoonGameActions);
		}

		@Override
		public boolean handles(AlexaIntent alexaIntent) {
			return false;
		}

		@Override
		SpeechletResponse doIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("SpeechletResponse for TestIntentHandler");
			return newTellResponse(outputSpeech);
		}
	}
}
