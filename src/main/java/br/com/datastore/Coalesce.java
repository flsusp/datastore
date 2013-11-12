package br.com.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Coalesce extends Expression {

	private final List<Expression> expressions;
	private final String alias;

	public Coalesce(List<Expression> expressions) {
		this.expressions = expressions;
		this.alias = null;
	}

	public Coalesce(String alias, List<Expression> expressions) {
		this.alias = alias;
		this.expressions = expressions;
	}

	public static Coalesce coalesce(Object... values) {
		List<Expression> expressions = new ArrayList<Expression>();
		for (Object value : values) {
			if (Field.class.isInstance(value)) {
				expressions.add(new FieldExpression((Field) value));
			} else if (Expression.class.isInstance(value)) {
				expressions.add((Expression) value);
			} else {
				expressions.add(new ValueExpression(value));
			}
		}
		return new Coalesce(expressions);
	}

	public static Coalesce coalesce(Expression... expressions) {
		return new Coalesce(Arrays.asList(expressions));
	}

	public static Coalesce coalesce(String alias, Expression... expressions) {
		return new Coalesce(alias, Arrays.asList(expressions));
	}

	public static Coalesce coalesce(Field... fields) {
		List<Expression> expressions = new ArrayList<Expression>();
		for (Field field : fields) {
			expressions.add(new FieldExpression(field));
		}
		return new Coalesce(expressions);
	}

	public static Coalesce coalesce(String alias, Field... fields) {
		List<Expression> expressions = new ArrayList<Expression>();
		for (Field field : fields) {
			expressions.add(new FieldExpression(field));
		}
		return new Coalesce(alias, expressions);
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append("COALESCE(");

		Iterator<Expression> it = expressions.iterator();
		while (it.hasNext()) {
			Expression expression = it.next();
			sql.append(expression);
			if (it.hasNext()) {
				sql.append(", ");
			}
		}

		sql.append(")");

		if (alias != null) {
			sql.append(" AS ");
			sql.append(alias);
		}

		return sql.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		for (Expression expression : expressions) {
			expression.populateParameters(parameters);
		}
	}
}
