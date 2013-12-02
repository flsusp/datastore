package br.com.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FunctionTest {

	private static final Field FIELD = new Field() {

		@Override
		public String name() {
			return "FIELD";
		}
	};

	@Test
	public void testEmptyFunction() {
		Expression expression = Function.function("test");
		assertEquals("test()", expression.toString());
		assertNoParameters(expression);
	}

	@Test
	public void testFunctionWithParameters() {
		Expression expression = Function.function("test", FIELD, new InlineValueExpression("now()"), 1);
		assertEquals("test(FIELD, now(), ?)", expression.toString());
		assertParameters(expression, 1);
	}

	@Test
	public void testCount() {
		Expression expression = Function.count();
		assertEquals("count(*)", expression.toString());
		assertNoParameters(expression);
	}

	@Test
	public void testCountWithParameter() {
		Expression expression = Function.count(FIELD);
		assertEquals("count(FIELD)", expression.toString());
		assertNoParameters(expression);
	}

	@Test
	public void testSumWithField() {
		Expression expression = Function.sum(FIELD);
		assertEquals("sum(FIELD)", expression.toString());
		assertNoParameters(expression);
	}

	@Test
	public void testSumWithExpression() {
		Expression expression = Function.sum(new InlineValueExpression("1"));
		assertEquals("sum(1)", expression.toString());
		assertNoParameters(expression);
	}

	@Test
	public void testSumWithObject() {
		Expression expression = Function.sum(1);
		assertEquals("sum(?)", expression.toString());
		assertParameters(expression, 1);
	}

	@Test
	public void testCoalesce() {
		Expression expression = Function.coalesce(FIELD, new InlineValueExpression("dt"), 0);
		assertEquals("coalesce(FIELD, dt, ?)", expression.toString());
		assertParameters(expression, 0);
	}

	@Test
	public void testNow() {
		Expression expression = Function.now();
		assertEquals("now()", expression.toString());
		assertNoParameters(expression);
	}

	private void assertParameters(Expression expression, Object... objs) {
		List<Object> parameters = new ArrayList<>();
		expression.populateParameters(parameters);

		if (parameters.size() != objs.length) {
			fail("Expected " + objs.length + " parameters, but was " + parameters.size());
		}

		Iterator<Object> resultIterator = parameters.iterator();
		for (Object expectedObject : objs) {
			Object result = resultIterator.next();
			assertEquals(expectedObject, result);
		}
	}

	private void assertNoParameters(Expression expression) {
		List<Object> parameters = new ArrayList<>();
		expression.populateParameters(parameters);
		assertEquals(0, parameters.size());
	}
}
