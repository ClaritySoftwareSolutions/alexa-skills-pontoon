package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Immutable class describing the hand current hand of cards
 */
@Data
@EqualsAndHashCode
public final class Hand {

	private final int score;

	private final List<Card> cards;

}
