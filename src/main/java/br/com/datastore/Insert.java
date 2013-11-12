package br.com.datastore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class Insert {

	protected final Datastore datastore;
	protected final TableName table;
	private Map<Field, Expression> expressions = new TreeMap<Field, Expression>(new Comparator<Object>() {

		@Override
		public int compare(Object o1, Object o2) {
			return o1.toString().compareTo(o2.toString());
		}
	});

	public Insert(TableName table, Datastore datastore) {
		super();
		this.table = table;
		this.datastore = datastore;
	}

	public TableName getTable() {
		return table;
	}

	public Insert with(Field field, Object value) {
		return with(field, new ValueExpression(value));
	}

	public Insert with(Field field, Expression expression) {
		if (expression == null) {
			expression = new ValueExpression(null);
		}
		expressions.put(field, expression);
		return this;
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

	public abstract Insert withCurrentTimestamp(Field field);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toString() {
		StringBuilder sql = new StringBuilder();

		List<Entry<Field, Expression>> expressionList = new ArrayList(expressions.entrySet());

		sql.append("INSERT INTO ");
		sql.append(table);
		sql.append("(");

		for (int i = 0; i < expressionList.size(); i++) {
			Field field = expressionList.get(i).getKey();
			sql.append(field);
			if (i < expressionList.size() - 1) {
				sql.append(", ");
			}
		}

		sql.append(")");
		sql.append(" VALUES (");

		for (int i = 0; i < expressionList.size(); i++) {
			Expression expression = expressionList.get(i).getValue();
			sql.append(expression.toString());
			if (i < expressionList.size() - 1) {
				sql.append(", ");
			}
		}

		sql.append(")");

		return sql.toString();
	}

	public Object[] getParameters() {
		List<Object> parameters = new ArrayList<Object>();

		for (Expression expression : expressions.values()) {
			expression.populateParameters(parameters);
		}

		return parameters.toArray();
	}

	public boolean containsField(Field field) {
		return this.expressions.containsKey(field);
	}
}
