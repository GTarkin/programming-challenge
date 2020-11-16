package de.exxcellent.challenge;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.base.Charsets;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The entry class for your solution. This class is only aimed as starting point
 * and not intended as baseline for your software design. Read: create your own
 * classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
public final class App {

	static final int SUCCESS = 0;
	static final int FAILURE = 1;

	private static final String USAGE_TEXT = "Usage: [--help] | [--weather <path to csv>] | [--football <path to csv>]";

	private PrintStream stdout;
	private PrintStream stderr;

	static final List<String> EXPECTED_WEATHER_DATA_HEADER_COLUMNS = Arrays.asList("Day", "MxT", "MnT", "AvT", "AvDP",
			"1HrP TPcpn", "PDir", "AvSp", "Dir", "MxS", "SkyC", "MxR", "Mn", "R AvSLP");

	static final List<String> EXPECTED_FOOTBALL_DATA_HEADER = Arrays.asList("Team", "Games", "Wins", "Losses", "Draws",
			"Goals", "Goals Allowed", "Points");

	Comparator<WeatherReading> bySpread = (WeatherReading item1, WeatherReading item2) -> Float.compare(item1.spread(),
			item2.spread());

	Comparator<FootballReading> byAbsoluteSpread = (FootballReading item1, FootballReading item2) -> Integer
			.compare(item1.absoluteSpread(), item2.absoluteSpread());

	/**
	 * This is the main entry method of your program.
	 * 
	 * @param args The CLI arguments passed
	 */
	public static void main(String... args) {
		Injector injector = Guice.createInjector(new AppModule());
		App app = injector.getInstance(App.class);
		int exitCode = app.run(args);
		System.exit(exitCode);
	}

	@Inject
	public App(@Named("stdout") PrintStream stdout, @Named("stderr") PrintStream stderr) {
		this.stdout = Objects.requireNonNull(stdout);
		this.stderr = Objects.requireNonNull(stderr);
	}

	int run(String... args) {
		if (args.length == 0) {
			return printUsageAndExitWithFailure();
		} else {
			String option = args[0];
			if (args.length == 1) {
				if (option.equals("--help")) {
					return printHelp();
				} else {
					return printUsageAndExitWithFailure();
				}
			} else if (args.length == 2) {
				if (option.equals("--weather")) {
					return processWeatherCSV(Paths.get(args[1]));
				} else if (option.equals("--football")) {
					return processFootballCSV(Paths.get(args[1]));
				} else {
					return printUsageAndExitWithFailure();
				}
			} else {
				return printUsageAndExitWithFailure();
			}
		}
	}

	int processFootballCSV(Path path) {
		if (!Files.exists(path)) {
			this.stderr.println(String.format("File not found: %s", path));
			return App.FAILURE;
		}

		try {
			CSVParser csvParser = CSVParser.parse(path, Charsets.UTF_8, CSVFormat.DEFAULT.withHeader());
			List<String> headerNames = csvParser.getHeaderNames();
			if (!headerNames.containsAll(App.EXPECTED_FOOTBALL_DATA_HEADER)) {
				stderr.println(String.format("Wrong header found: '%s' - Expected: '%s'", String.join(",", headerNames),
						String.join(",", App.EXPECTED_FOOTBALL_DATA_HEADER)));
				return App.FAILURE;
			}
			List<CSVRecord> records = csvParser.getRecords();
			ArrayList<FootballReading> footballReadings = new ArrayList<>();
			int i = 1;
			for (CSVRecord record : records) {
				try {
					FootballReading footballReading = FootballReading.fromStrings(record.get("Team"),
							record.get("Goals"), record.get("Goals Allowed"));
					footballReadings.add(footballReading);
				} catch (NumberFormatException ex) {
					this.stderr.println(String.format("Warn: Invalid data in line %d'", i));
				}
				++i;
			}
			MinimaFinder<FootballReading> minimumSpreadFinder = new MinimaFinder<>();
			List<FootballReading> minimas = minimumSpreadFinder.findMinima(byAbsoluteSpread, footballReadings);
			minimas.forEach(item -> stdout.println(item.team()));
		} catch (IOException e) {
			e.printStackTrace(stderr);
			return App.FAILURE;
		}

		return App.SUCCESS;
	}

	int processWeatherCSV(Path path) {
		if (!Files.exists(path)) {
			this.stderr.println(String.format("File not found: %s", path));
			return App.FAILURE;
		}

		try {
			CSVParser csvParser = CSVParser.parse(path, Charsets.UTF_8, CSVFormat.DEFAULT.withHeader());
			List<String> headerNames = csvParser.getHeaderNames();
			if (!headerNames.containsAll(App.EXPECTED_WEATHER_DATA_HEADER_COLUMNS)) {
				stderr.println(String.format("Wrong header found: '%s' - Expected: '%s'", String.join(",", headerNames),
						String.join(",", App.EXPECTED_WEATHER_DATA_HEADER_COLUMNS)));
				return App.FAILURE;
			}
			List<CSVRecord> records = csvParser.getRecords();
			ArrayList<WeatherReading> weatherReadings = new ArrayList<>();
			int i = 1;
			for (CSVRecord record : records) {
				try {
					WeatherReading weatherReading = WeatherReading.fromStrings(record.get("Day"), record.get("MxT"),
							record.get("MnT"));
					weatherReadings.add(weatherReading);
				} catch (NumberFormatException ex) {
					this.stderr.println(String.format("Warn: Invalid data in line %d'", i));
				}
				++i;
			}
			MinimaFinder<WeatherReading> minimumSpreadFinder = new MinimaFinder<>();
			List<WeatherReading> minimas = minimumSpreadFinder.findMinima(bySpread, weatherReadings);
			minimas.forEach(item -> stdout.println(item.name()));
		} catch (IOException e) {
			e.printStackTrace(stderr);
			return App.FAILURE;
		}

		return App.SUCCESS;
	}

	int printHelp() {
		stdout.println(App.USAGE_TEXT);
		return App.SUCCESS;
	}

	int printUsageAndExitWithFailure() {
		stderr.println(App.USAGE_TEXT);
		return App.FAILURE;
	}
}
