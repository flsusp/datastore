package br.com.datastore;

import java.util.List;

public class Order extends Expression {

	private final Expression expression;
	private final Type type;

	private static enum Type {
		DESC, ASC;
	}

	public static Order desc(Field field) {
		return desc(new FieldExpression(field));
	}

	public static Order asc(Field field) {
		return asc(new FieldExpression(field));
	}

	public static Order desc(Expression expression) {
		return new Order(expression, Type.DESC);
	}

	public static Order asc(Expression expression) {
		return new Order(expression, Type.ASC);
	}

	public Order(Expression expression, Type type) {
		this.expression = expression;
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append(expression.toString());
		sql.append(" ");
		sql.append(type.name());
		return sql.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		expression.populateParameters(parameters);
	}
}
