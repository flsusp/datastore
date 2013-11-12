package br.com.datastore;

import java.sql.Connection;
import java.util.List;

public interface Datastore {

	SQLQuery sqlExecute(String sql);

	SQLQuery sqlExecute(StringBuilder sql);

	SQLQuery sql(String sql);

	SQLQuery sql(StringBuilder sql);

	void close();

	Query select();

	Query select(Field... fields);

	Query select(List<Field> fields, Expression... expressions);

	Query select(Expression... expressions);

	Insert create(TableName table);

	void commit();

	void rollback();

	void beginTransaction();

	Update update(TableName table);

	Delete deleteFrom(TableName table);

	Connection getConnection();
}