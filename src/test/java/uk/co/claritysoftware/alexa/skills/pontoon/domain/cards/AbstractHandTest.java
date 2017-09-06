package uk.co.claritysoftware.alexa.skills.pontoon.domain.cards;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import uk.co.claritysoftware.alexa.skills.pontoon.domain.Hand;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link AbstractHand}
 */
public class AbstractHandTest {

	@Test
	public void shouldAddCard() {
		// Given
		AbstractHand hand = new TestHand(new ArrayList());
		Card card = new Card(CardValue.KING, CardSuit.CLUBS);

		List<Card> expectedCardsInHand = Collections.singletonList(card);

		// When
		hand.addCard(card);

		// Then
		assertThat(hand.getCards()).isEqualTo(expectedCardsInHand);
	}

	@Test
	public void shouldFailToModifyCardsList() {
		// Given
		AbstractHand hand = new TestHand(Collections.singletonList(new Card(CardValue.KING, CardSuit.SPADES)));
		List<Card> cards = hand.getCards();

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

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(AbstractHand.class)
				.withNonnullFields("cards")
				.withRedefinedSubclass(Hand.class)
				.verify();
	}

	private class TestHand extends AbstractHand {

		public TestHand(List<Card> cards) {
			super(cards);
		}
	}
}
