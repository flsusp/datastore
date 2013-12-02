package br.com.datastore;

public class AliasField implements Field {

	private String alias;
	private Field field;

	public AliasField(String alias, Field field) {
		super();
		this.alias = alias;
		this.field = field;
	}

	@Override
	public String name() {
		StringBuilder s = new StringBuilder();
		s.append(alias);
		s.append(".");
		s.append(field.name());
		return s.toString();
	}
}
