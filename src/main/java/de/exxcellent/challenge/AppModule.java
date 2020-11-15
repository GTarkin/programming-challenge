package de.exxcellent.challenge;

import java.io.PrintStream;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		
		// Bind stdout and stderr
		bind(PrintStream.class).annotatedWith(Names.named("stdout")).toInstance(new PrintStream(System.out));
		bind(PrintStream.class).annotatedWith(Names.named("stderr")).toInstance(new PrintStream(System.err));
	}
}
