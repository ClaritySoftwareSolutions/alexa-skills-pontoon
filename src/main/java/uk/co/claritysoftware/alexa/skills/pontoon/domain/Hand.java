package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import java.math.BigInteger;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.AbstractHand;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.cards.Card;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Specialisation of {@link AbstractHand} for a hand of cards for a game of pontoon.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public final class Hand extends AbstractHand {

	private static final int PONTOON_WIN_SCORE = 21;

	@JsonCreator
	public Hand(@JsonProperty("cards") final List<Card> cards) {
		super(cards);
	}

	/**
	 * Returns the score of the current hand.
	 *
	 * @param aceIsHigh determines whether the value of ace should be considered high or low
	 * @return the scoe of the current hand
	 */
	public int getScore(final boolean aceIsHigh) {
		return getCards().stream()
				.map(currentCard -> BigInteger.valueOf(currentCard.getValue(aceIsHigh)))
				.reduce(BigInteger.ZERO, BigInteger::add)
				.intValue();
	}

	/**
	 * Determines if the hand is 'bust' based on the value of the cards, and the specified aceIsHigh value
	 *
	 * @param aceIsHigh boolean to indicate whether aces should be consider high or not
	 * @return true if the value of the hand is 'bust'
	 */
	public boolean isBust(final boolean aceIsHigh) {
		return getScore(aceIsHigh) > PONTOON_WIN_SCORE;
	}

	/**
	 * Determines if the hand is a winning score based on the value of the cards, and the specified aceIsHigh value
	 *
	 * @param aceIsHigh boolean to indicate whether aces should be consider high or not
	 * @return true if the value of the hand is a winning hand
	 */
	public boolean isWin(final boolean aceIsHigh) {
		return getScore(aceIsHigh) == PONTOON_WIN_SCORE;
	}

	/**
	 * Determines if the hand is still in play based on the value of the cards, and the specified aceIsHigh value
	 * 'Still in play' means the hand is not bust and is not a winning hand.
	 *
	 * @param aceIsHigh boolean to indicate whether aces should be consider high or not
	 * @return true if the value of the hand means the hans is still in play
	 */
	public boolean isStillInPlay(final boolean aceIsHigh) {
		return getScore(aceIsHigh) < PONTOON_WIN_SCORE;
	}

}
