package br.com.datastore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Query extends Expression {

	protected final Datastore datastore;
	private List<Expression> selectExpressions = new ArrayList<Expression>();
	private From from;
	protected Condition where = new EmptyCondition(this);
	private List<Expression> orderBy = new ArrayList<Expression>();
	private List<Expression> groupBy = new ArrayList<Expression>();
	private int limit = -1;
	private int offset = -1;
	private String workMemory;

	public Query(Datastore datastore) {
		super();
		this.datastore = datastore;
	}

	public QueryResult execute() {
		try {
			return checkedExecute();
		} catch (RuntimeException e) {
			throw e;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract QueryResult checkedExecute() throws SQLException;

	public QueryResult executeAndClose() {
		try {
			return execute();
		} finally {
			datastore.close();
		}
	}

	public Query withWorkMemoryOf(String workMemory) {
		this.workMemory = workMemory;
		return this;
	}

	protected String getWorkMemory() {
		return workMemory;
	}

	public String toString() {
		StringBuilder sql = new StringBuilder();

		sql.append("(SELECT");
		if (selectExpressions.isEmpty()) {
			sql.append(" *");
		} else {
			for (int i = 0; i < selectExpressions.size() - 1; i++) {
				sql.append(" ");
				sql.append(selectExpressions.get(i));
				sql.append(",");
			}
			sql.append(" ");
			sql.append(selectExpressions.get(selectExpressions.size() - 1));
		}

		if (from != null) {
			sql.append(" FROM ");
			sql.append(from.toString());

			if (!EmptyCondition.class.isInstance(where)) {
				sql.append(" WHERE ");
				sql.append(where.toString());
			}
		}

		if (!groupBy.isEmpty()) {
			sql.append(" GROUP BY");
			for (int i = 0; i < groupBy.size() - 1; i++) {
				sql.append(" ");
				sql.append(groupBy.get(i).toString());
				sql.append(",");
			}
			sql.append(" ");
			sql.append(groupBy.get(groupBy.size() - 1));
		}

		if (!orderBy.isEmpty()) {
			sql.append(" ORDER BY");
			for (int i = 0; i < orderBy.size() - 1; i++) {
				sql.append(" ");
				sql.append(orderBy.get(i).toString());
				sql.append(",");
			}
			sql.append(" ");
			sql.append(orderBy.get(orderBy.size() - 1));
		}

		if (limit >= 0) {
			sql.append(" LIMIT ");
			sql.append(limit);
		}

		if (offset >= 0) {
			sql.append(" OFFSET ");
			sql.append(offset);
		}
		sql.append(")");

		return sql.toString();
	}

	@Override
	protected void populateParameters(List<Object> parameters) {
		Collections.addAll(parameters, getParameters());
	}

	protected Object[] getParameters() {
		List<Object> parameters = new ArrayList<Object>();

		for (Expression selectExpression : selectExpressions) {
			selectExpression.populateParameters(parameters);
		}
		where.populateParameters(parameters);

		return parameters.toArray();
	}

	public Query select() {
		return this;
	}

	public Query select(List<Field> fields, Expression... expressions) {
		List<Expression> fieldExpressions = new ArrayList<Expression>();
		for (Field field : fields) {
			fieldExpressions.add(new FieldExpression(field));
		}
		this.selectExpressions.addAll(fieldExpressions);
		return select(expressions);
	}

	public Query select(Field... fields) {
		List<Expression> expressions = new ArrayList<Expression>();
		for (Field field : fields) {
			expressions.add(new FieldExpression(field));
		}
		this.selectExpressions.addAll(expressions);
		return this;
	}

	public Query select(Expression... expressions) {
		this.selectExpressions.addAll(Arrays.asList(expressions));
		return this;
	}

	public Query from(TableName table) {
		this.from = new FromTable(table);
		return this;
	}

	public Query from(TableName table, String alias) {
		this.from = new FromTable(new TableNameWithAlias(table, alias));
		return this;
	}

	public Condition where(Field field) {
		return and(field);
	}

	public Condition where(Object value) {
		return and(value);
	}

	public Condition where(Expression expression) {
		return and(expression);
	}

	public Condition and(Field field) {
		where = where.and(field);
		return where;
	}

	public Condition and(Object value) {
		where = where.and(value);
		return where;
	}

	public Condition and(Expression expression) {
		where = where.and(expression);
		return where;
	}

	public Query and(Query query) {
		where = where.and(query);
		return this;
	}

	public Query or(Query query) {
		where = where.or(query);
		return this;
	}

	public Query orderBy(Field field) {
		this.orderBy.add(new FieldExpression(field));
		return this;
	}

	public Query orderBy(Expression expression) {
		this.orderBy.add(expression);
		return this;
	}

	public Query groupBy(Field field) {
		this.groupBy.add(new FieldExpression(field));
		return this;
	}

	public Query groupBy(Expression expression) {
		this.groupBy.add(expression);
		return this;
	}

	public Condition or(Field field) {
		where = where.or(field);
		return where;
	}

	public Condition or(Object value) {
		where = where.or(value);
		return where;
	}

	public Condition or(Expression expression) {
		where = where.or(expression);
		return where;
	}

	public Join join(TableName table) {
		Join join = new Join(this, this.from, table);
		this.from = join;
		return join;
	}

	public Join join(TableName table, String alias) {
		return join(new TableNameWithAlias(table, alias));
	}

	public Join leftJoin(TableName table) {
		Join join = new LeftJoin(this, this.from, table);
		this.from = join;
		return join;
	}

	public Join leftJoin(TableName table, String alias) {
		return leftJoin(new TableNameWithAlias(table, alias));
	}

	public Query limit(int limit) {
		this.limit = limit;
		return this;
	}

	public Query offset(int offset) {
		this.offset = offset;
		return this;
	}

	public static List<Field> fields(Field... fields) {
		return Arrays.asList(fields);
	}

	public abstract Expression getNowMinus(long amount, TimeUnit unit);
}
