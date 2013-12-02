package br.com.datastore;

public class AliasAll implements Field {

	private String alias;

	public AliasAll(String alias) {
		super();
		this.alias = alias;
	}

	@Override
	public String name() {
		StringBuilder s = new StringBuilder();
		if (alias != null) {
			s.append(alias);
			s.append(".");
		}
		s.append("*");
		return s.toString();
	}
}
