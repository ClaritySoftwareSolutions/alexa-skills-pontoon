package uk.co.claritysoftware.alexa.skills.pontoon.domain.cards;

import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;

/**
 * Abstract class describing a hand of cards. Additional behaviour such as scoring should be implemented in subclasses
 */
@EqualsAndHashCode
public abstract class AbstractHand {

	private final List<Card> cards;

	public AbstractHand(final List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Get a list containing this instances {@link Card Cards}. The returned list is deliberately unmodifiable so that all
	 * mutation operations are via controlled methods
	 *
	 * @return an unmodifiable list containing this instances {@link Card Cards}.
	 */
	public final List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}

	/**
	 * Adds a card to the current hand
	 *
	 * @param card the {@link Card} to add to the hand
	 */
	public final void addCard(final Card card) {
		cards.add(card);
	}
}
