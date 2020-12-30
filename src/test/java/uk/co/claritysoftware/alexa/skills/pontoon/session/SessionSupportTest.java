package uk.co.claritysoftware.alexa.skills.pontoon.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import com.amazon.speech.speechlet.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardDeck;

/**
 * Unit test class for {@link SessionSupport}
 */
public class SessionSupportTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private SessionSupport sessionSupport = new SessionSupport();

	@Test
	public void shouldSetCardDeckOnSession() throws Exception {
		// Given
		Session session = mock(Session.class);
		CardDeck cardDeck = new CardDeck();
		String serializedCardDeck = OBJECT_MAPPER.writeValueAsString(cardDeck);

		// When
		sessionSupport.setCardDeckOnSession(session, cardDeck);

		// THen
		verify(session).setAttribute("cardDeck", serializedCardDeck);
	}

	@Test
	public void shouldGetCardDeckFromSession() throws Exception {
		// Given
		Session session = mock(Session.class);
		CardDeck cardDeck = new CardDeck();
		String serializedCardDeck = OBJECT_MAPPER.writeValueAsString(cardDeck);

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);

		// When
		CardDeck cardDeckFromSession = sessionSupport.getCardDeckFromSession(session);

		// Then
		assertThat(cardDeckFromSession).isEqualTo(cardDeck);
	}

	@Test
	public void shouldNotGetCardDeckFromSessionGivenInvalidJson() throws Exception {
		// Given
		Session session = mock(Session.class);
		String serializedCardDeck = "{notValid}";

		given(session.getAttribute("cardDeck")).willReturn(serializedCardDeck);

		// When
		CardDeck cardDeckFromSession = sessionSupport.getCardDeckFromSession(session);

		// Then
		assertThat(cardDeckFromSession).isNull();
	}

	@Test
	public void shouldSetHandOnSession() throws Exception {
		// Given
		Session session = mock(Session.class);
		Hand hand = new Hand(Collections.emptyList());
		String serializedHand = OBJECT_MAPPER.writeValueAsString(hand);

		// When
		sessionSupport.setHandOnSession(session, hand);

		// THen
		verify(session).setAttribute("hand", serializedHand);
	}

	@Test
	public void shouldGetHandFromSession() throws Exception {
		// Given
		Session session = mock(Session.class);
		Hand hand = new Hand(Collections.emptyList());
		String serializedHand = OBJECT_MAPPER.writeValueAsString(hand);

		given(session.getAttribute("hand")).willReturn(serializedHand);

		// When
		Hand handFromSession = sessionSupport.getHandFromSession(session);

		// Then
		assertThat(handFromSession).isEqualTo(hand);
	}

	@Test
	public void shouldNotGetHandFromSessionGivenInvalidJson() throws Exception {
		// Given
		Session session = mock(Session.class);
		String serializedHand = "{invalidJson}";

		given(session.getAttribute("hand")).willReturn(serializedHand);

		// When
		Hand handFromSession = sessionSupport.getHandFromSession(session);

		// Then
		assertThat(handFromSession).isNull();
	}

	@Test
	public void shouldSetAceIsHighOnSession() {
		// Given
		Session session = mock(Session.class);
		boolean aceIsHigh = true;

		// When
		sessionSupport.setAceIsHighOnSession(session, aceIsHigh);

		// THen
		verify(session).setAttribute("aceIsHigh", aceIsHigh);
	}

	@Test
	public void shouldGetAceIsHighFromSession() {
		// Given
		Session session = mock(Session.class);
		boolean aceIsHigh = true;

		given(session.getAttribute("aceIsHigh")).willReturn(aceIsHigh);

		// When
		boolean aceIsHighFromSession = sessionSupport.getAceIsHighFromSession(session);

		// Then
		assertThat(aceIsHighFromSession).isEqualTo(aceIsHigh);
	}

}
