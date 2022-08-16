package com.autocomplete.display;

import static com.autocomplete.searches.SearchResults.result;

import java.util.concurrent.TimeUnit;

import com.autocomplete.props.BenchmarkProp;

public class TimeWastedDisplay extends Display {

	@Override
	public void show(String massage) {
		System.out.println("Количество найденых строк: " + result.size() + " Time: "
				+ TimeUnit.NANOSECONDS.toMillis(BenchmarkProp.finish - BenchmarkProp.start) + " мс");
	}

	public void show() {
		show("");
	}

}
