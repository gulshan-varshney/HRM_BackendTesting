package genericUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

/**
 * 
 * @author gulshan
 *
 */
public class DataBaseUtility {

	static Connection conn = null;
	static ResultSet result = null;
	static FileUtility fLib = new FileUtility();

	/**
	 * this method will perform the mySQL Database connection
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @throws SQLException
	 */
	public void getDbConnection() throws SQLException {

		try {
			Driver driverRef = new Driver();
			DriverManager.registerDriver(driverRef);
			conn = DriverManager.getConnection(fLib.getDataFromPropertyFile("DBUrl"),
					fLib.getDataFromPropertyFile("Db_Username"), fLib.getDataFromPropertyFile("Db_pwd"));
		} catch (Exception e) {
		}
	}

	/**
	 * this method perform the disconnection from database
	 * 
	 * @throws SQLException
	 */

	/* method for close the connection */
	public void closeConnection() throws SQLException {
		try {
			conn.close();
		} catch (Exception e) {
		}
	}

	/**
	 * this method will perform select query action
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	// method for select query
	public ResultSet executeSelectQuery(String query) throws SQLException {
		ResultSet result = null;
		try {
			Statement stat = conn.createStatement();
			result = stat.executeQuery(query);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * this method will perform non select query action
	 * 
	 * @param query
	 * @return
	 */
	// method for non-select query
	public int executeNonSelectQuery(String query) {
		int result = 0;
		try {
			Statement stat = conn.createStatement();
			result = stat.executeUpdate(query);
		} catch (Exception e) {
		}
		return result;
	}
	
	/**
	 * 
	 * @param query
	 * @param columnIndex
	 * @param expectedData
	 * @return
	 * @throws Throwable
	 */
	public boolean  executeQueryVerifyAndGetData(String query ,int columnIndex , String expectedData) throws Throwable{
        boolean flag = false;
			result = conn.createStatement().executeQuery(query);
			
		while (result.next()) {
			  		if(result.getString(columnIndex).equals(expectedData)) {
			  			flag= true;
			  			break;
			  		}
		}	
		if(flag) {
			System.out.println(expectedData + "===> data verified in data base table");
			return true;
		}else {
			System.out.println(expectedData + "===> data not verified in data base table");
			return false;
		}
		
		
	}
	
}
