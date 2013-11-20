package br.com.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.a4c.utils.DateUtils;
import br.com.datastore.Value;

public class ValueTest {

	@Test
	public void testIsNull() {
		assertTrue(Value.of(null).isNull());
		assertFalse(Value.of("a").isNull());
	}

	@Test
	public void testGetAsString() {
		assertEquals("123", Value.of("123").getAsString());
		assertEquals("123", Value.of(123).getAsString());
		assertEquals("123", Value.of(123l).getAsString());
		assertEquals("123.1", Value.of(123.1).getAsString());
		assertNull(Value.of(null).getAsString());
	}

	@Test(expected = RuntimeException.class)
	public void testRequireAsStringShouldFailOnNull() {
		Value.of(null).requireAsString();
	}

	@Test(expected = RuntimeException.class)
	public void testGetAsStringUnsupportedType() {
		Value.of(DateUtils.now()).getAsString();
	}
}
