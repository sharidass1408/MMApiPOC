package com.client.mmapi.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

//import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class MMDao {
	
	Connection con;
	
	
	public MMDao(){	
		try {
			//1. Get a connection to database
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mmapi_schema","mmapi_admin", "MmapiAdmin!408");
			
			//For AWS 
			con = DriverManager.getConnection("jdbc:mysql://rds-mysql-webmobilez.c0jdxqcv8cyj.us-east-2.rds.amazonaws.com:3306/sys","webmobilezAdmin", "WebmobileZ123");
			
			 //MS SQL statements
	           /* String serverName = "localhost";
	            String portNumber = "49688";
	            String databaseName = "UXI";
	            String username = "sa";
	            String password = "2305";

	            // Establish the connection.
	            SQLServerDataSource ds = new SQLServerDataSource();
	            ds.setServerName(serverName);
	            ds.setPortNumber(Integer.parseInt(portNumber));
	            ds.setDatabaseName(databaseName);
	            ds.setUser(username);
	            ds.setPassword(password);
			
	            con = ds.getConnection();*/
	            
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	//api_timestamp_data,startdate=01-01-2018,enddate=01-12-2018
	public void insertDataUsingPrepStatement(String query, Object... inputSelections) {
		
		try {
			PreparedStatement prepareStatement = con.prepareStatement(query);
			if(inputSelections != null ) {
				System.out.println(inputSelections.toString());
				for(int i= 1,j=0; i < inputSelections.length; i++,j++) {
					if(inputSelections[j] instanceof Timestamp) {
						prepareStatement.setTimestamp(i, (java.sql.Timestamp)inputSelections[j]);
					}else {
						prepareStatement.setString(i, (String)inputSelections[j]);
					}
					if(inputSelections[j]==null) {
						System.out.println(j+" - "+inputSelections[j]);
					} else {
						System.out.println(j+" - "+inputSelections[j]+" - "+inputSelections[j].getClass().getSimpleName());
					}
				}
			}
			System.out.println(prepareStatement.toString());
			
			prepareStatement.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
