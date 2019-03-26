package com.client.mmapi.dao;
import java.sql.*;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class MMDao {
	
	Connection con;
	
	
	public MMDao(){	
		try {
			//1. Get a connection to database
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mmapi_schema","mmapi_admin", "MmapiAdmin!408");
			//con = DriverManager.getConnection("jdbc:sqlserver://localhost:49688/mmapi_schema","sa", "2305");
			/*
			String dbURL = "jdbc:sqlserver://localhost\\sqlexpress/dbo";
			String user = "sa";
			String pass = "2305";
			con = DriverManager.getConnection(dbURL, user, pass);*/
			
			 
	            String serverName = "localhost";
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
			
	            con = ds.getConnection();
	            
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void insertDataUsingPrepStatement(String query, String... inputSelections) {
		
		try {
			PreparedStatement prepareStatement = con.prepareStatement(query);
			if(inputSelections != null ) {
				for(int i=0; i <inputSelections.length; i++) {
					prepareStatement.setString(i+1, inputSelections[i]);
				}
			}
			
			prepareStatement.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
