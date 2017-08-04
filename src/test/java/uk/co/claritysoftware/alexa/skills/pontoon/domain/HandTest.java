package uk.co.claritysoftware.alexa.skills.pontoon.domain;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test class for {@link Hand}
 */
public class HandTest {

	@Test
	public void shouldHonourEqualsHashcodeContract() {
		EqualsVerifier
				.forClass(Hand.class)
				.verify();
	}

}
