package br.com.datastore;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExpressionCondition extends Condition {

	private final Expression leftSideExpression;
	private Operator operator;
	private Expression rightSideExpression;

	public ExpressionCondition(Query query, Expression leftSideExpression) {
		super(query);
		this.leftSideExpression = leftSideExpression;
	}

	public ExpressionCondition(Query query, Expression leftSideExpression, Operator operator,
			Expression rightSideExpression) {
		super(query);
		this.leftSideExpression = leftSideExpression;
		this.operator = operator;
		this.rightSideExpression = rightSideExpression;
	}

	@Override
	public Query equalsTo(Object value) {
		return equalsTo(new ValueExpression(value));
	}

	@Override
	public Query equalsTo(Field field) {
		return equalsTo(new FieldExpression(field));
	}

	@Override
	public Query equalsTo(Expression expression) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.Equals;
			this.rightSideExpression = expression;
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).equalsTo(expression);
		}
		return query;
	}

	@Override
	public Query notEqualsTo(Object value) {
		return notEqualsTo(new ValueExpression(value));
	}

	@Override
	public Query notEqualsTo(Field field) {
		return notEqualsTo(new FieldExpression(field));
	}

	@Override
	public Query notEqualsTo(Expression expression) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.NotEquals;
			this.rightSideExpression = expression;
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).notEqualsTo(expression);
		}
		return query;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		if (this.operator.useParenthesis())
			s.append("(");

		s.append(leftSideExpression.toString());
		s.append(" ");
		if (operator.toString() != null) {
			s.append(operator.toString());
			s.append(" ");
		}
		s.append(rightSideExpression.toString());

		if (this.operator.useParenthesis())
			s.append(")");

		return s.toString();
	}

	@Override
	public Condition and(Field field) {
		return new ExpressionCondition(query, this, Operator.And, new ExpressionCondition(query, new FieldExpression(
				field)));
	}

	@Override
	public Condition and(Object value) {
		return new ExpressionCondition(query, this, Operator.And, new ExpressionCondition(query, new ValueExpression(
				value)));
	}

	@Override
	public Condition and(Expression expression) {
		return new ExpressionCondition(query, this, Operator.And, new ExpressionCondition(query, expression));
	}

	@Override
	public Condition and(Query query) {
		return new ExpressionCondition(query, this, Operator.And, query.where);
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		leftSideExpression.populateParameters(parameters);
		rightSideExpression.populateParameters(parameters);
	}

	@Override
	public Condition or(Field field) {
		return new ExpressionCondition(query, this, Operator.Or, new ExpressionCondition(query, new FieldExpression(
				field)));
	}

	@Override
	public Condition or(Object value) {
		return new ExpressionCondition(query, this, Operator.Or, new ExpressionCondition(query, new ValueExpression(
				value)));
	}

	@Override
	public Condition or(Expression expression) {
		return new ExpressionCondition(query, this, Operator.Or, new ExpressionCondition(query, expression));
	}

	@Override
	public Condition or(Query query) {
		return new ExpressionCondition(query, this, Operator.Or, query.where);
	}

	@Override
	public Query isNull() {
		if (this.rightSideExpression == null) {
			this.operator = Operator.IsNull;
			this.rightSideExpression = new NullExpression();
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).isNull();
		}
		return query;
	}

	@Override
	public Query isNotNull() {
		if (this.rightSideExpression == null) {
			this.operator = Operator.IsNotNull;
			this.rightSideExpression = new NotNullExpression();
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).isNotNull();
		}
		return query;
	}

	@Override
	public Query greaterThanOrEqualsTo(Object value) {
		return greaterThanOrEqualsTo(new ValueExpression(value));
	}

	@Override
	public Query greaterThanOrEqualsTo(Expression expression) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.GreaterThanOrEquals;
			this.rightSideExpression = expression;
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).greaterThanOrEqualsTo(expression);
		}
		return query;
	}

	@Override
	public Query greaterThanOrEqualsTo(Field field) {
		return greaterThanOrEqualsTo(new FieldExpression(field));
	}

	@Override
	public Query lessThanOrEqualsTo(Expression expression) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.LessThanOrEquals;
			this.rightSideExpression = expression;
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).lessThanOrEqualsTo(expression);
		}
		return query;
	}

	@Override
	public Query lessThanOrEqualsTo(Field field) {
		return lessThanOrEqualsTo(new FieldExpression(field));
	}

	@Override
	public Query lessThanOrEqualsTo(Object value) {
		return lessThanOrEqualsTo(new ValueExpression(value));
	}

	@Override
	public Query lessThan(Expression expression) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.LessThan;
			this.rightSideExpression = expression;
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).lessThan(expression);
		}
		return query;
	}

	@Override
	public Query lessThan(Field field) {
		return lessThan(new FieldExpression(field));
	}

	@Override
	public Query lessThan(Object value) {
		return lessThan(new ValueExpression(value));
	}

	@Override
	public Query between(Expression expressionStart, Expression expressionEnd) {
		Expression expression = new BetweenExpression(expressionStart, expressionEnd);
		this.operator = Operator.Between;
		this.rightSideExpression = expression;
		return query;
	}

	@Override
	public Query between(Field fieldStart, Field fieldEnd) {
		return between(new FieldExpression(fieldStart), new FieldExpression(fieldEnd));
	}

	@Override
	public Query between(Field fieldStart, Object valueEnd) {
		return between(new FieldExpression(fieldStart), new ValueExpression(valueEnd));
	}

	@Override
	public Query between(Object valueStart, Object valueEnd) {
		return between(new ValueExpression(valueStart), new ValueExpression(valueEnd));
	}

	@Override
	public Query between(Object valueStart, Field fieldEnd) {
		return between(new ValueExpression(valueStart), new FieldExpression(fieldEnd));
	}

	@Override
	public Query in(Object... values) {
		if (values.length == 0) {
			throw new RuntimeException("Operator IN cannot have an empty set of values.");
		}

		if (this.rightSideExpression == null) {
			this.operator = Operator.In;
			this.rightSideExpression = new ListValuesExpression(values);
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).in(values);
		}
		return query;
	}

	@Override
	public Query in(Long... values) {
		return in((Object[]) values);
	}

	@Override
	public Query startsWith(String value) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.Like;
			this.rightSideExpression = new ValueExpression(value + "%");
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).startsWith(value);
		}
		return query;
	}

	@Override
	public Query isNotBeforeNowMinus(long amount, TimeUnit unit) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.GreaterThanOrEquals;
			this.rightSideExpression = query.getNowMinus(amount, unit);
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).isNotBeforeNowMinus(amount, unit);
		}
		return query;
	}

	@Override
	public Query like(Field field) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.Like;
			this.rightSideExpression = new FieldExpression(field);
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).like(field);
		}
		return query;
	}

	@Override
	public Query like(Object value) {
		if (this.rightSideExpression == null) {
			this.operator = Operator.Like;
			this.rightSideExpression = new ValueExpression(value);
		} else if (Condition.class.isInstance(this.rightSideExpression)) {
			((Condition) this.rightSideExpression).like(value);
		}
		return query;
	}
}
