package br.com.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Function extends Expression {

	private final String functionName;
	private final List<Expression> parameterExpressions;

	public Function(String functionName, List<Expression> parameters) {
		super();
		this.functionName = functionName;
		this.parameterExpressions = parameters;
	}

	@SuppressWarnings("unchecked")
	public static Function function(String functionName) {
		return new Function(functionName, Collections.EMPTY_LIST);
	}

	public static Function function(String functionName, Object... values) {
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
		return new Function(functionName, expressions);
	}

	public static Function function(String functionName, Expression... expressions) {
		return new Function(functionName, Arrays.asList(expressions));
	}

	public static Function function(String functionName, Field... fields) {
		List<Expression> expressions = new ArrayList<Expression>();
		for (Field field : fields) {
			expressions.add(new FieldExpression(field));
		}
		return new Function(functionName, expressions);
	}

	public static Function count(Field field) {
		List<Expression> expressions = new ArrayList<Expression>();
		expressions.add(new FieldExpression(field));
		return new Function("count", expressions);
	}

	public static Function count() {
		List<Expression> expressions = new ArrayList<Expression>();
		expressions.add(Alias.all());
		return new Function("count", expressions);
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append(functionName);
		sql.append("(");

		Iterator<Expression> it = parameterExpressions.iterator();
		while (it.hasNext()) {
			Expression expression = it.next();
			sql.append(expression);
			if (it.hasNext()) {
				sql.append(", ");
			}
		}

		sql.append(")");
		return sql.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		for (Expression expression : this.parameterExpressions) {
			expression.populateParameters(parameters);
		}
	}

	public static Function now() {
		return function("now");
	}
}
