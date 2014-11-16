package com.mytestproject.bigsql;
import java.sql.*;

public class TestApplication {
	static final String db = "jdbc:db2://bi-hadoop-prod-319.services.dal.bluemix.net:51000/bigsql";
	static final String user = "biblumix";
	static final String pwd = "ak8p~3@wh5lI";
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		System.out.println("Started sample JDBC application.");
		try{
			conn = DriverManager.getConnection(db, user, pwd);
			System.out.println("Connected to the database.");
			
			stmt = conn.createStatement();
			System.out.println("Created a statement.");
			String sql;
			sql = "select * from bigsql.usertrajectorydata where userid=1  and time <= TIMESTAMP '2008-11-01 00:42:36'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("Executed a query.");
			
			System.out.println("Result set: ");
			while(rs.next()){
				int userid = rs.getInt("userid");
				double longitude = rs.getDouble("longitude");
				double latitude = rs.getDouble("latitude");
				Timestamp time = rs.getTimestamp("time");
				
				System.out.print("Userid: " + userid + "\n");
				System.out.print("Longitutde: " + longitude + "\n");
				System.out.print("Latitude: " + latitude + "\n");
				System.out.print("Timestamp: " + time + "\n");
			}
			
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException sqle2){
			} 
			try{
				if(conn!=null)
					conn.close();
			}
			catch(SQLException sqlE){
				sqlE.printStackTrace();
			}
		}
		System.out.println("Application complete");
	}
}
