package br.com.datastore;

import java.util.ArrayList;
import java.util.List;

public class QueryResultArray {

	private QueryResult queryResult;
	private Field field;

	public QueryResultArray(QueryResult queryResult, Field field) {
		this.queryResult = queryResult;
		this.field = field;
	}

	public Long[] ofLong() {
		List<Long> values = new ArrayList<Long>();
		for (Data data : queryResult.list()) {
			values.add(data.get(field).getAsLong());
		}
		return values.toArray(new Long[0]);
	}
}
