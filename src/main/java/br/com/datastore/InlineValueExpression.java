package br.com.datastore;

import java.util.List;

public class InlineValueExpression extends Expression {

	private final String value;

	public InlineValueExpression(String value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
	}
}
