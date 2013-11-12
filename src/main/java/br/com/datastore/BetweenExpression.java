package br.com.datastore;

import java.util.List;

public class BetweenExpression extends Expression {

	private Expression expressionStart;
	private Expression expressionEnd;

	public BetweenExpression(Expression expressionStart, Expression expressionEnd) {
		this.expressionStart = expressionStart;
		this.expressionEnd = expressionEnd;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(expressionStart.toString());
		sb.append(" AND ");
		sb.append(expressionEnd);
		return sb.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		expressionStart.populateParameters(parameters);
		expressionEnd.populateParameters(parameters);
	}
}
