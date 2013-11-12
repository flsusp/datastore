package br.com.datastore;

import java.sql.Connection;

public class WrapperConnectionSupplier implements ConnectionSupplier {

	private final Connection connection;

	public WrapperConnectionSupplier(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}
}
