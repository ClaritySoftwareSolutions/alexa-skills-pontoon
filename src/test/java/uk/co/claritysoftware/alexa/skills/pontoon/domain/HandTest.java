package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.Test;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardSuit;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.CardValue;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link Hand}
 */
public class HandTest {

	private Hand hand = new Hand(Arrays.asList(
			new Card(CardValue.KING, CardSuit.SPADES),
			new Card(CardValue.TWO, CardSuit.SPADES),
			new Card(CardValue.ACE, CardSuit.HEARTS)));

	@Test
	public void shouldGetScoreGivenAceIsLow() {
		// Given
		boolean aceIsHigh = false;
		int expectedScore = 13;

		// When
		int score = hand.getScore(aceIsHigh);

		// Then
		assertThat(score).isEqualTo(expectedScore);
	}

	@Test
	public void shouldGetScoreGivenAceIsHigh() {
		// Given
		boolean aceIsHigh = true;
		int expectedScore = 23;

		// When
		int score = hand.getScore(aceIsHigh);

		// Then
		assertThat(score).isEqualTo(expectedScore);
	}

	@Test
	public void shouldDetermineIfHandIsBustGivenAceIsLow() {
		// Given
		boolean aceIsHigh = false;
		boolean expectedBust = false;

		// When
		boolean isBust = hand.isBust(aceIsHigh);

		// Then
		assertThat(isBust).isEqualTo(expectedBust);
	}

	@Test
	public void shouldDetermineIfHandIsBustGivenAceIsHigh() {
		// Given
		boolean aceIsHigh = true;
		boolean expectedBust = true;

		// When
		boolean isBust = hand.isBust(aceIsHigh);

		// Then
		assertThat(isBust).isEqualTo(expectedBust);
	}

	@Test
	public void shouldDetermineIfHandIsStillInPlayGivenAceIsLow() {
		// Given
		boolean aceIsHigh = false;
		boolean expectedInPlay = true;

		// When
		boolean isInPlay = hand.isStillInPlay(aceIsHigh);

		// Then
		assertThat(isInPlay).isEqualTo(expectedInPlay);
	}

	@Test
	public void shouldDetermineIfHandIsStillInPlayGivenAceIsHigh() {
		// Given
		boolean aceIsHigh = true;
		boolean expectedInPlay = false;

		// When
		boolean isInPlay = hand.isStillInPlay(aceIsHigh);

		// Then
		assertThat(isInPlay).isEqualTo(expectedInPlay);
	}

	@Test
	public void shouldDetermineIfHandIsStillInPlayGivenAceIsLowAndHandWithTenAndAce() {
		// Given
		boolean aceIsHigh = false;
		Hand hand = new Hand(Arrays.asList(
				new Card(CardValue.TEN, CardSuit.SPADES),
				new Card(CardValue.ACE, CardSuit.HEARTS)));

		boolean expectedInPlay = true;

		// When
		boolean isInPlay = hand.isStillInPlay(aceIsHigh);

		// Then
		assertThat(isInPlay).isEqualTo(expectedInPlay);
	}

	@Test
	public void shouldDetermineIfHandIsStillInPlayGivenAceIsHighAndHandWithTenAndAce() {
		// Given
		boolean aceIsHigh = true;
		Hand hand = new Hand(Arrays.asList(
				new Card(CardValue.TEN, CardSuit.SPADES),
				new Card(CardValue.ACE, CardSuit.HEARTS)));

		boolean expectedInPlay = false;

		// When
		boolean isInPlay = hand.isStillInPlay(aceIsHigh);

		// Then
		assertThat(isInPlay).isEqualTo(expectedInPlay);
	}

	@Test
	public void shouldDetermineIfHandIsWinGivenAceIsLowAndHandWithTenAndAce() {
		// Given
		boolean aceIsHigh = false;
		Hand hand = new Hand(Arrays.asList(
				new Card(CardValue.TEN, CardSuit.SPADES),
				new Card(CardValue.ACE, CardSuit.HEARTS)));

		boolean expectedWin = false;

		// When
		boolean isWin = hand.isWin(aceIsHigh);

		// Then
		assertThat(isWin).isEqualTo(expectedWin);
	}

	@Test
	public void shouldDetermineIfHandIsWinGivenAceIsHighAndHandWithTenAndAce() {
		// Given
		boolean aceIsHigh = true;
		Hand hand = new Hand(Arrays.asList(
				new Card(CardValue.TEN, CardSuit.SPADES),
				new Card(CardValue.ACE, CardSuit.HEARTS)));

		boolean expectedWin = true;

		// When
		boolean isWin = hand.isWin(aceIsHigh);

		// Then
		assertThat(isWin).isEqualTo(expectedWin);
	}

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(Hand.class)
				.withNonnullFields("cards")
				.withRedefinedSuperclass()
				.verify();
	}

}
