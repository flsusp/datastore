package br.com.datastore;

import java.util.List;

public abstract class Expression {

	public abstract String toString();

	protected abstract void populateParameters(List<Object> parameters);
}
