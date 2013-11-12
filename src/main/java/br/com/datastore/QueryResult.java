package br.com.datastore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class QueryResult {

	public static interface Transformation<T> {

		T transform(Data data);
	}

	private List<Data> result = new ArrayList<Data>();
	private Map<String, Value> generatedKeys = new HashMap<String, Value>();
	private int numberOfUpdatedRows;

	public Data createEntry() {
		Data data = new Data();
		result.add(data);
		return data;
	}

	public Data unique() {
		if (result.isEmpty()) {
			throw new RuntimeException("To return an unique value the result cannot be empty.");
		}
		if (result.size() > 1) {
			throw new RuntimeException("To return an unique value the result must have just one element.");
		}
		return result.get(0);
	}

	public int count() {
		return result.size();
	}

	public Collection<Data> list() {
		return Collections.unmodifiableCollection(result);
	}

	public <T> List<T> listOf(Class<T> clazz) {
		try {
			final Constructor<T> constructor = clazz.getConstructor(Data.class);

			List<T> newResult = new ArrayList<T>();
			for (Data data : this.result) {
				newResult.add(constructor.newInstance(data));
			}

			return newResult;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> List<T> list(Transformation<T> transformation) {
		List<T> newResult = new ArrayList<T>();

		for (Data data : this.result) {
			newResult.add(transformation.transform(data));
		}

		return newResult;
	}

	public Value getGeneratedKeyFor(String field) {
		return getGeneratedKeyFor(new Name(field));
	}

	public Value getGeneratedKeyFor(Field field) {
		String key = field.name().toUpperCase();

		if (this.generatedKeys.containsKey(key)) {
			return this.generatedKeys.get(key);
		} else {
			return new NullValue();
		}
	}

	public void addGeneratedKey(String key, Object value) {
		this.generatedKeys.put(key.toUpperCase(), Value.of(value));
	}

	public boolean isEmpty() {
		return result.isEmpty();
	}

	public QueryResultArray toArray(Field field) {
		return new QueryResultArray(this, field);
	}

	public void setNumberOfUpdatedRows(int numberOfUpdatedRows) {
		this.numberOfUpdatedRows = numberOfUpdatedRows;
	}

	public int getNumberOfUpdatedRows() {
		return numberOfUpdatedRows;
	}

	public Map<String, Object> getGeneratedKeysAsMap() {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Entry<String, Value> entry : this.generatedKeys.entrySet()) {
			result.put(entry.getKey(), entry.getValue().getAsObject());
		}

		return result;
	}
}
