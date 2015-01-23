package teamgoat.data;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class PopulateSqliteDb {
	
	public PopulateSqliteDb() {
		try {
			insert();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<CSVRecord> getRows() throws Exception {
		List<CSVRecord> rows = new ArrayList<>();
		Reader in = new FileReader("C:\\Users\\IBM_ADMIN\\Downloads\\plague-data\\NewData.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		int i = 0;
		for (CSVRecord record : records) {
		    rows.add(record);
//		    if (i++ > 5000) {
//		    	return rows;
//		    }
		}
		
		in = new FileReader("C:\\Users\\IBM_ADMIN\\Downloads\\plague-data\\AppendData.csv");
		records = CSVFormat.EXCEL.withHeader().parse(in);

		for (CSVRecord record : records) {
		    rows.add(record);
		}
		
		return rows;
	}
	
	private void insert() throws Exception {
	    // load the sqlite-JDBC driver using the current class loader
	    Class.forName("org.sqlite.JDBC");

	    Connection connection = DriverManager.getConnection("jdbc:sqlite:data/bigsql.db");
		QueryRunner runner = new QueryRunner();
		ResultSetHandler h = new ArrayListHandler();
		String sql = null;

		runner.update(connection, "drop table if exists usertrajectorydata");
		runner.update(connection, "create table usertrajectorydata (LATITUDE real, LONGITUDE real, USERID int, TIME text,  primary key (TIME, LATITUDE, LONGITUDE, USERID))");
		
		connection.setAutoCommit(false);
		
		int start = 0;
		int end = 5000000;
		int i = -1;
		for (CSVRecord rec : getRows()) {
			i++;
			if (i < start) {
				continue;
			}
			if (i >= end) {
				break;
			}
			sql = "insert or ignore into usertrajectorydata (LATITUDE, LONGITUDE, USERID, TIME) values (?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setDouble(1, Double.parseDouble(rec.get("Latitude")));
			ps.setDouble(2, Double.parseDouble(rec.get("Longitude")));
			ps.setInt(3, Integer.parseInt(rec.get("UserId")));
			ps.setString(4, rec.get("Timestamp"));
			int userid = Integer.parseInt(rec.get("UserId"));

			
			ps.execute();
			
			int updated = ps.getUpdateCount();
			if (updated == 0) {
				System.out.println("no update " + userid);
			}
			

			if (i % 1000 == 0) {
				System.out.println("line" + i);
				connection.commit();
			}
			
			ps.close();
		}
		
		connection.commit();
	}
	
	
	public static void main(String args[]) {
		new PopulateSqliteDb();
	}
}
