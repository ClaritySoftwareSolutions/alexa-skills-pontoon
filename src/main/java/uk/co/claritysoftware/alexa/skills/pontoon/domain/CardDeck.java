package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE_HIGH;
import static uk.co.claritysoftware.alexa.skills.pontoon.domain.CardValue.ACE_LOW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;

/**
 * Class representing a deck of cards, where a initial deck consists of 52 cards (13 from each suit)
 *
 * Subclasses may introduce their own behaviour methods, but may not override {@link CardDeck#shuffle()} or {@link CardDeck#deal()}
 */
@EqualsAndHashCode
public class CardDeck {

	private static final int CARD_ON_TOP_OF_DECK = 0;

	private static final boolean DEFAULT_ACE_IS_HIGH = false;

	private static final boolean DEFAULT_SHUFFLE_ON_CREATION = false;

	private final List<Card> cards;

	/**
	 * Construct a populated deck of cards where ace is low and the deck is not shuffled
	 */
	public CardDeck() {
		this(DEFAULT_ACE_IS_HIGH, DEFAULT_SHUFFLE_ON_CREATION);
	}

	/**
	 * Construct a populated deck of cards where the value of ace is high or low as specified by the parameter,
	 * and the deck is not shuffled
	 *
	 * @param aceIsHigh boolean to determine whether ace should he high
	 */
	public CardDeck(boolean aceIsHigh) {
		this(aceIsHigh, DEFAULT_SHUFFLE_ON_CREATION);
	}

	/**
	 * Construct a populated deck of cards where the value of ace is high or low as specified by the parameter,
	 * and the deck is shuffled as specified by the parameter
	 *
	 * @param aceIsHigh         boolean to determine whether ace should he high
	 * @param shuffleOnCreation boolean to determine whether to shuffle the deck
	 */
	public CardDeck(boolean aceIsHigh, boolean shuffleOnCreation) {
		cards = new ArrayList<>();
		for (CardSuit suit : CardSuit.values()) {
			for (CardValue value : CardValue.values()) {
				if ((value != ACE_HIGH && value != ACE_LOW) || (value == ACE_HIGH && aceIsHigh) || (value == ACE_LOW && !aceIsHigh)) {
					cards.add(new Card(value, suit));
				}
			}
		}

		if (shuffleOnCreation) {
			shuffle();
		}
	}

	/**
	 * Get a list containing this instances {@link Card Cards}. The returned list is deliberately unmodifiable so that all
	 * mutation operations are via controlled methods such as {@link CardDeck#deal()}
	 *
	 * @return an unmodifiable list containing this instances {@link Card Cards}.
	 */
	public List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}

	/**
	 * Shuffles the deck of cards
	 *
	 * Subclasses may not override this.
	 */
	public final void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Deal a card from the top of the deck. The dealt card is removed from the deck, and is returned.
	 *
	 * Subclasses may not override this.
	 *
	 * @return the Card dealt from the top of the deck
	 * @throws IllegalStateException if the deck has no more cards to deal
	 */
	public final Card deal() {
		if (cards.isEmpty()) {
			throw new IllegalStateException("All cards have already been dealt from the deck");
		}

		return cards.remove(CARD_ON_TOP_OF_DECK);
	}
}
