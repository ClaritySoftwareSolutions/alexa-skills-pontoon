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
 * Unit test class for {@link StickIntentHandler}
 */
@RunWith(MockitoJUnitRunner.class)
public class StickIntentHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@InjectMocks
	private StickIntentHandler intentHandler;

	@Test
	public void shouldDoIntent() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("StickIntent");
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.doIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).stick(session);
	}

	@Test
	public void shouldDetermineIfHandlesGivenStickIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.STICK_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isTrue();
	}

	@Test
	public void shouldDetermineIfHandlesGivenNonStickIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.HELP_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isFalse();
	}

}
