package br.com.datastore;

import static br.com.datastore.QueryTest.Constants.FIELD1;
import static br.com.datastore.QueryTest.Constants.FIELD2;
import static br.com.datastore.QueryTest.Constants.FIELD3;
import static br.com.datastore.QueryTest.Constants.TABLE1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import br.com.datastore.Expression;
import br.com.datastore.QueryResult;
import br.com.datastore.TableName;
import br.com.datastore.Update;

public class UpdateTest {

	@Test
	public void testUpdate() {
		Update update = buildUpdate(TABLE1);

		String sql = update.set(FIELD1, 10).set(FIELD2, FIELD3).setNull(FIELD3).setCurrentTimestamp(FIELD3)
				.where(FIELD3).equalsTo("123").and(FIELD2).equalsTo(1).toString();

		assertEquals(
				"UPDATE TABLE1 SET FIELD1 = ?, FIELD2 = FIELD3, FIELD3 = ?, FIELD3 = current_timestamp WHERE (FIELD3 = ? AND FIELD2 = ?)",
				sql);
		assertEquals(4, update.getParameters().length);
		assertEquals(10, update.getParameters()[0]);
		assertNull(update.getParameters()[1]);
		assertEquals("123", update.getParameters()[2]);
		assertEquals(1, update.getParameters()[3]);
	}

	@Test
	public void testUpdateWithoutWhere() {
		Update update = buildUpdate(TABLE1);

		String sql = update.set(FIELD1, 10).set(FIELD2, FIELD3).setNull(FIELD3).setCurrentTimestamp(FIELD3).toString();

		assertEquals("UPDATE TABLE1 SET FIELD1 = ?, FIELD2 = FIELD3, FIELD3 = ?, FIELD3 = current_timestamp", sql);
		assertEquals(2, update.getParameters().length);
		assertEquals(10, update.getParameters()[0]);
		assertNull(update.getParameters()[1]);
	}

	private Update buildUpdate(TableName table) {
		return new Update(table, null) {

			@Override
			public QueryResult checkedExecute() {
				return null;
			}

			@Override
			protected Expression getNowMinus(long amount, TimeUnit unit) {
				return null;
			}
		};
	}
}
