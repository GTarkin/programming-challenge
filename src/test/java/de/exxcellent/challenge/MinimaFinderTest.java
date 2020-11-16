package de.exxcellent.challenge;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinimaFinderTest {

	MinimaFinder<Integer> sut;

	Integer item0 = Integer.valueOf(0);
	Integer item1 = Integer.valueOf(1);
	Integer item2 = Integer.valueOf(2);
	Integer item3 = Integer.valueOf(0);

	@BeforeEach
	void setup() {
		this.sut = new MinimaFinder<Integer>();
	}

	@Test
	void finderReturnsEmptyListOnEmptyInput() throws Exception {
		List<Integer> emptyList = Collections.emptyList();

		List<Integer> minima = sut.findMinima(Integer::compareTo, emptyList);

		assertThat(minima, empty());
	}

	@Test
	void findsSingleMinimaIfOnlyOneMinimumExists() throws Exception {

		List<Integer> minima = sut.findMinima(Integer::compareTo, Arrays.asList(item0, item1, item2));
		System.out.println(minima);
		assertThat(minima, containsInAnyOrder(item0));
	}

	@Test
	void findsMultipleMinimaIfMultipleMinimasExist() throws Exception {

		List<Integer> minima = sut.findMinima(Integer::compareTo, Arrays.asList(item0, item1, item2, item3));

		assertThat(minima, containsInAnyOrder(item0, item3));
	}
}
