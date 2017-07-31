package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Immutable class describing a playing card
 */
@Data
@EqualsAndHashCode
public final class Card {

	private final CardValue cardValue;

	private final CardSuit cardSuit;

	/**
	 * @return the numeric value of the card
	 */
	public int getValue() {
		return cardValue.getValue();
	}

}
