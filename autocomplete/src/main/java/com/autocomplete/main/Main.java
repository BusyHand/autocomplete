package com.autocomplete.main;

import java.net.URISyntaxException;

import com.autocomplete.application.Application;

public class Main {

	private static final String STOP_APPLICATION = "!quit";

	public static void main(String[] args) throws URISyntaxException {
		
		Application application = Application.run(args);
		
		//While user will not write '!quit'
		while (!STOP_APPLICATION.equals(Application.input)) {
			application.input()
					   .search()
					   .cacheResults()
					   .showResult()
					   .refreshResults();
		}
	}

}
