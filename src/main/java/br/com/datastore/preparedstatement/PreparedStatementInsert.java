package br.com.datastore.preparedstatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.datastore.Datastore;
import br.com.datastore.Insert;
import br.com.datastore.QueryResult;
import br.com.datastore.TableName;

public abstract class PreparedStatementInsert extends Insert {

	private final PreparedStatementUtils utils;

	public PreparedStatementInsert(TableName table, Datastore datastore, PreparedStatementUtils utils) {
		super(table, datastore);
		this.utils = utils;
	}

	@Override
	protected QueryResult checkedExecute() throws SQLException {
		Connection connection = datastore.getConnection();

		QueryResult result = new QueryResult();

		PreparedStatement statement = connection.prepareStatement(toString(), Statement.RETURN_GENERATED_KEYS);
		utils.setParametersOnStatement(statement, getParameters());
		statement.execute();

		Integer columnCount = null;
		ResultSet rs = statement.getGeneratedKeys();
		boolean next = rs.next();
		if (next) {
			if (columnCount == null) {
				columnCount = rs.getMetaData().getColumnCount();
			}

			for (int i = 1; i <= columnCount; i++) {
				Object object = utils.extractObjectFromResultSet(rs, i);
				result.addGeneratedKey(rs.getMetaData().getColumnName(i).toUpperCase(), object);
			}
		}

		rs.close();
		statement.close();

		return result;
	}
}
