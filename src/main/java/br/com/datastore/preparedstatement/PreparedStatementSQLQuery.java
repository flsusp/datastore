package br.com.datastore.preparedstatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.datastore.Data;
import br.com.datastore.Datastore;
import br.com.datastore.QueryResult;
import br.com.datastore.SQLQuery;

public abstract class PreparedStatementSQLQuery extends SQLQuery {

	private final PreparedStatementUtils utils;

	public PreparedStatementSQLQuery(Datastore datastore, PreparedStatementUtils utils) {
		super(datastore);
		this.utils = utils;
	}

	public PreparedStatementSQLQuery(String sql, Datastore datastore, PreparedStatementUtils utils) {
		super(sql, datastore);
		this.utils = utils;
	}

	@Override
	protected QueryResult checkedExecute() throws SQLException {
		Connection connection = datastore.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(toString());

			utils.setParametersOnStatement(statement, getParameters());

			QueryResult result = execute(statement);

			statement.close();

			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected QueryResult execute(PreparedStatement statement) throws SQLException {
		QueryResult result = new QueryResult();

		ResultSet rs = statement.executeQuery();

		populateResultFromResultSet(result, rs);

		rs.close();
		return result;
	}

	protected void populateResultFromResultSet(QueryResult result, ResultSet rs) throws SQLException {
		Integer columnCount = null;

		boolean next = rs.next();
		while (next) {
			Data data = result.createEntry();

			if (columnCount == null) {
				columnCount = rs.getMetaData().getColumnCount();
			}

			for (int i = 1; i <= columnCount; i++) {
				Object object = utils.extractObjectFromResultSet(rs, i);
				data.put(rs.getMetaData().getColumnName(i).toUpperCase(), object);
			}

			next = rs.next();
		}
	}
}
