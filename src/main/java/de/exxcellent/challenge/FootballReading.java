package de.exxcellent.challenge;

import java.util.Objects;

public class FootballReading {

	private String team;
	private int goals;
	private int goalsAllowed;

	public FootballReading(String team, int goals, int goalsAllowed) {
		this.team = Objects.requireNonNull(team);
		this.goals = goals;
		this.goalsAllowed = goalsAllowed;
	}

	public static FootballReading fromStrings(String team, String goals, String goalsAllowed)
			throws NumberFormatException {
		int _goals = Integer.parseInt(goals);
		int _goalsAllowed = Integer.parseInt(goalsAllowed);
		return new FootballReading(team, _goals, _goalsAllowed);
	}

	public String team() {
		return team;
	}

	public int goals() {
		return goals;
	}

	public int goalsAllowed() {
		return goalsAllowed;
	}

	public int absoluteSpread() {
		return Math.abs(this.goals - this.goalsAllowed);
	}
}
