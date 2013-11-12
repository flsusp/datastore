package br.com.datastore.preparedstatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.datastore.Datastore;
import br.com.datastore.QueryResult;
import br.com.datastore.TableName;
import br.com.datastore.Update;

public abstract class PreparedStatementUpdate extends Update {

	private final PreparedStatementUtils utils;

	public PreparedStatementUpdate(TableName table, Datastore datastore, PreparedStatementUtils utils) {
		super(table, datastore);
		this.utils = utils;
	}

	@Override
	public QueryResult checkedExecute() throws SQLException {
		Connection connection = datastore.getConnection();

		QueryResult result = new QueryResult();

		PreparedStatement statement = connection.prepareStatement(toString());
		utils.setParametersOnStatement(statement, getParameters());
		int rowCount = statement.executeUpdate();
		result.setNumberOfUpdatedRows(rowCount);
		statement.close();

		return result;
	}
}
