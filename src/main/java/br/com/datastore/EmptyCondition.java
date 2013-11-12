package br.com.datastore;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmptyCondition extends Condition {

	public EmptyCondition(Query query) {
		super(query);
	}

	@Override
	public Query equalsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public Condition and(Field field) {
		return new ExpressionCondition(query, new FieldExpression(field));
	}

	@Override
	public Condition and(Query query) {
		return query.where;
	}

	@Override
	public Condition and(Object value) {
		return new ExpressionCondition(query, new ValueExpression(value));
	}

	@Override
	public Condition and(Expression expression) {
		return new ExpressionCondition(query, expression);
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
	}

	@Override
	public Condition or(Field field) {
		return new ExpressionCondition(query, new FieldExpression(field));
	}

	@Override
	public Condition or(Object value) {
		return new ExpressionCondition(query, new ValueExpression(value));
	}

	@Override
	public Condition or(Expression expression) {
		return new ExpressionCondition(query, expression);
	}

	@Override
	public Query equalsTo(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query equalsTo(Expression expression) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query notEqualsTo(Expression expression) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query notEqualsTo(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query notEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query isNull() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query isNotNull() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query greaterThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query greaterThanOrEqualsTo(Expression expression) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query greaterThanOrEqualsTo(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query between(Field fieldStart, Field fieldEnd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query between(Field fieldStart, Object valueEnd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query between(Object valueStart, Object valueEnd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query between(Object valueStart, Field fieldEnd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query between(Expression expressionStart, Expression expressionEnd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query in(Object... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query in(Long... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThanOrEqualsTo(Expression expression) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThanOrEqualsTo(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThan(Expression expression) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThan(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query lessThan(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query startsWith(String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query isNotBeforeNowMinus(long amount, TimeUnit unit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query like(Field field) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Query like(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Condition or(Query query) {
		return query.where;
	}
}
