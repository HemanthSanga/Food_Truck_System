import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class foodOffer {

	int TruckId = 1;
	String FoodType;
	String Day;
	String TimeSlot;
	String Offer;
	String FromDate;
	String ToDate;
	String choice;
	String FoodTypeChoice, DayChoice;
	int i = 0;

	boolean Flag, IsNotNumeric, Flag2, Flag3;

	public void foodOffer() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false","root", "lenovopc.3");
		Statement stmt = con.createStatement();
		PreparedStatement preparedStmt;
		Scanner Sc = new Scanner(System.in);

		ResultSet resultTruck = stmt.executeQuery(
				"SELECT food_truck_tab.FromDate, food_truck_tab.ToDate, food_truck_tab.Days, food_truck_tab.TimeSlot1, food_truck_tab.TimeSlot2, food_truck_tab.TimeSlot3 "
						+ "FROM food_truck_tab " + "WHERE food_truck_tab.TruckId = '" + TruckId + "'");

		while (resultTruck.next()) {
			FromDate = resultTruck.getString(1);
			ToDate = resultTruck.getString(2);
		}
		do {
			Flag = false;
			do {
				System.out.println("Enter the Day");
				System.out.println("Press 1 for Monday ");
				System.out.println("Press 2 for Tuesday");
				System.out.println("Press 3 for Wednesday");
				System.out.println("Press 4 for Thursday");
				System.out.println("Press 5 for Friday");
				System.out.println("Press 6 for Saturday");
				System.out.println("Press 7 for Sunday");
				DayChoice = Sc.next();
				Flag3 = false;
				switch (DayChoice) {
				case "1":
					Day = "Monday";
					break;
				case "2":
					Day = "Tuesday";
					break;
				case "3":
					Day = "Wednesday";
					break;
				case "4":
					Day = "Thursday";
					break;
				case "5":
					Day = "Friday";
					break;
				case "6":
					Day = "Saturday";
					break;
				case "7":
					Day = "Sunday";
					break;
				default:
					System.out.println("Wrong Entry");
					System.out.println("Please give a correct input...!!");
					Flag3 = true;
					break;
				}
			} while (Flag3);

			do {
				Flag = false;
				System.out.println("Choose the time slot..");
				System.out.println("Press 1 for 6-10");
				System.out.println("Press 2 for 12-15");
				System.out.println("Press 3 for 18-22");
				choice = Sc.next();
				switch (choice) {
				case "1":
					TimeSlot = "6:00-10:00";
					break;
				case "2":
					TimeSlot = "12:00-16:00";
					break;
				case "3":
					TimeSlot = "18:00-22:00";
					break;
				default: {
					System.out.println("Wrong Entry..");
					System.out.println("Please give a correct input...!!");
					Flag = true;
					break;
				}
				}
			} while (Flag);

			ResultSet resultQuery = stmt.executeQuery("SELECT * FROM food_truck_tab WHERE food_truck_tab.TruckId = '"
					+ TruckId + "' AND FromDate = '" + FromDate + "' AND ToDate = '" + ToDate + "' AND Days = '" + Day
					+ "' AND  ( ( TimeSlot1 = '" + TimeSlot + "' ) OR ( TimeSlot2 = '" + TimeSlot
					+ "' ) OR ( TimeSlot3 = '" + TimeSlot + "' ) )");

			if (!(resultQuery.next())) {
				Flag = true;
				System.out.println("No matching Days and timeslots available. Please enter again.");
			}
		} while (Flag);

		do {
			do {
				Flag = false;
				Flag2 = false;
				System.out.println("Enter the Food Type");
				System.out.println("Press 1 for German ");
				System.out.println("Press 2 for Indian");
				System.out.println("Press 3 for Chinese");
				System.out.println("Press 4 for Continental");
				FoodTypeChoice = Sc.next();
				switch (FoodTypeChoice) {
				case "1":
					FoodType = "German";
					break;
				case "2":
					FoodType = "Indian";
					break;
				case "3":
					FoodType = "Chinese";
					break;
				case "4":
					FoodType = "Continental";
					break;
				default:
					System.out.println("Wrong Entry");
					System.out.println("Please give a correct input...!!");
					Flag2 = true;
					break;
				}
			} while (Flag2);

			ResultSet resultMenu = stmt.executeQuery("SELECT FoodType FROM truckmenu_tab WHERE TruckId = '" + TruckId
					+ "' AND FoodType = '" + FoodType + "'");

			if (!(resultMenu.next())) {
				Flag = true;
				System.out.println("No matching FoodTypes available. Please enter again.");
			}
		} while (Flag);

		do {
			Flag = false;
			try {
				System.out.println("Enter the Offer percentage for the Food Type");
				Offer = Sc.next();
				float Pr = Float.parseFloat(Offer);
			} catch (NumberFormatException ex) {
				System.out.println("Wrong Entry...");
				System.out.println("Please enter again..");
				Flag = true;
			}
		} while (Flag);

		String Query = "INSERT INTO truck_offer_tab (`TruckId`,`FoodType`,`Day`,`TimeSlot`,`Offer`,`FromDate`,`ToDate`) VALUES (?,?,?,?,?,?,?)";
		preparedStmt = con.prepareStatement(Query);
		preparedStmt.setInt(1, TruckId);
		preparedStmt.setString(2, FoodType);
		preparedStmt.setString(3, Day);
		preparedStmt.setString(4, TimeSlot);
		preparedStmt.setString(5, Offer);
		preparedStmt.setString(6, FromDate);
		preparedStmt.setString(7, ToDate);
		preparedStmt.execute();
		con.close();

	}

}
