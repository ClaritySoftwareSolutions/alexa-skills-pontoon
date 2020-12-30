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
 * Unit test class for {@link HelpIntentHandler}
 */
@ExtendWith(MockitoExtension.class)
public class HelpIntentHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@InjectMocks
	private HelpIntentHandler intentHandler;

	@Test
	public void shouldHandleIntent() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("AMAZON.HelpIntent");
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.handleIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).help(session);
	}

	@Test
	public void shouldDetermineIfHandlesGivenHelpIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.HELP_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isTrue();
	}

	@Test
	public void shouldDetermineIfHandlesGivenNonHelpIntent() {
		// Given
		PontoonIntent intent = PontoonIntent.TWIST_INTENT;

		// When
		boolean handles = intentHandler.handles(intent);

		// Then
		assertThat(handles).isFalse();
	}

}
