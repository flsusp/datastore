package br.com.datastore.preparedstatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class PreparedStatementUtils {

	public void setParametersOnStatement(PreparedStatement prepareStatement, Object[] parameters) throws SQLException {
		int i = 1;
		for (Object parameter : parameters) {
			setStatementParameter(prepareStatement, i++, parameter);
		}
	}

	protected void setStatementParameter(PreparedStatement prepareStatement, int i, Object value) throws SQLException {
		if (value instanceof Timestamp) {
			prepareStatement.setTimestamp(i, (Timestamp) value, Calendar.getInstance());
		} else if (DateTime.class.isInstance(value)) {
			DateTime dateTimeValue = (DateTime) value;
			prepareStatement.setTimestamp(i, new java.sql.Timestamp(dateTimeValue.withZone(DateTimeZone.UTC)
					.getMillis()), Calendar.getInstance(TimeZone.getTimeZone("UTC")));
		} else if (Date.class.isInstance(value)) {
			prepareStatement.setTimestamp(i, new java.sql.Timestamp(((Date) value).getTime()), Calendar.getInstance());
		} else if (String.class.isInstance(value)) {
			prepareStatement.setString(i, (String) value);
		} else {
			prepareStatement.setObject(i, value);
		}
	}

	public Object extractObjectFromResultSet(ResultSet rs, int i) throws SQLException {
		final int columnType = rs.getMetaData().getColumnType(i);
		if (columnType == Types.TIMESTAMP || columnType == Types.DATE) {
			Timestamp t = rs.getTimestamp(i, Calendar.getInstance());
			if (t == null) {
				return null;
			} else {
				return new DateTime(t.getTime());
			}
		} else {
			return rs.getObject(i);
		}
	}
}
