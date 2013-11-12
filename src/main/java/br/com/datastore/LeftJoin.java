package br.com.datastore;


public class LeftJoin extends Join {

	public LeftJoin(Query query, From leftSide, TableName rightSideTable) {
		super(query, leftSide, rightSideTable);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(leftSide.toString());
		s.append(" LEFT JOIN ");
		s.append(rightSide.toString());
		s.append(" ON (");
		s.append(on.toString());
		s.append(")");
		return s.toString();
	}
}
