package updateoffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;
public class UpdateOffer {
	public static void main(String[] args){
		 int truckId;
	     String foodType;
	     String day;
	     String timeSlot;
		 String offer;
		 String from;
		 String to;
		 String choice ;
		 
		try{
				Scanner scanner = new Scanner(System.in);
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema","root","desertstrom47007");
				PreparedStatement preparedStmt;
				Statement stmt = con.createStatement();
				System.out.println("Please provide TruckId :" );
				truckId = scanner.nextInt();
				String q = "Select * from truck_offer_tab where TruckId ='"+truckId+"'";
				preparedStmt = con.prepareStatement(q);	
				ResultSet rs = preparedStmt.executeQuery(q);
				while(rs.next())
				{
					System.out.println("Truck ID: " +truckId+ "\n"+
										"Food Type: " +rs.getString("foodtype") + "\n"+
											"Day: " +rs.getString("day") + "\n"+
											"Timeslot: " +rs.getString("timeslot") + "\n"+
											"Offer: " +rs.getString("offer") + "\n"+
											"From Date: " +rs.getString("fromdate") + "\n"+
											"To Date: " +rs.getString("todate") );
               }
				boolean isloopneeded;
				do{
					System.out.println("MENU" + "\n" +
														"1. Delete" + "\n" +
														"2. Change Food Type" + "\n" +
														"3. Change Day" + "\n" +
														"4. Change Timeslot" + "\n" +
														"5. Change Offer" + "\n" +
														"6. Change FromDate" + "\n" +
														"7. Change ToDate" + "\n" +
														"0. Exit");
				choice = scanner.next();
			
				isloopneeded = false;
				switch(choice)
				{
					case "1": {
						
						preparedStmt = con.prepareStatement("Delete from truck_offer_tab where TruckId ='"+ truckId+"'");	
						preparedStmt.executeUpdate();					
						break;
					}
					case "2": {
						String oldAttribute;
						System.out.println("Please enter the old Food Type :" );
						oldAttribute = scanner.next();
						System.out.println("Now provide the new Food Type: " );
						foodType = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set foodtype ='" + foodType + "' where TruckId ='"+truckId+"' and FoodType = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
						break;
					}
					case "3": {
						String oldAttribute;
						System.out.println("Please enter the old day:" );
						oldAttribute = scanner.next();
						System.out.println("Now provide the new day: " );
						day = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set day ='" + day + "' where TruckId ='"+truckId+"' and Day = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
						break;
					}
					case "4": {
						String oldAttribute;
						System.out.println("Please enter the old Timeslot:(00-00)" );
						oldAttribute = scanner.next();
						System.out.println("Now provide the new Timeslot:(00-00) " );
						timeSlot = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set timeslot ='" + timeSlot + "' where TruckId ='"+truckId+"'and TimeSlot = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
						break;
					}
					case "5": {
						String oldAttribute;
						System.out.println("Please enter the old Offer");
						oldAttribute = scanner.next();
						System.out.println("Now provide the new Offer");
						offer = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set offer ='" + offer + "' where TruckId ='"+truckId+"'and Offer = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
						break;
					}
					case "6": {
						String oldAttribute;
						System.out.println("Please enter the old From Date:(YYYY-MM-DD)");
						oldAttribute = scanner.next();
						System.out.println("Now provide new From Date:(YYYY-MM-DD) " );
						from = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set fromdate ='" + from + "' where TruckId ='"+truckId+"'and FromDate = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
						break;
					}
					case "7": {
						String oldAttribute;
						System.out.println("Please enter the old to Date:(YYYY-MM-DD)");
						oldAttribute = scanner.next();
						System.out.println("Now provide new To Day:(YYYY-MM-DD) " );
						to = scanner.next();
						preparedStmt = con.prepareStatement("update truck_offer_tab set todate ='" + to + "' where TruckId ='"+truckId+"'and ToDate = '" +oldAttribute+"'");	
						preparedStmt.executeUpdate();
						System.out.println("Your Details have been sucessfully updated" );
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
		catch (Exception e){
      		System.err.println("Got an exception! \n" + e);
		}
	}
}

