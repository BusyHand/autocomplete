package com.autocomplete.display;

import java.time.LocalTime;

public class Display {

	public void show(String massage) {
		System.out.println(massage + " (" + LocalTime.now() + ")");
	}

}
