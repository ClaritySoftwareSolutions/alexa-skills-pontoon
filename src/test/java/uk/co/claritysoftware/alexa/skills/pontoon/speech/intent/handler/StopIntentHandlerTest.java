package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;

/**
 * Unit test class for {@link StopIntentHandler}
 */
@RunWith(MockitoJUnitRunner.class)
public class StopIntentHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@InjectMocks
	private StopIntentHandler intentHandler;

	@Test
	public void shouldHandleIntent() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("AMAZON.StopIntent");
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.handleIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).stop(session);
	}

	@Test
	public void shouldDetermineIfHandlesGivenStopIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.STOP_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isTrue();
	}

	@Test
	public void shouldDetermineIfHandlesGivenNonStopIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.HELP_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isFalse();
	}

}
