package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link Card}
 */
public class CardTest {

	@Test
	public void shouldGetValueGivenNonAceAndAceIsHigh() {
		// Given
		Card card = new Card(CardValue.KING, CardSuit.CLUBS);
		boolean aceIsHigh = true;

		int expectedValue = 10;

		// When
		int value = card.getValue(aceIsHigh);

		// Then
		assertThat(value).isEqualTo(expectedValue);
	}

	@Test
	public void shouldGetValueGivenNonAceAndAceIsLow() {
		// Given
		Card card = new Card(CardValue.KING, CardSuit.CLUBS);
		boolean aceIsHigh = false;

		int expectedValue = 10;

		// When
		int value = card.getValue(aceIsHigh);

		// Then
		assertThat(value).isEqualTo(expectedValue);
	}

	@Test
	public void shouldGetValueGivenAceAndAceIsHigh() {
		// Given
		Card card = new Card(CardValue.ACE, CardSuit.CLUBS);
		boolean aceIsHigh = true;

		int expectedValue = 10;

		// When
		int value = card.getValue(aceIsHigh);

		// Then
		assertThat(value).isEqualTo(expectedValue);
	}

	@Test
	public void shouldGetValueGivenAceAndAceIsLow() {
		// Given
		Card card = new Card(CardValue.ACE, CardSuit.CLUBS);
		boolean aceIsHigh = false;

		int expectedValue = 1;

		// When
		int value = card.getValue(aceIsHigh);

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
