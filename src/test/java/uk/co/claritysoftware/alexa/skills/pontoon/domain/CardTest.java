package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link Card}
 */
public class CardTest {

	@Test
	public void shouldGetValue() {
		// Given
		Card card = new Card(CardValue.KING, CardSuit.CLUBS);
		int expectedValue = 10;

		// When
		int value = card.getValue();

		// Then
		assertThat(value).isEqualTo(expectedValue);
	}

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(Card.class)
				.verify();
	}
}
