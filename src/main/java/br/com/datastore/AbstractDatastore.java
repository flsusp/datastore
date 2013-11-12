package br.com.datastore;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDatastore implements Datastore {

	private final ConnectionSupplier connectionSupplier;
	private boolean transaction = false;
	private Connection connection;

	public AbstractDatastore(Connection connection) {
		this.connectionSupplier = new WrapperConnectionSupplier(connection);
	}

	public AbstractDatastore(ConnectionSupplier connectionSupplier) {
		this.connectionSupplier = connectionSupplier;
	}

	@Override
	public void close() {
		if (connection != null) {
			try {
				if (this.transaction) {
					rollback();
				}

				connection.close();
				connection = null;
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public Connection getConnection() {
		if (connection == null) {
			connection = connectionSupplier.getConnection();
		}
		return connection;
	}

	@Override
	public void beginTransaction() {
		try {
			getConnection().setAutoCommit(false);
			this.transaction = true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commit() {
		try {
			getConnection().commit();
			getConnection().setAutoCommit(true);
			this.transaction = false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rollback() {
		try {
			getConnection().rollback();
			getConnection().setAutoCommit(true);
			this.transaction = false;
		} catch (SQLException e) {
		}
	}

	protected boolean isTransactionActive() {
		return transaction;
	}
}
