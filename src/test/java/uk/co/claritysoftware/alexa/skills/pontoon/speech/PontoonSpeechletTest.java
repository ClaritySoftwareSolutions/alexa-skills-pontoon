package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelopeWithIntentName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.PontoonIntent;
import uk.co.claritysoftware.alexa.skills.speech.intent.IntentHandler;

/**
 * Unit test class for {@link PontoonSpeechlet}
 */
@ExtendWith(MockitoExtension.class)
public class PontoonSpeechletTest {

	@Mock
	private SessionSupport sessionSupport;

	@Mock
	private HandlerFactory handlerFactory;

	@InjectMocks
	private PontoonSpeechlet speechlet;

	@Test
	public void shouldOnSessionStarted() {
		// Given
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope();
		Session session = requestEnvelope.getSession();

		// When
		speechlet.onSessionStarted(requestEnvelope);

		// Then
		verify(sessionSupport).setCardDeckOnSession(eq(session), any(CardDeck.class));
	}

	@Test
	public void shouldOnLaunch() throws Exception {
		// Given
		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelope();

		LaunchHandler launchHandler = mock(LaunchHandler.class);
		given(handlerFactory.getLaunchHandler()).willReturn(launchHandler);

		SpeechletResponse expectedSpeechletResponse = mock(SpeechletResponse.class);
		given(launchHandler.handle(requestEnvelope)).willReturn(expectedSpeechletResponse);

		// When
		SpeechletResponse speechletResponse = speechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedSpeechletResponse);
	}

	@Test
	public void shouldOnIntentGivenValidIntentName() {
		// Given
		PontoonIntent intent = PontoonIntent.HELP_INTENT;
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("AMAZON.HelpIntent");

		IntentHandler intentHandler = mock(IntentHandler.class);
		given(handlerFactory.getIntentHandlerForIntent(intent)).willReturn(intentHandler);

		SpeechletResponse expectedSpeechletResponse = mock(SpeechletResponse.class);
		given(intentHandler.handleIntent(requestEnvelope)).willReturn(expectedSpeechletResponse);

		// When
		SpeechletResponse speechletResponse = speechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedSpeechletResponse);
	}

	@Test
	public void shouldFailToOnIntentGivenUnknownIntentName() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelopeWithIntentName("some-unknown-intent");

		// When
		Throwable e = catchThrowable(() -> speechlet.onIntent(requestEnvelope));

		// Then
		assertThat(e)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("No intent with name some-unknown-intent");
	}
}
