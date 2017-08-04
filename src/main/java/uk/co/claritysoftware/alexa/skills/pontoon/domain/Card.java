package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Immutable class describing a playing card
 */
@Data
@EqualsAndHashCode
public final class Card {

	private static final int ACE_IS_HIGH_MODIFIER = 9;

	private final CardValue cardValue;

	private final CardSuit cardSuit;

	/**
	 * @return the numeric value of the card
	 */
	public int getValue(final boolean aceIsHIgh) {
		return cardIsAce() && aceIsHIgh ? cardValue.getValue() + ACE_IS_HIGH_MODIFIER : cardValue.getValue();
	}

	private boolean cardIsAce() {
		return cardValue.equals(CardValue.ACE);
	}
}
