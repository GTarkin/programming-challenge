package de.exxcellent.challenge;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumSpreadFinder<T extends SpreadItem> {

	List<T> findMinima(List<T> items) {
		if (items.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Sort by spread, min first
		List<T> sorted = items.stream().sorted((item1, item2) -> Float.compare(item1.spread(), item2.spread()))
				.collect(Collectors.toList());

		// Get all minimum items
		SpreadItem minimum = sorted.get(0);
		List<T> minimas = sorted.stream().filter(item -> item.spread() <= minimum.spread()).collect(Collectors.toList());
		return minimas;
	}
}
