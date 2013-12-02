package br.com.datastore;

public class Join implements From {

	protected final From leftSide;
	protected final FromTable rightSide;
	protected final Query query;
	protected Condition on;
	protected Field using;

	public Join(Query query, From leftSide, TableName rightSideTable) {
		this.query = query;
		this.leftSide = leftSide;
		this.rightSide = new FromTable(rightSideTable);
		this.on = new EmptyCondition(query);
		this.using = null;
	}

	public Condition on(Field field) {
		this.on = this.on.and(field);
		return this.on;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(leftSide.toString());
		s.append(" JOIN ");
		s.append(rightSide.toString());
		if (using != null) {
			s.append(" USING (");
			s.append(using.name());
		} else {
			s.append(" ON (");
			s.append(on.toString());
		}
		s.append(")");
		return s.toString();
	}

	public Query using(Field field) {
		this.using = field;
		return this.query;
	}
}
