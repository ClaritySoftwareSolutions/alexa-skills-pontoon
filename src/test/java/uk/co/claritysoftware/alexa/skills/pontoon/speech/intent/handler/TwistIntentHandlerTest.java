package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;

/**
 * Unit test class for {@link TwistIntentHandler}
 */
@ExtendWith(MockitoExtension.class)
public class TwistIntentHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@InjectMocks
	private TwistIntentHandler intentHandler;

	@Test
	public void shouldDoIntent() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("TwistIntent");
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.doIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).twist(session);
	}

	@Test
	public void shouldDetermineIfHandlesGivenTwistIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.TWIST_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isTrue();
	}

	@Test
	public void shouldDetermineIfHandlesGivenNonTwistIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.HELP_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isFalse();
	}

}
