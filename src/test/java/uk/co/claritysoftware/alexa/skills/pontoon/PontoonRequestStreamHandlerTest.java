package uk.co.claritysoftware.alexa.skills.pontoon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static uk.co.claritysoftware.alexa.skills.testsupport.assertj.SpeechletRequestStreamHandlerAssert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

/**
 * Unit test class for {@link PontoonRequestStreamHandler}
 */
public class PontoonRequestStreamHandlerTest {

	private static final String APPLICATION_IDS = "com_amazon_speech_speechlet_servlet_supportedApplicationIds";

	@Rule
	public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

	@Test
	public void shouldConstructGivenSingleApplicationId() {
		// Given
		environmentVariables.set(APPLICATION_IDS, "1234");

		Set<String> expectedApplicationIds = Stream.of("1234").collect(Collectors.toSet());

		// When
		PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

		// Then
		assertThat(handler).hasApplicationIds(expectedApplicationIds);
	}

	@Test
	public void shouldConstructGivenMultipleApplicationIds() {
		// Given
		environmentVariables.set(APPLICATION_IDS, "1234,5678");

		Set<String> expectedApplicationIds = Stream.of("1234", "5678").collect(Collectors.toSet());

		// When
		PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

		// Then
		assertThat(handler).hasApplicationIds(expectedApplicationIds);
	}

	@Test
	public void shouldConstructGivenMultipleApplicationIdsWithSpaces() {
		// Given
		environmentVariables.set(APPLICATION_IDS, "1234, 5678, ,9012");

		Set<String> expectedApplicationIds = Stream.of("1234", "5678", "9012").collect(Collectors.toSet());

		// When
		PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

		// Then
		assertThat(handler).hasApplicationIds(expectedApplicationIds);
	}

	@Test
	public void shouldFailToConstructGivenNullAPPLICATION_IDS() {
		// Given

		// When
		try {
			new PontoonRequestStreamHandler();

			fail("Was expecting an IllegalStateException");

		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
		}
	}

	@Test
	public void shouldFailToConstructGivenEmptyAPPLICATION_IDS() {
		// Given
		environmentVariables.set(APPLICATION_IDS, "");

		// When
		try {
			new PontoonRequestStreamHandler();

			fail("Was expecting an IllegalStateException");

		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
		}
	}

	@Test
	public void shouldFailToConstructGivenCollectionOfEmptyAPPLICATION_IDS() {
		// Given
		environmentVariables.set(APPLICATION_IDS, ",, , ");

		// When
		try {
			new PontoonRequestStreamHandler();

			fail("Was expecting an IllegalStateException");

		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
		}
	}

}
