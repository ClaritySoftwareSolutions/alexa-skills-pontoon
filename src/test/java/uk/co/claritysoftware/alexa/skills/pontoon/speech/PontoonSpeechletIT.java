package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.SpeechletResponseAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelopeWithSession;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;

import java.util.regex.Pattern;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Integration test class for {@link PontoonSpeechlet}
 */
@QuarkusTest
public class PontoonSpeechletIT {

	@Inject
	private PontoonSpeechlet pontoonSpeechlet;

	private Session session;

	@BeforeEach
	public void sessionSetup() {
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope();
		pontoonSpeechlet.onSessionStarted(requestEnvelope);
		session = requestEnvelope.getSession();
	}

	@Test
	public void shouldOnLaunch() {
		// Given
		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelopeWithSession(session);

		Pattern expectedSpeechTextPattern = Pattern
				.compile("^I have dealt you the \\w+ of \\w+, and the \\w+ of \\w+\\.\n(Ace is (high|low)\\.\n)?Your score is \\d{1,2}\\.\nYou can twist or stick. What would you like to do\\?");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeechWithText(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeechWithText(expectedReprompt);
	}

}
