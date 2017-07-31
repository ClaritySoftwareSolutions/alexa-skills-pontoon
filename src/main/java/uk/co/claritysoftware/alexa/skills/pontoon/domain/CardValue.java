package uk.co.claritysoftware.alexa.skills.pontoon.domain;

/**
 * Enum of the values of cards
 */
public enum CardValue {

	ACE_LOW("Ace", 1),
	TWO("Two", 2),
	THREE("Three", 3),
	FOUR("Four", 4),
	FIVE("Five", 5),
	SIX("Six", 6),
	SEVEN("Seven", 7),
	EIGHT("Eight", 8),
	NINE("Nine", 9),
	TEN("Ten", 10),
	JACK("Jack", 10),
	QUEEN("Queen", 10),
	KING("King", 10),
	ACE_HIGH("Aee", 10);

	private final String name;

	private final int value;

	CardValue(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
