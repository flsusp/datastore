package br.com.datastore;

public class Name implements Field, TableName, Comparable<Object> {

	private String name;

	public Name(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		return name().compareTo(o.toString());
	}

	@Override
	public String toString() {
		return name();
	}
}
