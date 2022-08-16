package com.autocomplete.searches;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchResults {

	public static Map<Integer, byte[]> result = new HashMap<>();

	public static void addResult(byte[] data, int startLine, int endLine, int currentLine) {
		result.put(currentLine, Arrays.copyOfRange(data, startLine, endLine));
	}

	public static void refreshResults() {
		result = new HashMap<>();
	}

}
