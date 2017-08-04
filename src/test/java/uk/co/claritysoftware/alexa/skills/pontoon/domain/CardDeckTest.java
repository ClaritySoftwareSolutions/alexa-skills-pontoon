package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Unit test class for {@link CardDeck}
 */
public class CardDeckTest {

	private static final List<Card> UNSHUFFLED_FULL_DECK;

	private CardDeck cardDeck;

	static {
		UNSHUFFLED_FULL_DECK = new ArrayList<>();
		for (CardSuit suit : CardSuit.values()) {
			for (CardValue value : CardValue.values()) {
				UNSHUFFLED_FULL_DECK.add(new Card(value, suit));
			}
		}
	}

	@Test
	public void shouldConstruct() {
		// Given

		// When
		cardDeck = new CardDeck();

		// Then
		List<Card> cards = cardDeck.getCards();
		assertThat(cards).containsExactlyElementsOf(UNSHUFFLED_FULL_DECK);
	}

	@Test
	public void shouldConstructGivenShuffleIsTrue() {
		// Given
		boolean shuffleDeck = true;

		// When
		cardDeck = new CardDeck(shuffleDeck);

		// Then
		List<Card> cards = cardDeck.getCards();
		assertThat(cards).containsExactlyInAnyOrder(UNSHUFFLED_FULL_DECK.toArray(new Card[UNSHUFFLED_FULL_DECK.size()]));
	}

	@Test
	public void shouldShuffle() {
		// Given
		cardDeck = new CardDeck();
		List<Card> cards = cardDeck.getCards();
		List<Card> cardsBeforeShuffle = new ArrayList<>(cards);

		// When
		cardDeck.shuffle();

		// Then
		assertThat(cardsBeforeShuffle).containsExactlyElementsOf(UNSHUFFLED_FULL_DECK);
		assertThat(cards).containsExactlyInAnyOrder(UNSHUFFLED_FULL_DECK.toArray(new Card[UNSHUFFLED_FULL_DECK.size()]));
	}

	@Test
	public void shouldDeal() {
		// Given
		cardDeck = new CardDeck();

		int expectedDeckSizeAfterDealingCard = 51;

		// When
		Card dealtCard = cardDeck.deal();

		// Then
		assertThat(dealtCard).isNotNull();
		List<Card> cards = cardDeck.getCards();
		assertThat(cards).hasSize(expectedDeckSizeAfterDealingCard);
	}

	@Test
	public void shouldFailTodDealGivenEmptyDeck() {
		// Given
		cardDeck = new CardDeck();
		for (int i = 1; i <= 52; i++) {
			cardDeck.deal();
		}

		// When
		try {
			cardDeck.deal();

			fail("Was expecting an IllegalStateExceptiom");
		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("All cards have already been dealt from the deck");
		}
	}

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(CardDeck.class)
				.suppress(Warning.STRICT_INHERITANCE)
				.withNonnullFields("cards")
				.verify();
	}

	@Test
	public void shouldBeAbleToSerialize() throws Exception {
		// Given
		ObjectMapper objectMapper = new ObjectMapper();

		// When
		boolean canSerialize = objectMapper.canSerialize(Card.class);

		// Then
		assertThat(canSerialize).isTrue();
	}

	@Test
	public void shouldFailToModifyCardsList() {
		// Given
		cardDeck = new CardDeck();
		List<Card> cards = cardDeck.getCards();

		// When
		try {
			cards.remove(0);

			fail("Was expecting an UnsupportedOperationException");
		}
		// Then
		catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(UnsupportedOperationException.class);
		}
	}
}
