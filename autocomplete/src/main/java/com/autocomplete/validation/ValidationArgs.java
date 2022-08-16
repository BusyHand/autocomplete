package com.autocomplete.validation;

public class ValidationArgs implements Validation<String[]> {

	@Override
	public boolean isValid(String[] args) {
		// Have one argument
		if (args.length > 1 || args.length == 0 || args[0].length() > 2)
			return false;

		// And all the symbols of the number
		String arg = args[0];
		for (int i = 0; i < arg.length(); i++) {
			if (arg.charAt(i) < '0' || arg.charAt(i) > '9')
				return false;
		}
		return true;
	}

}
