package com.autocomplete.validation;

public class ValidationInput implements Validation<String> {

	private boolean isNumbers = false;

	@Override
	public boolean isValid(String input) {
		// Not empty
		if (input.isBlank() || input.isEmpty())
			return false;

		// Don't have incorrect symbols
		for (int i = 0; i < input.length(); i++) {

			if ((input.charAt(i) >= 0 && input.charAt(i) <= 31) || input.charAt(i) == 34 || input.charAt(i) == 44
					|| input.charAt(i) == 127)
				return false;
		}

		// Have only alphabetic or numeric characters
		if ("\\N".equals(input)) {
			isNumbers = true;
			return true;
		}
		boolean numeric = false;
		boolean alphabetic = false;
		for (int i = 0; i < input.length(); i++) {

			if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
				numeric = true;
			}
			if ((input.charAt(i) >= 'A' && input.charAt(i) <= 'Z')
					|| (input.charAt(i) >= 'a' && input.charAt(i) <= 'z')) {
				alphabetic = true;
			}
		}
		if ((numeric && alphabetic) || (!numeric && !alphabetic)) {
			return false;
		}

		// Set what input is
		if (numeric)
			isNumbers = true;
		else
			isNumbers = false;
		return true;
	}

	public boolean isNumbers() {
		return isNumbers;
	}

}
