package br.com.datastore.preparedstatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.datastore.Datastore;
import br.com.datastore.Delete;
import br.com.datastore.TableName;

public abstract class PreparedStatementDelete extends Delete {

	private final PreparedStatementUtils utils;

	public PreparedStatementDelete(TableName table, Datastore datastore, PreparedStatementUtils utils) {
		super(table, datastore);
		this.utils = utils;
	}

	@Override
	public void checkedExecute() throws SQLException {
		Connection connection = datastore.getConnection();

		PreparedStatement prepareStatement = connection.prepareStatement(toString());

		utils.setParametersOnStatement(prepareStatement, getParameters());

		prepareStatement.execute();
		prepareStatement.close();
	}
}
