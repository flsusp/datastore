package br.com.datastore;

import java.util.List;

public class NullExpression extends Expression {

	@Override
	public String toString() {
		return "IS NULL";
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
	}
}
