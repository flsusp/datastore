package br.com.datastore;

import java.util.List;

public class AliasExpression extends Expression implements Field {

	private final Expression expression;
	private final String alias;

	public AliasExpression(Expression expression, String alias) {
		this.expression = expression;
		this.alias = alias;
	}

	@Override
	public String name() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(expression.toString());
		s.append(") AS ");
		s.append(alias);
		return s.toString();
	}

	@Override
	public String toString() {
		return name();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		this.expression.populateParameters(parameters);
	}
}
