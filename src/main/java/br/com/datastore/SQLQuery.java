package br.com.datastore;

import java.util.List;

public abstract class SQLQuery extends Query {

	private final String sql;
	private Object[] parameters;

	public SQLQuery(String sql, Datastore datastore) {
		super(datastore);
		this.sql = sql;
	}

	public SQLQuery(Datastore datastore) {
		super(datastore);
		this.sql = null;
	}

	@SuppressWarnings("rawtypes")
	public SQLQuery withParameters(List parameters) {
		return withParameters(parameters.toArray());
	}

	public SQLQuery withParameters(Object... parameters) {
		this.parameters = parameters;
		return this;
	}

	@Override
	public String toString() {
		if (this.sql != null) {
			return this.sql;
		} else {
			return super.toString();
		}
	}

	@Override
	protected Object[] getParameters() {
		if (this.parameters != null) {
			return this.parameters;
		} else {
			return super.getParameters();
		}
	}
}
