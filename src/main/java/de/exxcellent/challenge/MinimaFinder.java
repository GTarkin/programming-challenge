package de.exxcellent.challenge;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MinimaFinder<T> {

	List<T> findMinima(Comparator<T> comparator, List<T> items) {
		Objects.requireNonNull(items);
		Objects.requireNonNull(comparator);
		if (items.isEmpty()) {
			return Collections.emptyList();
		}

		// Sort by comparator, min first
		List<T> sorted = items.stream().sorted(comparator).collect(Collectors.toList());

		// Get all minimum items by retaining all items that are greater or equal the minimum
		T minimum = sorted.get(0);
		List<T> minimas = sorted.stream().filter(item -> comparator.compare(minimum, item) >= 0)
				.collect(Collectors.toList());
		return minimas;
	}
}
