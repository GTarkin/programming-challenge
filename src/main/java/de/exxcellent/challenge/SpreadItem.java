package de.exxcellent.challenge;

public interface SpreadItem {

	String name();

	float min();

	float max();

	default float spread() {
		return max() - min();
	}
}
