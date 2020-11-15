package de.exxcellent.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Named;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;

/**
 * Example JUnit 5 test case.
 * 
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
class AppTest {

	App sut;

	@Mock
	@Bind
	@Named("stdout")
	PrintStream stdout;

	@Mock
	@Bind
	@Named("stderr")
	PrintStream stderr;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		Injector injector = Guice.createInjector(BoundFieldModule.of(this));
		sut = injector.getInstance(App.class);
	}

	@Test
	void noArgsShowsHelpOnStderr() throws Exception {

		int exitCode = sut.run();

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(App.USAGE_TEXT);
		Mockito.verifyNoInteractions(stdout);
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void helpOptionShowsHelpOnStdout() throws Exception {

		int exitCode = sut.run("--help");

		assertEquals(App.SUCCESS, exitCode);
		Mockito.verify(stdout).println(App.USAGE_TEXT);
		Mockito.verifyNoMoreInteractions(stdout);
		Mockito.verifyNoInteractions(stderr);
	}

	@Test
	void someUnknownArgShowsUsage() throws Exception {

		int exitCode = sut.run("I am invalid argument");

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(App.USAGE_TEXT);
	}

	@Test
	void failsIfNoFilenameGiven() throws Exception {

		int exitCode = sut.run("--weather" /* here the filename is missing */ );

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(App.USAGE_TEXT);
		Mockito.verifyNoInteractions(stdout);
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void failsIfWrongOptionsIsGiven() throws Exception {

		int exitCode = sut.run("--WRONG_OPTION!", "fileame");

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(App.USAGE_TEXT);
		Mockito.verifyNoInteractions(stdout);
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void failsWithToManyArguments() throws Exception {

		int exitCode = sut.run("--weather", "fileame1", "filename2");

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(App.USAGE_TEXT);
		Mockito.verifyNoInteractions(stdout);
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void failsWithErrorMessageIfNoCsvFileFound() throws Exception {

		int exitCode = sut.run("--weather", "NOT_EXISTING.csv");

		assertEquals(App.FAILURE, exitCode);
		Mockito.verify(stderr).println(ArgumentMatchers.startsWith("File not found: "));
		Mockito.verifyNoInteractions(stdout);
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void callOnSingleMinimumFileShowsSingleDay() throws Exception {

		int exitCode = sut.run("--weather", absolutePathTo("weather_data_with_single_min_spread.csv"));

		assertEquals(App.SUCCESS, exitCode);
		Mockito.verify(stdout).println("2");
		Mockito.verifyNoMoreInteractions(stdout);
		Mockito.verifyNoInteractions(stderr);
	}

	@Test
	void callOnMultipleMinimumFileShowsMultipleDays() throws Exception {

		int exitCode = sut.run("--weather", absolutePathTo("weather_data_with_multiple_min_spreads.csv"));

		assertEquals(App.SUCCESS, exitCode);
		Mockito.verify(stdout).println("1");
		Mockito.verify(stdout).println("3");
		Mockito.verifyNoMoreInteractions(stdout);
		Mockito.verifyNoInteractions(stderr);
	}

	@Test
	void callOnFileWithWrongHeaderFails() throws Exception {

		int exitCode = sut.run("--weather", absolutePathTo("weather_data_with_wrong_header.csv"));

		assertEquals(App.FAILURE, exitCode);
		Mockito.verifyNoMoreInteractions(stdout);
		Mockito.verify(stderr).println(
				"Wrong header found: 'Day,NOT_EXPECTED_HERE!!!,MnT,AvT,AvDP,1HrP TPcpn,PDir,AvSp,Dir,MxS,SkyC,MxR,Mn,R AvSLP' - Expected: 'Day,MxT,MnT,AvT,AvDP,1HrP TPcpn,PDir,AvSp,Dir,MxS,SkyC,MxR,Mn,R AvSLP'");
		Mockito.verifyNoMoreInteractions(stderr);
	}

	@Test
	void warnsOnInvalidValuesInTemperatureColumn() throws Exception {

		int exitCode = sut.run("--weather", absolutePathTo("weather_data_with_invalid_temperatures.csv"));

		assertEquals(App.SUCCESS, exitCode);
		Mockito.verify(stdout).println("1");
		Mockito.verify(stdout).println("3");
		Mockito.verify(stderr).println("Warn: Invalid temperature data in line 2'");
		Mockito.verifyNoMoreInteractions(stdout, stderr);
	}

	// Helper method to get test file from resources folder
	final String absolutePathTo(String filename) {
		Path resourceDirectory = Paths.get("src", "test", "resources", filename);
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		return absolutePath;
	}
}