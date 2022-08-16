package com.autocomplete.display;

public class ErrorMessageDisplay extends Display {

	@Override
	public void show(String errorMassage) {
		super.show("ERROR: " + errorMassage);
	}

}
