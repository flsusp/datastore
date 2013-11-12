package br.com.datastore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

public abstract class Delete {

	protected final Datastore datastore;
	protected final TableName table;
	private Query whereQuery;

	public Delete(TableName table, Datastore datastore) {
		super();
		this.table = table;
		this.datastore = datastore;
		this.whereQuery = new Query(datastore) {

			@Override
			protected QueryResult checkedExecute() throws SQLException {
				Delete.this.checkedExecute();
				return new QueryResult();
			}

			@Override
			public String toString() {
				return Delete.this.toString();
			}

			@Override
			public Expression getNowMinus(long amount, TimeUnit unit) {
				return Delete.this.getNowMinus(amount, unit);
			}
		};
	}

	protected abstract Expression getNowMinus(long amount, TimeUnit unit);

	protected Object[] getParameters() {
		List<Object> parameters = new ArrayList<Object>();
		this.whereQuery.where.populateParameters(parameters);
		return parameters.toArray();
	}

	public void execute() {
		try {
			checkedExecute();
		} catch (RuntimeException e) {
			throw e;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void checkedExecute() throws SQLException;

	public void executeAndClose() {
		try {
			execute();
		} finally {
			datastore.close();
		}
	}

	public Condition where(Field field) {
		return this.whereQuery.and(field);
	}

	public Condition where(Object value) {
		return this.whereQuery.and(value);
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(this.table.name());

		String where = this.whereQuery.where.toString();
		if (!StringUtils.isEmpty(where)) {
			sql.append(" WHERE ");
			sql.append(where);
		}

		return sql.toString();
	}
}
