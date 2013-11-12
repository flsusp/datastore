package br.com.datastore;

public class TableNameWithAlias implements TableName {

	private TableName table;
	private String alias;

	public TableNameWithAlias(TableName table, String alias) {
		super();
		this.table = table;
		this.alias = alias;
	}

	@Override
	public String name() {
		StringBuilder s = new StringBuilder();
		s.append(table.name());
		s.append(" ");
		s.append(alias);
		return s.toString();
	}
}
