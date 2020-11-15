package ChangeTruckInformation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
 public class ChangeTruckInformation {
    private static final String TimeSlot = null;
	public static String NewDetails;
	public static int TruckID;
    public static String FoodType;
    public static String Dish;
	public static String Price;
	static String choice ;
	public static void main(String[] args) throws SQLException
	{
		try{
			Scanner scanner = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system","root","desertstrom47007");
			PreparedStatement preparedStmt;
			Statement stmt = con.createStatement();
			System.out.println("Please provide TruckId :" );
			TruckID = scanner.nextInt();
			String q = "Select * from truck_offer_tab where TruckId ='"+TruckID+"'";
			preparedStmt = con.prepareStatement(q);	
			ResultSet rs = preparedStmt.executeQuery(q);
			while(rs.next())
			{
				System.out.println("Truck ID: " +TruckID+ "\n"+
									"Food Type: " +rs.getString("foodtype") + "\n"+
									"Dish: " +rs.getString("dish") + "\n"+
									"Price: " +rs.getString("price") );
           }
			boolean isloopneeded;
			do{
				System.out.println("MENU" + "\n" +
									"1. Delete" + "\n" +
									"2. Change Food Type" + "\n" +
									"3. Change Dish" + "\n" +
									"4. Change Price");
			choice = scanner.next();
		
			isloopneeded = false;
			switch(choice)
			{
				case "1": {
					
					preparedStmt = con.prepareStatement("Delete from truck_offer_tab where TruckId ='"+ TruckID+"'");	
					preparedStmt.executeUpdate();					
					break;
				}
				case "2": {
					String oldAttribute;
					System.out.println("Please enter the old Food Type :");
					oldAttribute = scanner.next();
					System.out.println("Now provide the new Food Type: ");
					FoodType = scanner.next();
					preparedStmt = con.prepareStatement("update truck_offer_tab set foodtype ='" + FoodType + "' where TruckId ='"+TruckID+"' and FoodType = '" +oldAttribute+"'");	
					preparedStmt.executeUpdate();
					System.out.println("Your Details have been sucessfully updated");
					break;
				}
				case "3": {
					String oldAttribute;
					System.out.println("Please enter the old dish:");
					oldAttribute = scanner.next();
					System.out.println("Now provide the new dish: ");
					Dish = scanner.next();
					preparedStmt = con.prepareStatement("update truck_offer_tab set dish ='" +Dish+ "' where TruckId ='"+TruckID+"' and Dish = '" +oldAttribute+"'");	
					preparedStmt.executeUpdate();
					System.out.println("Your Details have been sucessfully updated");
					break;
				}
				case "4": {
					String oldAttribute;
					System.out.println("Please enter the old Price");
					oldAttribute = scanner.next();
					System.out.println("Now provide the new Price ");
					Price = scanner.next();
					preparedStmt = con.prepareStatement("update truck_offer_tab set price ='" +TimeSlot+ "' where TruckId ='"+TruckID+"'and Price = '" +oldAttribute+"'");	
					preparedStmt.executeUpdate();
					System.out.println("Your Details have been sucessfully updated");
					break;
				
				}
				case "0":{
					System.out.println("Bye");
					break;
				}
				default:{
					isloopneeded = true;
					System.out.println("Wrong choice");
					break;
				}					
			}
		} while(isloopneeded);
	}
	catch (Exception e)
	{
  		System.err.println("Got an exception! \n" + e);
	}
}
}

