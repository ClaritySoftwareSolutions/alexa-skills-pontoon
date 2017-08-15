package uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler;

import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentNameAndSlots;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;

/**
 * Unit test class for {@link StartGameIntentHandler}
 */
@RunWith(MockitoJUnitRunner.class)
public class StartGameIntentHandlerTest {

	@Mock
	private PontoonGameActions pontoonGameActions;

	@Mock
	private SessionSupport sessionSupport;

	@InjectMocks
	private StartGameIntentHandler intentHandler;

	@Test
	public void shouldHandleIntentGivenNoSlot() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("StartGameIntent");
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.handleIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).dealInitialHand(session);
		verify(sessionSupport).setAceIsHighOnSession(session, false);
	}

	@Test
	public void shouldHandleIntentGivenSlotWithAceIsLow() {
		// Given
		Map<String, Slot> slots = new HashMap<>();
		slots.put("aceValue", Slot.builder().withName("aceValue").withValue("low").build());
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentNameAndSlots(
				"StartGameIntent",
				slots);
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.handleIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).dealInitialHand(session);
		verify(sessionSupport).setAceIsHighOnSession(session, false);
	}

	@Test
	public void shouldHandleIntentGivenSlotWithAceIsHigh() {
		// Given
		Map<String, Slot> slots = new HashMap<>();
		slots.put("aceValue", Slot.builder().withName("aceValue").withValue("high").build());
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentNameAndSlots(
				"StartGameIntent",
				slots);
		Session session = requestEnvelope.getSession();

		// When
		intentHandler.handleIntent(requestEnvelope);

		// Then
		verify(pontoonGameActions).dealInitialHand(session);
		verify(sessionSupport).setAceIsHighOnSession(session, true);
	}

}
