package teamgoat.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.joda.time.DateTime;

import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public class BuzzwordPoweredDataProvider implements UserLocationDataProvider {
	
	static final String db = "jdbc:db2://bi-hadoop-prod-319.services.dal.bluemix.net:51000/bigsql";
	static final String user = "biblumix";
	static final String pwd = "ak8p~3@wh5lI";
	private Connection conn;
	
	private Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(db, user, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return conn;
	}

	public List<UserLocationSnapshot> getUsersWithinRange(TemporalLocation temporalLocation, double maxRangeInMeters) throws DataAccessException {
		return null;
	}

	public UserLocationSnapshot getLocation(User user, DateTime instant) throws DataAccessException {
		return null;
	}


	public User getUser(int userId) throws DataAccessException {
		QueryRunner runner = new QueryRunner();
		try {
		    String sql = "select * from bigsql.usertrajectorydata where userid=1  and time <= TIMESTAMP '2008-11-01 00:42:36'";
		    List<Map<String, Object>> list = runner.query(getConnection(), sql, new MapListHandler());

		    for(Map<String, Object> map : list) {
		    	System.out.println(map.get("latitude"));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
