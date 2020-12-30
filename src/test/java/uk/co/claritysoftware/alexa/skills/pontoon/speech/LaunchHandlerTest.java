package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelope;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;

/**
 * Unit test class for {@link LaunchHandler}
 */
@ExtendWith(MockitoExtension.class)
public class LaunchHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@Mock
	private SessionSupport sessionSupport;

	@InjectMocks
	private LaunchHandler launchHandler;

	@Test
	public void shouldHandle() {
		// Given
		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();

		Session session = requestEnvelope.getSession();

		// When
		launchHandler.handle(requestEnvelope);

		// Then
		verify(sessionSupport).setAceIsHighOnSession(session, false);
		verify(pontoonGameActions).dealInitialHand(session);
	}
}
