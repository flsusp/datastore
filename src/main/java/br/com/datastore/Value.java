package br.com.datastore;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;

public abstract class Value {

	public abstract boolean isNull();

	public abstract String getAsString();

	public abstract String requireAsString();

	public abstract Long getAsLong();

	public abstract long requireAsLong();

	public abstract Boolean getAsBoolean();

	public abstract boolean requireAsBoolean();

	public abstract Integer getAsInteger();

	public abstract int requireAsInteger();

	public static Value of(Object value) {
		if (value == null) {
			return new NullValue();
		} else {
			return new NotNullValue(value);
		}
	}

	public abstract DateTime getAsDateTime();

	public abstract DateTime requireAsDateTime();

	public abstract Double getAsDouble();

	public abstract Double requireAsDouble();

	public abstract Object getAsObject();

	public boolean isNotNull() {
		return !isNull();
	}

	@Override
	public String toString() {
		return getAsString();
	}
}

class NotNullValue extends Value {

	private static Class<?>[] supportedStringTypes = new Class[] { int.class, long.class, short.class, float.class,
			double.class, byte.class, boolean.class, char.class, Integer.class, Long.class, Short.class, Float.class,
			Double.class, Byte.class, Boolean.class, Character.class, String.class, BigDecimal.class };

	private Object value;

	protected NotNullValue(Object value) {
		if (value == null)
			throw new RuntimeException("Value cannot be null.");
		this.value = value;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public String getAsString() {
		for (Class<?> type : supportedStringTypes) {
			if (type.isInstance(value)) {
				return value.toString();
			}
		}
		throw new RuntimeException("Unsupported convertion from " + value.getClass() + " to String.");
	}

	@Override
	public String requireAsString() {
		return getAsString();
	}

	@Override
	public long requireAsLong() {
		return getAsLong();
	}

	@Override
	public Boolean getAsBoolean() {
		return Boolean.valueOf(value.toString());
	}

	@Override
	public Long getAsLong() {
		if (Number.class.isInstance(value)) {
			return ((Number) value).longValue();
		} else {
			return Long.valueOf(getAsString());
		}
	}

	@Override
	public boolean requireAsBoolean() {
		return getAsBoolean();
	}

	@Override
	public Integer getAsInteger() {
		if (Number.class.isInstance(value)) {
			return ((Number) value).intValue();
		} else {
			return Integer.valueOf(getAsString());
		}
	}

	@Override
	public int requireAsInteger() {
		return getAsInteger();
	}

	@Override
	public DateTime getAsDateTime() {
		if (Date.class.isInstance(value)) {
			return new DateTime(value);
		} else {
			return (DateTime) value;
		}
	}

	@Override
	public DateTime requireAsDateTime() {
		return getAsDateTime();
	}

	@Override
	public Double getAsDouble() {
		if (Number.class.isInstance(value)) {
			return ((Number) value).doubleValue();
		} else {
			return Double.valueOf(getAsString());
		}
	}

	@Override
	public Double requireAsDouble() {
		return getAsDouble();
	}

	@Override
	public Object getAsObject() {
		return value;
	}

}

class NullValue extends Value {

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public String getAsString() {
		return null;
	}

	@Override
	public String requireAsString() {
		fail();
		return null;
	}

	private void fail() {
		throw new RuntimeException("Value cannot be null.");
	}

	@Override
	public long requireAsLong() {
		fail();
		return 0;
	}

	@Override
	public Boolean getAsBoolean() {
		return null;
	}

	@Override
	public Long getAsLong() {
		return null;
	}

	@Override
	public boolean requireAsBoolean() {
		fail();
		return false;
	}

	@Override
	public Integer getAsInteger() {
		return null;
	}

	@Override
	public int requireAsInteger() {
		fail();
		return 0;
	}

	@Override
	public DateTime getAsDateTime() {
		return null;
	}

	@Override
	public DateTime requireAsDateTime() {
		fail();
		return null;
	}

	@Override
	public Double getAsDouble() {
		return null;
	}

	@Override
	public Double requireAsDouble() {
		fail();
		return null;
	}

	@Override
	public Object getAsObject() {
		return null;
	}
}
