package br.com.datastore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public abstract class Update {

	protected final Datastore datastore;
	protected final TableName table;
	private List<Entry<Field, Expression>> fieldsToUpdate = new ArrayList<Entry<Field, Expression>>();
	private Query whereQuery;

	public Update(TableName table, Datastore datastore) {
		super();
		this.table = table;
		this.datastore = datastore;
	}

	private Query buildWhere(Datastore datastore) {
		return new Query(datastore) {

			@Override
			public QueryResult checkedExecute() {
				return Update.this.execute();
			}

			@Override
			public String toString() {
				return Update.this.toString();
			}

			@Override
			public Expression getNowMinus(long amount, TimeUnit unit) {
				return Update.this.getNowMinus(amount, unit);
			}
		};
	}

	protected abstract Expression getNowMinus(long amount, TimeUnit unit);

	public Update set(Field field, Object value) {
		fieldsToUpdate.add(new ListEntry<Field, Expression>(field, new ValueExpression(value)));
		return this;
	}

	public Update set(Field field, Field fieldValue) {
		fieldsToUpdate.add(new ListEntry<Field, Expression>(field, new FieldExpression(fieldValue)));
		return this;
	}

	public Update setNull(Field field) {
		fieldsToUpdate.add(new ListEntry<Field, Expression>(field, ValueExpression.Null));
		return this;
	}

	public abstract Update setCurrentTimestamp(Field field);

	protected Object[] getParameters() {
		List<Object> parameters = new ArrayList<Object>();

		Iterator<Entry<Field, Expression>> iterator = this.fieldsToUpdate.iterator();
		while (iterator.hasNext()) {
			Entry<Field, Expression> entry = iterator.next();
			entry.getValue().populateParameters(parameters);
		}

		if (this.whereQuery != null) {
			this.whereQuery.where.populateParameters(parameters);
		}
		return parameters.toArray();
	}

	public QueryResult execute() {
		try {
			return checkedExecute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract QueryResult checkedExecute() throws SQLException;

	public QueryResult executeAndClose() {
		try {
			return execute();
		} finally {
			datastore.close();
		}
	}

	public Condition where(Field field) {
		this.whereQuery = buildWhere(datastore);
		return this.whereQuery.and(field);
	}

	public Condition where(Object value) {
		this.whereQuery = buildWhere(datastore);
		return this.whereQuery.and(value);
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(this.table.name());
		sql.append(" SET ");

		Iterator<Entry<Field, Expression>> iterator = this.fieldsToUpdate.iterator();
		while (iterator.hasNext()) {
			Entry<Field, Expression> entry = iterator.next();
			sql.append(entry.getKey().name());
			sql.append(" = ");
			sql.append(entry.getValue().toString());
			if (iterator.hasNext()) {
				sql.append(",");
			}
			sql.append(" ");
		}

		if (this.whereQuery != null) {
			sql.append("WHERE ");
			sql.append(this.whereQuery.where.toString());
		}

		return sql.toString().trim();
	}

	protected void addFieldToUpdate(Field field, Expression expression) {
		fieldsToUpdate.add(new ListEntry<Field, Expression>(field, expression));
	}

	public List<Entry<Field, Expression>> getFieldsToUpdate() {
		return fieldsToUpdate;
	}

	private static final class ListEntry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		public ListEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
	}
}
