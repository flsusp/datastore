package br.com.datastore;

import java.util.List;

public class FieldExpression extends Expression {

	private final Field field;

	public FieldExpression(Field field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return field.name();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
	}
}
