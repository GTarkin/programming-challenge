package de.exxcellent.challenge;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinimumSpreadFinderTest {

	static class TestSpreadItem implements SpreadItem {

		String name;
		float min, max;

		public TestSpreadItem(String name, float min, float max) {
			this.name = name;
			this.min = min;
			this.max = max;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public float min() {
			return min;
		}

		@Override
		public float max() {
			return max;
		}

		static TestSpreadItem create(String name, float min, float max) {
			return new TestSpreadItem(name, min, max);
		}
	}

	MinimumSpreadFinder sut;

	TestSpreadItem spread0 = TestSpreadItem.create("A", 0, 0);
	TestSpreadItem spread1 = TestSpreadItem.create("B", 0, 1);
	TestSpreadItem spread2 = TestSpreadItem.create("C", 0, 2);
	TestSpreadItem spread3 = TestSpreadItem.create("d", 0, 0);

	@BeforeEach
	void setup() {
		this.sut = new MinimumSpreadFinder();
	}

	@Test
	void finderReturnsEmptyListOnEmptyInput() throws Exception {
		List<SpreadItem> emptyList = Collections.emptyList();

		List<SpreadItem> minima = sut.findMinima(emptyList);

		assertThat(minima, empty());
	}

	@Test
	void findsSingleMinimaIfOnlyOneMinimumExists() throws Exception {

		List<SpreadItem> minima = sut.findMinima(Arrays.asList(spread0, spread1, spread2));

		assertThat(minima, containsInAnyOrder(spread0));
	}

	@Test
	void findsMultipleMinimaIfMultipleMinimasExist() throws Exception {

		List<SpreadItem> minima = sut.findMinima(Arrays.asList(spread0, spread1, spread2, spread3));

		assertThat(minima, containsInAnyOrder(spread0, spread3));
	}
}
