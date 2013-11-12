package br.com.datastore;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public abstract class Condition extends Expression {

	protected final Query query;

	public Condition(Query query) {
		super();
		this.query = query;
	}

	public abstract String toString();

	public abstract Query equalsTo(Expression expression);

	public abstract Query equalsTo(Field field);

	public abstract Query equalsTo(Object value);

	public abstract Query notEqualsTo(Expression expression);

	public abstract Query notEqualsTo(Field field);

	public abstract Query notEqualsTo(Object value);

	public abstract Query startsWith(String value);

	public abstract Query isNotBeforeNowMinus(long amount, TimeUnit unit);

	public abstract Condition and(Field field);

	public abstract Condition or(Field field);

	public abstract Condition or(Object value);

	public abstract Condition or(Expression expression);

	public abstract Query isNull();

	public abstract Query isNotNull();

	public abstract Query greaterThanOrEqualsTo(Expression expression);

	public abstract Query greaterThanOrEqualsTo(Field field);

	public abstract Query greaterThanOrEqualsTo(Object value);

	public abstract Query lessThanOrEqualsTo(Expression expression);

	public abstract Query lessThanOrEqualsTo(Field field);

	public abstract Query lessThanOrEqualsTo(Object value);

	public abstract Query lessThan(Expression expression);

	public abstract Query lessThan(Field field);

	public abstract Query lessThan(Object value);

	public abstract Condition and(Object value);

	public abstract Condition and(Expression expression);

	public abstract Query between(Expression expressionStart, Expression expressionEnd);

	public abstract Query between(Field fieldStart, Field fieldEnd);

	public abstract Query between(Field fieldStart, Object valueEnd);

	public abstract Query between(Object valueStart, Object valueEnd);

	public abstract Query between(Object valueStart, Field fieldEnd);

	public abstract Query in(Object... values);

	public abstract Query in(Long... values);

	public abstract Query like(Field field);

	public abstract Query like(Object value);

	public abstract Condition and(Query query);

	public abstract Condition or(Query query);

	public static Condition condition(Object value) {
		return internalQuery().and(value);
	}

	public static Condition condition(Field field) {
		return internalQuery().and(field);
	}

	public static Condition condition(Expression expression) {
		return internalQuery().and(expression);
	}

	private static Query internalQuery() {
		Query query = new Query(null) {

			@Override
			public Expression getNowMinus(long amount, TimeUnit unit) {
				throw new UnsupportedOperationException();
			}

			@Override
			protected QueryResult checkedExecute() throws SQLException {
				throw new UnsupportedOperationException();
			}
		};
		return query.select().from(new Name(""));
	}
}
