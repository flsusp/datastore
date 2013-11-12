package br.com.datastore;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.a4c.AbstractTestCase;
import br.com.a4c.datastore.DatastoreService;
import br.com.a4c.datastore.DatastoreServiceName;
import br.com.a4c.datastore.impl.DataAccessManagerDatastoreServiceImpl;
import br.com.datastore.Data;
import br.com.datastore.Datastore;
import br.com.datastore.Field;
import br.com.datastore.QueryResult;

public class DatastoreServiceTest extends AbstractTestCase {

	private DatastoreService service;

	@Before
	public void before() {
		service = new DataAccessManagerDatastoreServiceImpl();

		Datastore dts = service.get(DatastoreServiceName.ConfigWrite);
		try {
			dts.sqlExecute(
					"CREATE TABLE TESTTABLE(ID INTEGER, NAME VARCHAR(255), DESCRIPTION VARCHAR(255), DATA DATE);")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(1, 'Test2', 'asdasdasdasdasdasd', '2012-01-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(2, 'Test3', 'asdasdasdasdasdasd', '2012-02-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(3, 'Test4', 'asdasdasdasdasdasd', '2012-01-02');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(4, 'Test5', 'asdasdasdasdasdasd', '2012-03-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(5, 'Test6', 'asdasdasdasdasdasd', '2012-01-03');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(6, 'Test7', 'asdasdasdasdasdasd', '2012-04-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(7, 'Test8', 'asdasdasdasdasdasd', '2012-01-04');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(8, 'Test9', 'asdasdasdasdasdasd', '2012-05-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(9, 'Test10', 'asdasdasdasdasdasd', '2012-01-05');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(10, 'Test11', 'asdasdasdasdasdasd', '2012-06-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(11, 'Test12', 'asdasdasdasdasdasd', '2012-01-06');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(12, 'Test13', 'asdasdasdasdasdasd', '2012-07-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(13, 'Test14', 'asdasdasdasdasdasd', '2012-01-07');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(14, 'Test15', 'asdasdasdasdasdasd', '2012-08-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(15, 'Test16', 'asdasdasdasdasdasd', '2012-01-08');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(16, 'Test17', 'asdasdasdasdasdasd', '2012-09-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(17, 'Test18', 'asdasdasdasdasdasd', '2012-01-09');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(18, 'Test19', 'asdasdasdasdasdasd', '2012-11-01');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(19, 'Test20', 'asdasdasdasdasdasd', '2012-01-11');")
					.execute();
			dts.sqlExecute(
					"INSERT INTO TESTTABLE(ID, NAME, DESCRIPTION, DATA) VALUES(20, 'Test21', 'asdasdasdasdasdasd', '2012-12-12');")
					.execute();
		} finally {
			dts.close();
		}
	}

	@After
	public void after() {
		Datastore dts = service.get(DatastoreServiceName.ConfigWrite);
		dts.sqlExecute("DROP TABLE TESTTABLE;").executeAndClose();
	}

	@Test
	public void testSimpleQuery() {
		DatastoreService service = new DataAccessManagerDatastoreServiceImpl();
		Datastore datastore = service.get(DatastoreServiceName.ConfigRead);
		QueryResult result = datastore.sql("select name from testtable where id = ?").withParameters(1l)
				.executeAndClose();
		Data unique = result.unique();
		assertEquals("Test2", unique.get(TestFieldName.NAME).getAsString());
	}
}

enum TestFieldName implements Field {
	NAME;
}
