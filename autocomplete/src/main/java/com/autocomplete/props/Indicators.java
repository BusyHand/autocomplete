package com.autocomplete.props;

public enum Indicators {
	DELIMITR((byte) 44), KOVICHKA((byte) 34), NEXT_LINE((byte) 10);

	public final byte index;

	private Indicators(byte index) {
		this.index = index;
	}
}
