package sample;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

 public class ApprovalofFoodTruck {
	 
	 public static String LoginId;
	 public static int TruckId;
	 public static String TruckName;
	 public static int ApprovedFlag;
	 private static String Sql2;
	 public int i;
	 public int[] arrayofApproval =  new int[9999];
	 public int[] truck = new int[9999];
	String[] login = new String[9999];

	 public void Approval() throws ClassNotFoundException, SQLException {
		
		try {
		 Scanner Sc = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","June1405");
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM truck_owner_tab where ApprovedFlag = 0;";
			ResultSet  resultSet = stmt.executeQuery(sql);
		 
			while(resultSet.next())
			{
				i = i + 1;
				LoginId = resultSet.getString("LoginId");
				TruckId = resultSet.getInt("TruckId");
				TruckName = resultSet.getString("TruckName");
				 System.out.println(" LoginId : " +LoginId+ "\tTruckId :"+TruckId+ "\tTruckName :"+TruckName);
				 ApprovedFlag = Sc.nextInt();
				 arrayofApproval[i] =  ApprovedFlag;
				 login[i] = LoginId;
				 truck[i] = TruckId;
			}
							 
			 for(int j = 0; j <= i; j++ ) 
			 {
				 Sql2 = "UPDATE truck_owner_tab SET ApprovedFlag = '"+ arrayofApproval[j]+"' where LoginId ='"+ login[j]+"' and TruckId ='"+ truck[j]+"' ";
	        		stmt.executeUpdate(Sql2);	 
			 }
						
			System.exit(0);
		 }	
		catch(Exception e)  {
			 e.printStackTrace();
		}
	 
 }
	 
	 }



	
	
