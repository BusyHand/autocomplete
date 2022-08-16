package com.autocomplete.display;

import static com.autocomplete.cache.Cache.cache;
import static com.autocomplete.props.SearchParameters.isNumbers;
import static com.autocomplete.searches.SearchResults.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResultsDisplay extends Display {

	@Override
	public void show(String massage) {

	}

	public void show() {
		List<String> arrayList = new ArrayList<>();
		Set<Integer> keySet = result.keySet();

		for (Integer key : keySet) {
			List<byte[]> list = cache.get(key);
			if (isNumbers) {
				arrayList.add(new String(list.get(0)) + new String(list.get(1)));
			} else {
				arrayList.add("\"" + new String(list.get(0)) + "\"" + new String(list.get(1)));
			}

		}
		arrayList.sort((o1, o2) -> o1.compareTo(o2));
		arrayList.forEach(System.out::println);

	}

}
