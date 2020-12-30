package uk.co.claritysoftware.alexa.skills.pontoon;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static uk.co.claritysoftware.alexa.skills.kit.test.assertj.SpeechletRequestStreamHandlerAssert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@link PontoonRequestStreamHandler}
 */
public class PontoonRequestStreamHandlerTest {

	private static final String APPLICATION_IDS = "com_amazon_speech_speechlet_servlet_supportedApplicationIds";

	@Test
	public void shouldConstructGivenSingleApplicationId() throws Exception {
		withEnvironmentVariable(APPLICATION_IDS, "1234").execute(() -> {
			// Given

			Set<String> expectedApplicationIds = Stream.of("1234").collect(Collectors.toSet());

			// When
			PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

			// Then
			assertThat(handler).hasApplicationIds(expectedApplicationIds);
		});
	}

	@Test
	public void shouldConstructGivenMultipleApplicationIds() throws Exception {
		withEnvironmentVariable(APPLICATION_IDS, "1234,5678").execute(() -> {
			// Given
			Set<String> expectedApplicationIds = Stream.of("1234", "5678").collect(Collectors.toSet());

			// When
			PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

			// Then
			assertThat(handler).hasApplicationIds(expectedApplicationIds);
		});
	}

	@Test
	public void shouldConstructGivenMultipleApplicationIdsWithSpaces() throws Exception {
		withEnvironmentVariable(APPLICATION_IDS, "1234, 5678, ,9012").execute(() -> {
			// Given
			Set<String> expectedApplicationIds = Stream.of("1234", "5678", "9012").collect(Collectors.toSet());

			// When
			PontoonRequestStreamHandler handler = new PontoonRequestStreamHandler();

			// Then
			assertThat(handler).hasApplicationIds(expectedApplicationIds);
		});
	}

	@Test
	public void shouldFailToConstructGivenNullAPPLICATION_IDS() {
		// Given

		// When
		Throwable e = catchThrowable(() -> new PontoonRequestStreamHandler());

		// Then
		assertThat(e)
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
	}

	@Test
	public void shouldFailToConstructGivenEmptyAPPLICATION_IDS() throws Exception {
		withEnvironmentVariable(APPLICATION_IDS, "").execute(() -> {
			// Given

			// When
			Throwable e = catchThrowable(() -> new PontoonRequestStreamHandler());

			// Then
			assertThat(e)
					.isInstanceOf(IllegalStateException.class)
					.hasMessage("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
		});
	}

	@Test
	public void shouldFailToConstructGivenCollectionOfEmptyAPPLICATION_IDS() throws Exception {
		withEnvironmentVariable(APPLICATION_IDS, ",, , ").execute(() -> {
			// Given

			// When
			Throwable e = catchThrowable(() -> new PontoonRequestStreamHandler());

			// Then
			assertThat(e)
					.isInstanceOf(IllegalStateException.class)
					.hasMessage("Cannot instantiate PontoonRequestStreamHandler with null or empty com_amazon_speech_speechlet_servlet_supportedApplicationIds system property");
		});
	}
}
