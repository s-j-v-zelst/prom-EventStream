package org.processmining.eventstream.authors.staticeventstream;

public enum StaticEventStreamFileFormat {
	EVST("evst");

	private final String format;

	private StaticEventStreamFileFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return format;
	}

}
