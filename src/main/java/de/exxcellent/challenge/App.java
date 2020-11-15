package de.exxcellent.challenge;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
public final class App {
	
	static final int SUCCESS = 0;
	static final int FAILURE = 1;
	
	static final String USAGE_TEXT = "Usage: --weather <path to csv>";

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {
    	
    }

	
    int run(String ...strings) {
		return App.SUCCESS;
	}
}
