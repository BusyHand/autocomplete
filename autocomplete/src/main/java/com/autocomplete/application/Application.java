package com.autocomplete.application;

import java.net.URISyntaxException;
import java.util.Scanner;

import com.autocomplete.cache.Cache;
import com.autocomplete.configuration.Configuration;
import com.autocomplete.display.ResultsDisplay;
import com.autocomplete.display.TimeWastedDisplay;
import com.autocomplete.props.SearchParameters;
import com.autocomplete.searches.SearchResults;
import com.autocomplete.searches.SearchWithCache;

/*
 * Class that open functionality of application. This class does not violate the 
 * single responsibility principal because does modifying another's classes
 */
public class Application {

	private Configuration configuration = new Configuration();
	private Scanner scanner = new Scanner(System.in);
	public static String input = "start";

	/*
	 * Setup
	 */
	public static Application run(String[] args) {
		Application application = new Application();
		application.configuration.configureArgs(args);
		return application;

	}

	/*
	 * Validate and sets global variables
	 */
	public Application input() {
		String input;
		do {
			input = scanner.nextLine();
		} while (!configuration.configureInput(input));
		Application.input = new String(SearchParameters.input);
		return this;
	}

	/*
	 * Run search
	 */
	public Application search() throws URISyntaxException {
		SearchWithCache searchWithCache = new SearchWithCache();
		searchWithCache.search();
		return this;

	}

	/*
	 * Show results and time which wasted on search
	 */
	public Application showResult() {
		ResultsDisplay resultsDisplay = new ResultsDisplay();
		TimeWastedDisplay timeWastedDisplay = new TimeWastedDisplay();
		resultsDisplay.show();
		timeWastedDisplay.show();
		return this;
	}

	/*
	 * Caching result
	 */
	public Application cacheResults() {
		Cache.cacheResults();
		return this;
	}

	public Application refreshResults() {
		SearchResults.refreshResults();
		return this;
	}

}
