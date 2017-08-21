package uk.co.claritysoftware.alexa.skills.pontoon.domain.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Immutable class describing a playing card
 */
@Value
public final class Card {

	private static final int ACE_IS_HIGH_MODIFIER = 10;

	private final CardValue cardValue;

	private final CardSuit cardSuit;

	@JsonCreator
	public Card(@JsonProperty("cardValue") final CardValue cardValue, @JsonProperty("cardSuit") final CardSuit cardSuit) {
		this.cardValue = cardValue;
		this.cardSuit = cardSuit;
	}

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
