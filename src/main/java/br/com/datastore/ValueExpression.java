package br.com.datastore;

import java.util.List;

public class ValueExpression extends Expression {

	public static final Expression Null = new ValueExpression(null);
	private final Object value;

	public ValueExpression(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "?";
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		parameters.add(value);
	}
}
