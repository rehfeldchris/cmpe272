package teamgoat.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public class SqliteDataProvider implements UserLocationDataProvider {
	
	private static String db = "jdbc:sqlite:%s/bigsql.db";
	public static String cwd;
	
	private Connection conn;
	// this uses the haversine formula to calculate distance from lat/lng. Its gets innacurate the farther you get from the equator....but its the best we got for now.
	// paramters to bind, in order: LATITUDE, LONGITUDE, LATITUDE, LOWER TIME BOUND, UPPER TIME BOUND, LATITUDE, LONGITUDE, LATITUDE, maxDistanceInMeters
	private static final String findUsersWithinRangeSql_ = "select LATITUDE\r\n" + 
			"     , LONGITUDE\r\n" + 
			"	 , USERID\r\n" + 
			"	 , TIME\r\n" + 
			"       (\r\n" + 
			"			6366707.01949371 * acos (\r\n" + 
			"				cos ( radians(?) )\r\n" + 
			"				* cos( radians( LATITUDE ) )\r\n" + 
			"				* cos( radians( LONGITUDE ) - radians(?) )\r\n" + 
			"				+ sin ( radians(?) )\r\n" + 
			"				* sin( radians( LATITUDE ) )\r\n" + 
			"			)\r\n" + 
			"	  ) as distance\r\n" + 
			"FROM usertrajectorydata \r\n" + 
			"where TIME between ? and ?\r\n" + 
			"and \r\n" + 
			"\r\n" + 
			"(\r\n" + 
			"			6366707.01949371 * acos (\r\n" + 
			"				cos ( radians(?) )\r\n" + 
			"				* cos( radians( LATITUDE ) )\r\n" + 
			"				* cos( radians( LONGITUDE ) - radians(?) )\r\n" + 
			"				+ sin ( radians(?) )\r\n" + 
			"				* sin( radians( LATITUDE ) )\r\n" + 
			"			)\r\n" + 
			"	  ) < ?\r\n" + 
			"";
	
	private static final String findUsersWithinRangeSql = "	  \r\n" + 
			"select LATITUDE\r\n" + 
			"     , LONGITUDE\r\n" + 
			"	 , USERID\r\n" + 
			"	 , TIME\r\n" + 
			"FROM usertrajectorydata\r\n" + 
			"where TIME between ? and ?\r\n" + 
			"and LATITUDE between ? and ?\r\n" + 
			"and LONGITUDE between ? and ?\r\n" + 
			""
			;
	
	public boolean testConnection() {
		return getConnection() != null;
	}
	
	
	private Connection getConnection() {
		if (conn == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(String.format(db, cwd));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return conn;
	}

	public List<UserLocationSnapshot> getUsersWithinRange(TemporalLocation temporalLocation, double maxRangeInMeters) throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
			Duration moment = Duration.standardSeconds(2);
			DateTime instant = temporalLocation.getTimestamp();
			double rangeFactor = 0.1;
			double boundingBoxPlusMinus = maxRangeInMeters * rangeFactor;
			
			Object[] sqlArgs = new Object[]{
	    		formatForSql(instant.withDurationAdded(moment, -1)),
	    		formatForSql(instant.withDurationAdded(moment, 1)),
				temporalLocation.getLat() - boundingBoxPlusMinus, 
				temporalLocation.getLat() + boundingBoxPlusMinus,
				temporalLocation.getLng() - boundingBoxPlusMinus, 
				temporalLocation.getLng() + boundingBoxPlusMinus, 
			};
			
			//System.out.printf("running sql: %s\n", getInterpolatedSql(findUsersWithinRangeSql, sqlArgs));

		    List<Map<String, Object>> rows = runner.query(
	    		getConnection(), 
	    		findUsersWithinRangeSql, 
	    		new MapListHandler(),
	    		sqlArgs
    		);

		    return toUserLocationSnapshots(rows);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public UserLocationSnapshot getLocation(User user, DateTime instant) throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
			Duration moment = Duration.standardSeconds(2);
		    String sql = "select USERID, LATITUDE, LONGITUDE, TIME from usertrajectorydata where userid=? and time between ? and ? limit 1";
		    Map<String, Object> row = runner.query(
	    		getConnection(), 
	    		sql, 
	    		new MapHandler(), 
	    		user.getId(),
	    		formatForSql(instant.withDurationAdded(moment, -1)),
	    		formatForSql(instant.withDurationAdded(moment, 1))
    		);

		    return toUserLocationSnapshot(row);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public UserLocationSnapshot getLocationAtArbitraryTime(User user) throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
		    String sql = "select USERID, LATITUDE, LONGITUDE, TIME from usertrajectorydata where userid=? order by TIME asc limit 1";
		    Map<String, Object> row = runner.query(
	    		getConnection(), 
	    		sql, 
	    		new MapHandler(), 
	    		user.getId()
    		);

		    return toUserLocationSnapshot(row);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String getInterpolatedSql(String sql, Object[] args)
	{
		for (Object arg : args) {
			sql = sql.replaceFirst("\\?", arg.toString());
		}
		return sql;
	}


	public User getUser(int userId) throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
		    String sql = "select * from usertrajectorydata where userid=? limit 1";
		    Map<String, Object> row = runner.query(getConnection(), sql, new MapHandler(), userId);
		    // Check for existence....
		    if (row != null) {
		    	return new User("unknown name", userId);
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public int getUsersTest() throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
		    String sql = "select * from usertrajectorydata where userid between 100 and 77770 limit 100";
		    List<Map<String, Object>> rows = runner.query(getConnection(), sql, new MapListHandler());
		    // Check for existence....
		    if (rows != null) {
		    	
		    }
		    
		    return rows.size();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	private UserLocationSnapshot toUserLocationSnapshot(Map<String, Object> dbRow) {
	    if (dbRow == null) {
	    	return null;
	    }

	    return new UserLocationSnapshot(
			new User("", (Integer) dbRow.get("USERID")), 
			new TemporalLocation(
				(Double) dbRow.get("LATITUDE"),
				(Double) dbRow.get("LONGITUDE"),
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime((String) dbRow.get("TIME"))
			)
		);
	}
	
	private List<UserLocationSnapshot> toUserLocationSnapshots(List<Map<String, Object>> dbRows) {
		List<UserLocationSnapshot> snapshots = new ArrayList<>();
	    if (dbRows == null) {
	    	return snapshots;
	    }

	    for (Map<String, Object> dbRow : dbRows) {
	    	snapshots.add(toUserLocationSnapshot(dbRow));
	    }
	    
	    return snapshots;
	}
	
	private String formatForSql(ReadableInstant instant) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		return dtf.print(instant);
	}
	
	/*****************************************************
	 * Used for PlotTest
	 * @throws Exception 
	 ****************************************************/
	public List<UserLocationSnapshot> getUsersWithinTimeRange(DateTime dateTime, int seconds) throws Exception {
		QueryRunner runner = new QueryRunner();
		try {

			Duration moment = Duration.standardSeconds(seconds);
			DateTime instant = dateTime;
			
			Object[] sqlArgs = new Object[]{
	    		formatForSql(instant.withDurationAdded(moment, -1)),
	    		formatForSql(instant.withDurationAdded(moment, 1)),
	    		formatForSql(instant.withDurationAdded(moment, -1)),
	    		formatForSql(instant.withDurationAdded(moment, 1)),
			};

//			String sqlStatement = "	  \r\n" + 
//					"select max(latitude) latitude\r\n" + 
//					"     , max(LONGITUDE) LONGITUDE\r\n" + 
//					"	 , max(time) time\r\n" + 
//					"	 , userid\r\n" + 
//					"FROM usertrajectorydata\r\n" + 
//					"where TIME between ?\r\n" +
//					"and ?\r\n" +
//					"group by USERID\r\n" +
//					"";
			
			
			String sqlStatement = "	  \r\n" + 
					"select a.LATITUDE LATITUDE\r\n" + 
					"     , a.LONGITUDE LONGITUDE\r\n" + 
					"	 , a.USERID USERID\r\n" + 
					"	 , a.TIME TIME\r\n" + 
					"FROM (select * FROM usertrajectorydata\r\n" + 
					"where TIME between ?\r\n" +
					"and ?\r\n) a\r\n" + 
					"inner join (select USERID, MAX(TIME) MAXTIME from usertrajectorydata\r\n" +
					"where TIME between ?\r\n" +
					"and ?\r\n" +
					"group by USERID) b on a.USERID=b.USERID and a.TIME=b.MAXTIME\r\n" +
					"";
			
			System.out.println(sqlStatement);
		    List<Map<String, Object>> rows = runner.query(
	    		getConnection(), 
	    		sqlStatement, 
	    		new MapListHandler(),
	    		sqlArgs
    		);

		    return toUserLocationSnapshots(rows);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
