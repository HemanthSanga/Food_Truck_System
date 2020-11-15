import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class foodMenu {

	int TruckId;
	String FoodType;
	String Dish;
	String Price;

	public void insertFoodMenu() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "lenovopc.3");
		Statement stmt = con.createStatement();
		PreparedStatement preparedStmt;
		String choice;
		int count;
		boolean Flag;
		Scanner Sc = new Scanner(System.in);
		do {
			do {
				System.out.println("Enter the Food Type");
				System.out.println("Press 1 for German ");
				System.out.println("Press 2 for Indian");
				System.out.println("Press 3 for Chineese");
				System.out.println("Press 4 for Continental");
				choice = Sc.nextLine();
				Flag = false;
				switch (choice) {
				case "1":
					FoodType = "German";
					break;
				case "2":
					FoodType = "Indian";
					break;
				case "3":
					FoodType = "Chineese";
					break;
				case "4":
					FoodType = "Continental";
					break;
				default:
					System.out.println("Wrong Entry");
					System.out.println("Please try again");
					Flag = true;
					break;
				}
			} while (Flag);
			Flag = false;
			System.out.println("Enter the dish to be entered");
			Dish = Sc.nextLine();
			do {
				Flag = false;
				try {
					System.out.println("Enter the price for the dish");
					Price = Sc.nextLine();
					float Pr = Float.parseFloat(Price);
				} catch (NumberFormatException ex) {
					System.out.println("Invalid Amount Entered!! Please enter again..");
					Flag = true;
				}
			} while (Flag);
			// stmt = con.createStatement();
			String Query = "INSERT INTO truckmenu_tab(`TruckId`, `FoodType`,`Dish`,`Price`) VALUES (?,?,?,?)";
			preparedStmt = con.prepareStatement(Query);
			preparedStmt.setString(1, "4");
			preparedStmt.setString(2, FoodType);
			preparedStmt.setString(3, Dish);
			preparedStmt.setString(4, Price);
			preparedStmt.execute();
			System.out.println("Do you want to enter more details \nPress 1 to Add More \nPress 0 to exit");
			count = Sc.nextInt();
		} while (count == 1);
		con.close();
	}
}
