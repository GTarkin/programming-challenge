package de.exxcellent.challenge;

import java.io.PrintStream;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

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

	static final String USAGE_TEXT = "Usage: [--help] | [--weather <path to csv>]";

	private PrintStream stdout;
	private PrintStream stderr;

	/**
	 * This is the main entry method of your program.
	 * 
	 * @param args The CLI arguments passed
	 */
	public static void main(String... args) {

	}

	@Inject
	public App(@Named("stdout") PrintStream stdout, @Named("stderr") PrintStream stderr) {
		this.stdout = Objects.requireNonNull(stdout);
		this.stderr = Objects.requireNonNull(stderr);
	}

	int run(String... args) {
		if (args.length == 0) {
			return printUsageAndExitWithFailure();
		} else if (args.length == 1) {
			if (args[0].equals("--help")) {
				return printHelp();
			} else {
				return printUsageAndExitWithFailure();
			}
		} else if (args.length == 2) {
			if (args[0].equals("--weather")) {
				// Logic goes here
				return App.SUCCESS;
			} else {
				return printUsageAndExitWithFailure();
			}
		} else {
			return printUsageAndExitWithFailure();
		}
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
