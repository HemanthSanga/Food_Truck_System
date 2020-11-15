package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;
public class UpdateTruckInfo {
	
	public static int TruckId;
	public static String foodtype;
	public static String foodtype2;
	public static String dish;
	public static String price;
	public static int no;
	public static String userchoice;
	public void Update() throws ClassNotFoundException, SQLException {
		try {
			
		      Scanner Sc = new Scanner(System.in);
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","June1405");
				PreparedStatement preparedStmt;
				Statement stmt = con.createStatement();
				System.out.println("Please provide TruckId :" );
				TruckId = Sc.nextInt();
				System.out.println("Please provide Foodtype :");
				foodtype = Sc.next();	
							
				int flag;
				do {
					System.out.println("Please press 1 to delete all the entries , press 2 to change price or delete an entry");
					userchoice = Sc.next();
					flag = 0;
				switch (userchoice) {
				
				case "1":
					preparedStmt = con.prepareStatement("Delete from truckmenu_tab where TruckId ='"+ TruckId+"' and Foodtype ='"+ foodtype+"' ");	
					preparedStmt.executeUpdate();					
					break;
				case "2":
					String sql = "SELECT * FROM truckmenu_tab where TruckId ='"+ TruckId+"' and Foodtype ='"+ foodtype+"' ";
					ResultSet  resultSet = stmt.executeQuery(sql);
					while(resultSet.next())
					{
						foodtype2 = resultSet.getString("Foodtype");
						dish = resultSet.getString("Dish");
						price = resultSet.getString("Price");
						no = no + 1;
						System.out.println( +no+ "\tFoodtype: " +foodtype2+ "\tDish :"+dish+ "\tPrice :"+price);
						
					}
					System.out.println("Please press C to change the price or press D to delete an entry");
					
					break;
				default:
					System.out.println("Wrong Entry");
					flag = 1;
					break;
				}	
				
				} while(flag > 0);
		      		        		
		
		}
		catch(Exception e)  {
			 e.printStackTrace();
		}
		}
	public void UpdateMenu() throws ClassNotFoundException, SQLException
	{
		}
	}




