package br.com.datastore;

import java.sql.Connection;

public interface ConnectionSupplier {

	Connection getConnection();
}
