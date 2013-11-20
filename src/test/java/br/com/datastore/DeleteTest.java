package br.com.datastore;

import static br.com.datastore.QueryTest.Constants.FIELD2;
import static br.com.datastore.QueryTest.Constants.FIELD3;
import static br.com.datastore.QueryTest.Constants.TABLE1;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import br.com.datastore.Delete;
import br.com.datastore.Expression;
import br.com.datastore.TableName;

public class DeleteTest {

	@Test
	public void testDelete() {
		Delete delete = buildDelete(TABLE1);

		String sql = delete.where(FIELD3).equalsTo("123").and(FIELD2).isNull().toString();

		assertEquals("DELETE FROM TABLE1 WHERE (FIELD3 = ? AND FIELD2 IS NULL)", sql);
		assertEquals(1, delete.getParameters().length);
		assertEquals("123", delete.getParameters()[0]);
	}

	@Test
	public void testDeleteAll() {
		Delete delete = buildDelete(TABLE1);

		String sql = delete.toString();

		assertEquals("DELETE FROM TABLE1", sql);
		assertEquals(0, delete.getParameters().length);
	}

	private Delete buildDelete(TableName table) {
		return new Delete(table, null) {

			@Override
			protected Expression getNowMinus(long amount, TimeUnit unit) {
				return null;
			}

			@Override
			public void checkedExecute() throws SQLException {
			}
		};
	}
}
