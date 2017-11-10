package org.processmining.eventstream.authors.cpn.parameters;

public enum CPN2XSEventStreamCaseIdentification {
	REPITITION("By Repetition", "trace"), CPN_VARIABLE("By CPN Variable", "trace");

	private final String description;
	private final String defaultValue;

	private CPN2XSEventStreamCaseIdentification(final String description, final String defaultValue) {
		this.description = description;
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Deprecated
	public static String CPN_VARIABLE_DEFAULT_VALUE = "trace";

	@Deprecated
	public static String DEFAULT_REPITITION_CASE_IDENTIFIER = "case";
}
