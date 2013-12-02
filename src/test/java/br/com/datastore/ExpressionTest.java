package br.com.datastore;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExpressionTest {

	@Test
	public void testInline() {
		Expression expression = Expression.inline("test");
		assertEquals("test", expression.toString());
	}
}
