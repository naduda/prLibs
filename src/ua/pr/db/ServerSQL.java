package ua.pr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServerSQL {
	
	public static Connection getConnection(String strConn) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(strConn);
		} catch (Exception e) {
			System.out.println("error: getConnection(String servName, String user, String password); " + e);
		}
		return conn;		
	}
	
	public static List<String> getDBmsSQL(Connection conn) {
		List<String> result = new ArrayList<String>();
		Statement stmt = null;

		try {
			stmt = conn.createStatement();

			ResultSet res = stmt.executeQuery("EXEC sp_databases");
			
			String sysDB = ":master:model:msdb:tempdb:";
			while (res.next()) 
		    {
				String nameDB = res.getString(1);
				if (sysDB.indexOf(nameDB) == -1) {
					result.add(nameDB);
				}   
		    }
		} catch (Exception e) {
			System.out.println("Error - : " + e);
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
				System.out.println("error stmt.close();");
			}
			try {	
				conn.close();
			} catch (Exception e) {
				System.out.println("error conn.close();");
			}
		}
		return result;		
	}

}
