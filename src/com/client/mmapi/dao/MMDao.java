package com.client.mmapi.dao;
import java.sql.*;

public class MMDao {
	
	Connection con;
	
	
	public MMDao(){	
		try {
			//1. Get a connection to database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mmapi_schema","mmapi_admin", "MmapiAdmin!408");

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
			
			prepareStatement.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
