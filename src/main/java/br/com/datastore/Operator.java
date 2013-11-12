package br.com.datastore;

public enum Operator {

	NotEquals("!=", false), Equals("=", false), And("AND", true), Or("OR", true), IsNull(null, false),
	GreaterThanOrEquals(">=", false), Between("BETWEEN", false), In("IN", false), LessThanOrEquals("<=", false), Like(
			"LIKE", false), LessThan("<", false), IsNotNull(null, false);

	private String stringRepresentation;
	private boolean useParenthesis;

	private Operator(String stringRepresentation, boolean useParenthesis) {
		this.stringRepresentation = stringRepresentation;
		this.useParenthesis = useParenthesis;
	}

	public String toString() {
		return stringRepresentation;
	}

	public boolean useParenthesis() {
		return useParenthesis;
	}
}
