package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link Hand}
 */
public class HandTest {

	@Test
	public void shouldAddCard() {
		// Given
		Hand hand = new Hand(new ArrayList());
		Card card = new Card(CardValue.KING, CardSuit.CLUBS);

		List<Card> expectedCardsInHand = Collections.singletonList(card);

		// When
		hand.addCard(card);

		// Then
		assertThat(hand.getCards()).isEqualTo(expectedCardsInHand);
	}

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(Hand.class)
				.withNonnullFields("cards")
				.verify();
	}

	@Test
	public void shouldBeAbleToSerialize() throws Exception {
		// Given
		ObjectMapper objectMapper = new ObjectMapper();

		// When
		boolean canSerialize = objectMapper.canSerialize(Hand.class);

		// Then
		assertThat(canSerialize).isTrue();
	}

	@Test
	public void shouldFailToModifyCardsList() {
		// Given
		Hand hand = new Hand(Collections.singletonList(new Card(CardValue.KING, CardSuit.SPADES)));
		List<Card> cards = hand.getCards();

		// When
		try {
			cards.remove(0);

			fail("Was expecting an UnsupportedOperationException");
		}
		// Then
		catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(UnsupportedOperationException.class);
		}
	}

}
