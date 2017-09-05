package uk.co.claritysoftware.alexa.skills.pontoon.speech;

import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletResponseAssert.assertThat;

import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Integration test class for {@link PontoonGameActions}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
public class PontoonGameActionsIT {

	@Autowired
	private PontoonGameActions pontoonGameActions;

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
	public void shouldHelpGivenGameNotYetStarted() {
		// Given
		Pattern expectedPlainTextOutputSpeech = Pattern.compile("Pontoon is a card game.*\nTo start the game");

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.help(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
	}

	@Test
	public void shouldHelpGivenGameStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("StartGameIntent")
								.build())
						.build())
				.build();
		pontoonSpeechlet.onIntent(requestEnvelope);
		Pattern expectedPlainTextOutputSpeech = Pattern.compile("Pontoon is a card game.*\nYour game is already started");

		// When
		SpeechletResponse speechletResponse = pontoonGameActions.help(session);

		// Then
		assertThat(speechletResponse)
				.isAnAskResponse()
				.hasPlainTextOutputSpeech(expectedPlainTextOutputSpeech);
	}
}
