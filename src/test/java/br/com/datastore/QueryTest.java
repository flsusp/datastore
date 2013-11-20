package br.com.datastore;

import static br.com.datastore.Alias.all;
import static br.com.datastore.Alias.field;
import static br.com.datastore.Coalesce.coalesce;
import static br.com.datastore.Function.function;
import static br.com.datastore.Order.desc;
import static br.com.datastore.Query.fields;
import static br.com.datastore.QueryTest.Constants.FIELD1;
import static br.com.datastore.QueryTest.Constants.FIELD2;
import static br.com.datastore.QueryTest.Constants.FIELD3;
import static br.com.datastore.QueryTest.Constants.TABLE1;
import static br.com.datastore.QueryTest.Constants.TABLE2;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class QueryTest {

	public static enum Constants implements Field, TableName {
		FIELD1, FIELD2, FIELD3, TABLE1, TABLE2;
	}

	@Test
	public void testSimpleQuery() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQuerySelectAll() {
		Query query = buildQuery();

		query = query.select().from(TABLE1);
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testBetweenWithValues() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(10).between(1, 20);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE ? BETWEEN ? AND ?)", sql);
		assertEquals(3, query.getParameters().length);
	}

	@Test
	public void testBetweenWithFields() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD1).between(FIELD2, FIELD3);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD1 BETWEEN FIELD2 AND FIELD3)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQueryWithLimit() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).limit(1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 LIMIT 1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQueryWithLimitAndOffset() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).limit(1).offset(0);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 LIMIT 1 OFFSET 0)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQueryWithSelectAllFields() {
		Query query = buildQuery();

		query = query.select().from(TABLE1);
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQueryWithWhereClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).equalsTo(10);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 = ?)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
	}

	@Test
	public void testSimpleQueryWithInClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).in(8, 9, 10);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 IN (?, ?, ?))", sql);
		assertEquals(3, query.getParameters().length);
		assertEquals(8, query.getParameters()[0]);
		assertEquals(9, query.getParameters()[1]);
		assertEquals(10, query.getParameters()[2]);
	}

	@Test
	public void testSimpleQueryWithIsNullClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).isNull();
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 IS NULL)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testSimpleQueryWithGreaterThanClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).greaterThanOrEqualsTo(10);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 >= ?)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
	}

	@Test
	public void testSimpleQueryWithLessThanClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).lessThanOrEqualsTo(10);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 <= ?)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
	}

	@Test
	public void testSimpleQueryWithComplexWhereClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).equalsTo(10).and(FIELD1).equalsTo(true)
				.or(FIELD3).equalsTo(11);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE ((FIELD3 = ? AND FIELD1 = ?) OR FIELD3 = ?))", sql);
		assertEquals(3, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
		assertEquals(true, query.getParameters()[1]);
		assertEquals(11, query.getParameters()[2]);
	}

	@Test
	public void testSimpleQueryWithThreeOrsOnWhereClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).equalsTo(10).or(FIELD1).equalsTo(true)
				.or(FIELD3).equalsTo(11);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE ((FIELD3 = ? OR FIELD1 = ?) OR FIELD3 = ?))", sql);
		assertEquals(3, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
		assertEquals(true, query.getParameters()[1]);
		assertEquals(11, query.getParameters()[2]);
	}

	@Test
	public void testQueryWithWhereAndOrderByClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).where(FIELD3).equalsTo(10).orderBy(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 WHERE FIELD3 = ? ORDER BY FIELD1)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
	}

	@Test
	public void testQueryWithWhereAndGroupByClause() {
		Query query = buildQuery();

		query = query.select(FIELD1).from(TABLE1).where(FIELD3).equalsTo(10).groupBy(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1 FROM TABLE1 WHERE FIELD3 = ? GROUP BY FIELD1)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals(10, query.getParameters()[0]);
	}

	@Test
	public void testQueryWithGroupByClause() {
		Query query = buildQuery();

		query = query.select(FIELD1).from(TABLE1).groupBy(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1 FROM TABLE1 GROUP BY FIELD1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithOrderByClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).orderBy(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 ORDER BY FIELD1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithOrderByExpressionClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).orderBy(function("length", FIELD1));
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 ORDER BY length(FIELD1))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithOrderByDescClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).orderBy(desc(FIELD1));
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 ORDER BY FIELD1 DESC)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithJoinClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).join(TABLE2).on(FIELD1).equalsTo(FIELD3);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 JOIN TABLE2 ON (FIELD1 = FIELD3))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithJoinAndUsignClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).join(TABLE2).using(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 JOIN TABLE2 USING (FIELD1))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithLeftJoinClause() {
		Query query = buildQuery();

		query = query.select(FIELD1, FIELD2).from(TABLE1).leftJoin(TABLE2).on(FIELD1).equalsTo(FIELD3);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, FIELD2 FROM TABLE1 LEFT JOIN TABLE2 ON (FIELD1 = FIELD3))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithAliases() {
		Query query = buildQuery();

		query = query.select(field(TABLE1, FIELD1), field("t2", FIELD2)).from(TABLE1).join(TABLE2, "t2")
				.on(field(TABLE1, FIELD1)).equalsTo(field("t2", FIELD3));
		String sql = query.toString();

		assertEquals("(SELECT TABLE1.FIELD1, t2.FIELD2 FROM TABLE1 JOIN TABLE2 t2 ON (TABLE1.FIELD1 = t2.FIELD3))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryUsingAliasesOnSelectAll() {
		Query query = buildQuery();

		query = query.select(field(TABLE1, FIELD1), all("t2")).from(TABLE1).join(TABLE2, "t2")
				.on(field(TABLE1, FIELD1)).equalsTo(field("t2", FIELD3));
		String sql = query.toString();

		assertEquals("(SELECT TABLE1.FIELD1, t2.* FROM TABLE1 JOIN TABLE2 t2 ON (TABLE1.FIELD1 = t2.FIELD3))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithSelectCoalesce() {
		Query query = buildQuery();

		query = query.select(fields(FIELD1), coalesce("A", FIELD2, FIELD3)).from(TABLE1);
		String sql = query.toString();

		assertEquals("(SELECT FIELD1, COALESCE(FIELD2, FIELD3) AS A FROM TABLE1)", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryFunctions() {
		Query query = buildQuery();

		query = query.select(function("test", FIELD1)).from(TABLE1).where(FIELD1)
				.equalsTo(function("lower", function("trim", FIELD3)));
		String sql = query.toString();

		assertEquals("(SELECT test(FIELD1) FROM TABLE1 WHERE FIELD1 = lower(trim(FIELD3)))", sql);
		assertEquals(0, query.getParameters().length);
	}

	@Test
	public void testQueryWithLike() {
		Query query = buildQuery();

		query = query.select().from(TABLE1).where(FIELD1).like("%test%");
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1 WHERE FIELD1 LIKE ?)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals("%test%", query.getParameters()[0]);
	}

	@Test
	public void testQueryWithInvertedLike() {
		Query query = buildQuery();

		query = query.select().from(TABLE1).where("test").like(FIELD1);
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1 WHERE ? LIKE FIELD1)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals("test", query.getParameters()[0]);
	}

	@Test
	public void testQueryWithLike_StartsWith() {
		Query query = buildQuery();

		query = query.select().from(TABLE1).where(FIELD1).startsWith("test");
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1 WHERE FIELD1 LIKE ?)", sql);
		assertEquals(1, query.getParameters().length);
		assertEquals("test%", query.getParameters()[0]);
	}

	@Test
	public void testQueryWithNotBefore() {
		Query query = buildQuery();

		query = query.select().from(TABLE1).where(FIELD1).isNotBeforeNowMinus(1, TimeUnit.HOURS);
		String sql = query.toString();

		assertEquals("(SELECT * FROM TABLE1 WHERE FIELD1 >= current_timestamp - '1 hours'::interval)", sql);
		assertEquals(0, query.getParameters().length);
	}

	private Query buildQuery() {
		return new Query(null) {

			@Override
			public QueryResult checkedExecute() {
				return null;
			}

			@Override
			public Expression getNowMinus(long amount, TimeUnit unit) {
				return new InlineValueExpression("current_timestamp - '" + amount + " " + unit.name().toLowerCase()
						+ "'::interval");
			}
		};
	}
}
