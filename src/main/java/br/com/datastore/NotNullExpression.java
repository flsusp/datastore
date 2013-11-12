package br.com.datastore;

import java.util.List;

public class NotNullExpression extends Expression {

	@Override
	public String toString() {
		return "IS NOT NULL";
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
	}
}
