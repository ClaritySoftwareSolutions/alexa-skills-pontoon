package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelopeWithSession;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.RepromptAssert.assertThat;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Integration test class for {@link PontoonSpeechlet}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
public class PontoonSpeechletIT {

	@Autowired
	private PontoonSpeechlet pontoonSpeechlet;

	private Session session;

	@Before
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
				.compile("^I have dealt you the \\w+ of \\w+, and the \\w+ of \\w+\\. Your score is \\d{1,2}\\. (Ace is (high|low)\\. )?What would you like to do\\?$");
		String expectedReprompt = "What would you like to do?";

		// When
		SpeechletResponse speechletResponse = pontoonSpeechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedSpeechTextPattern);
		assertThat(speechletResponse.getReprompt())
				.hasPlainTextOutputSpeech(expectedReprompt);
	}
}
