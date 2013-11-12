package br.com.datastore;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Data {

	private Map<String, Value> values = new HashMap<String, Value>();

	public void put(String field, Object value) {
		values.put(field, Value.of(value));
	}

	public void put(Field field, Object value) {
		put(field.name(), value);
	}

	public boolean has(Field field) {
		return has(field.name());
	}

	public boolean has(String name) {
		return values.containsKey(name);
	}

	public Value get(Field field) {
		return get(field.name());
	}

	public Value getOptional(Field field) {
		return getOptional(field.name());
	}

	@Override
	public String toString() {
		return values.toString();
	}

	public Value get(String name) {
		if (has(name))
			return values.get(name);
		else
			throw new RuntimeException("Field with name " + name + " not found.");
	}

	public Value getOptional(String name) {
		if (has(name))
			return values.get(name);
		else
			return new NullValue();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		for (Entry<String, Value> entry : this.values.entrySet()) {
			map.put(entry.getKey(), entry.getValue().getAsObject());
		}

		return map;
	}

}
