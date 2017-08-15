package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Immutable class describing the hand current hand of cards
 */
@Value
public final class Hand {

	private final List<Card> cards;

	@JsonCreator
	public Hand(@JsonProperty("cards") final List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Get a list containing this instances {@link Card Cards}. The returned list is deliberately unmodifiable so that all
	 * mutation operations are via controlled methods
	 *
	 * @return an unmodifiable list containing this instances {@link Card Cards}.
	 */
	public List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}

	/**
	 * Adds a card to the current hand
	 *
	 * @param card the {@link Card} to add to the hand
	 */
	public void addCard(final Card card) {
		cards.add(card);
	}

	/**
	 * Returns the score of the current hand.
	 *
	 * @param aceIsHigh determines whether the value of ace should be considered high or low
	 * @return the scoe of the current hand
	 */
	public int getScore(final boolean aceIsHigh) {
		return cards.stream()
				.map(currentCard -> BigInteger.valueOf(currentCard.getValue(aceIsHigh)))
				.reduce(BigInteger.ZERO, BigInteger::add)
				.intValue();
	}
}
