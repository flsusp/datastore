package br.com.datastore;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

public class ListValuesExpression extends Expression {

	private final Object[] values;

	public ListValuesExpression(Object[] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append("(");

		Iterator<Object> iterator = Iterators.forArray(values);
		while (iterator.hasNext()) {
			iterator.next();
			sql.append("?");
			if (iterator.hasNext()) {
				sql.append(", ");
			}
		}

		sql.append(")");
		return sql.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		for (Object value : values) {
			parameters.add(value);
		}
	}
}
