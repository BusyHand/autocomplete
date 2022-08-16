package com.autocomplete.configuration;

import com.autocomplete.display.ErrorMessageDisplay;
import com.autocomplete.props.SearchParameters;
import com.autocomplete.validation.ValidationArgs;
import com.autocomplete.validation.ValidationInput;

public class Configuration {

	private ErrorMessageDisplay errorMessage = new ErrorMessageDisplay();

	public void configureArgs(String[] args) {
		ValidationArgs validationArgs = new ValidationArgs();
		if (validationArgs.isValid(args)) {
			SearchParameters.column = Integer.valueOf(args[0]);
			return;
		}
		errorMessage.show("Invalid argument, default argument will be used: " + SearchParameters.column);

	}

	public boolean configureInput(String input) {
		ValidationInput validationInput = new ValidationInput();
		if (validationInput.isValid(input)) {
			SearchParameters.input = input.toLowerCase().getBytes();
			SearchParameters.isNumbers = validationInput.isNumbers();
			return true;
		}
		errorMessage.show("Invalid characters must be only alphabetic or numeric ");
		return false;
	}


}
