package com.autocomplete.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.autocomplete.props.SearchParameters;

import java.util.Map.Entry;
import static com.autocomplete.props.Indicators.*;
import static com.autocomplete.props.SearchParameters.*;
import static com.autocomplete.searches.SearchResults.*;

public class Cache {

	public static Map<Integer, List<byte[]>> cache = new HashMap<>();

	public static void addFromCache(int current, byte[] b) {
		result.put(current, b);
	}

	public static void cacheResults() {
		Set<Entry<Integer, byte[]>> entrySet = result.entrySet();
		entrySet.forEach(map -> {
			Integer key = map.getKey();
			if (!cache.containsKey(key)) {

				byte[] line = map.getValue();
				int countCOLUMN = 1;
				int endColumn = 0;
				int startColumn = 0;
				for (; startColumn < line.length; startColumn++) {
					if (line[startColumn] == DELIMITR.index) {
						countCOLUMN++;
					}
					// save zone
					if (countCOLUMN == SearchParameters.column) {

						if (!isNumbers) {
							// for symbols
							startColumn += 2;
							endColumn = startColumn;
							while (line[endColumn] != KOVICHKA.index) {
								endColumn++;
							}
						} else {
							// for numbers
							if (column == 1) {
								endColumn = startColumn + 1;
							} else {
								endColumn = startColumn + 2;
								startColumn++;
							}
							while (line[endColumn] != DELIMITR.index) {
								endColumn++;
							}
						}
						break;
					}
				}
				byte[] columnArray = Arrays.copyOfRange(line, startColumn, endColumn);
				cache.put(key, List.of(columnArray, line));

			}
		});

	}

}
