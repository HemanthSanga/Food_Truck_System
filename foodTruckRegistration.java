import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.mysql.cj.jdbc.result.ResultSetImpl;

public class foodTruckRegistration {
	public int TruckId = 0;
	public int TruckNameCount;
	public int BulkOrderFlag;
	public String TruckName;
	public String FromDate;
	public String ToDate;
	public String DaysOption;
	public String BulkOption;
	public String Days;
	public String TimeOption;
	public String TimeSlot;
	public String TimeSlot1;
	public String TimeSlot2;
	public String TimeSlot3;
	public String Location;
	public String Location1;
	public String Location2;
	public String Location3;
	public String KeyEntry;
	boolean IsNotValid;
	boolean IsNotNumeric;
	boolean IsLoopNeeded;
	boolean MinEntryMade = false;

	public void truckRegistration() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "123456789");
		PreparedStatement preparedStmt;
		java.sql.Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		do {
			IsLoopNeeded = false;
			System.out.println("Please enter your Food Truck Name");
			TruckName = Sc.nextLine();
			TruckName = TruckName.replaceAll("'", "");
			TruckName = TruckName.replaceAll("\"", "");
			if (TruckName.trim().isEmpty()) {
				IsLoopNeeded = true;
				System.out.println("Truck Name cannot be empty");
			}
			ResultSet rs = stmt.executeQuery("Select * from truck_owner_tab where TruckName = '" + TruckName + "'");
			while (((ResultSetImpl) rs).next())
				TruckNameCount++;
			if (TruckNameCount > 0) {
				System.out.println("Truck with same name already exist!! Please enter another name");
				TruckNameCount = 0;
				IsLoopNeeded = true;
			}
		} while (IsLoopNeeded);
		do {
			System.out.println("Does your Food Truck accepts Bulk Order?");
			System.out.println("Please enter 1 for Yes");
			System.out.println("Please enter 2 for No");
			BulkOption = Sc.nextLine();
			if (BulkOption.matches("\\d+")) {
				IsNotNumeric = false;
			} else {
				System.out.println("You have entered a non numeric number");
				IsNotNumeric = true;
			}
			switch (BulkOption) {
			case "1":
				BulkOrderFlag = 1;
				break;
			case "2":
				BulkOrderFlag = 0;
				break;
			default:
				System.out.println("Please enter a valid input from below");
				IsNotNumeric = true;
				break;
			}
		} while (IsNotNumeric);
		String Query = "Insert into truck_owner_tab "
				+ "(`LoginType`,`LoginId`, `TruckName`, `ApprovedFlag`, `BulkOrderFlag`) " + "VALUES (?,?,?,?,?)";
		preparedStmt = con.prepareStatement(Query);
		preparedStmt.setString(1, "O");
		preparedStmt.setString(2, "rahulanrahul");
		preparedStmt.setString(3, TruckName);
		preparedStmt.setInt(4, 0);
		preparedStmt.setInt(5, BulkOrderFlag);
		preparedStmt.execute();

		ResultSet rs = stmt.executeQuery(
				"Select * from truck_owner_tab where LoginType = 'O' and LoginId = 'rahulanrahul' and TruckName = '"
						+ TruckName + "'");
		while (((ResultSetImpl) rs).next())
			TruckId = ((ResultSetImpl) rs).getInt(1);
		do {
			System.out.println("Please enter From Date:(YYYY-MM-DD)");
			FromDate = Sc.nextLine();
			IsNotValid = false;
			if (FromDate.contains(" ")) {
				IsNotValid = true;
				System.out.println("No spaces allowed. Please try again!!!");
				continue;
			}
			DateFormat FromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			FromDateFormat.setLenient(false);
			try {
				FromDateFormat.parse(FromDate);
			} catch (ParseException ex) {
				System.out.println("Date " + FromDate + " is not valid");
				IsNotValid = true;
			}
		} while (IsNotValid);
		do {
			System.out.println("Please enter To Date:(YYYY-MM-DD)");
			ToDate = Sc.nextLine();
			IsNotValid = false;
			if (ToDate.contains(" ")) {
				IsNotValid = true;
				System.out.println("No spaces allowed. Please try again!!!");
				continue;
			}
			try {
				SimpleDateFormat ToDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				ToDateFormat.setLenient(false);
				ToDateFormat.parse(FromDate);
				Date FromDateParse = ToDateFormat.parse(FromDate);
				Date ToDateParse = ToDateFormat.parse(ToDate);
				if (FromDateParse.after(ToDateParse)) {
					System.out.println("The ToDate must be greater than FromDate");
					IsNotValid = true;
				}
			} catch (ParseException ex) {
				System.out.println("Date " + ToDate + " is not valid");
				IsNotValid = true;
			}
		} while (IsNotValid);
		do {
			do {
				System.out.println("Please enter your Days of operation");
				System.out.println("Please enter 1 for Sunday");
				System.out.println("Please enter 2 for Monday");
				System.out.println("Please enter 3 for Tuesday");
				System.out.println("Please enter 4 for Wednesday");
				System.out.println("Please enter 5 for Thursday");
				System.out.println("Please enter 6 for Friday");
				System.out.println("Please enter 7 for Saturday");
				DaysOption = Sc.nextLine();
				if (DaysOption.matches("\\d+")) {
					IsNotNumeric = false;
				} else {
					System.out.println("You have entered a non numeric number");
				}
				switch (DaysOption) {
				case "1":
					Days = "Sunday";
					break;
				case "2":
					Days = "Monday";
					break;
				case "3":
					Days = "Tuesday";
					break;
				case "4":
					Days = "Wednesday";
					break;
				case "5":
					Days = "Thursday";
					break;
				case "6":
					Days = "Friday";
					break;
				case "7":
					Days = "Saturday";
					break;
				default:
					System.out.println("Please enter a valid input from below");
					IsNotNumeric = true;
					break;
				}
			} while (IsNotNumeric);
			do {
				do {
					System.out.println("Please enter your desired timeslot:");
					System.out.println("Please enter 1 for slot 06:00 to 10:00");
					System.out.println("Please enter 2 for slot 12:00 to 16:00");
					System.out.println("Please enter 3 for slot 18:00 to 22:00");
					TimeOption = Sc.nextLine();
					if (TimeOption.matches("\\d+")) {
						IsNotNumeric = false;
					} else {
						System.out.println("You have entered a non numeric number");
					}
					switch (TimeOption) {
					case "1":
						TimeSlot = "06:00 to 10:00";
						break;
					case "2":
						TimeSlot = "12:00 to 16:00";
						break;
					case "3":
						TimeSlot = "18:00 to 22:00";
						break;
					default:
						System.out.println("Please enter a valid input from below");
						IsNotNumeric = true;
					}
				} while (IsNotNumeric);
				if (TimeSlot == TimeSlot1 || TimeSlot == TimeSlot2) {
					System.out.println("Time Slot already selected before.");
					IsLoopNeeded = true;
					continue;
				}
				do {
					IsNotValid = false;
					System.out.println("Please enter your desired location for this timeslot:");
					Location = Sc.nextLine();
					if (Location.contains(" ")) {
						IsNotValid = true;
						System.out.println("No spaces allowed. Please try again!!!");
					}
				} while (IsNotValid);
				if (TimeSlot1 == null) {
					TimeSlot1 = TimeSlot;
					TimeSlot = "";
					Location1 = Location;
					Location = "";
				} else if (TimeSlot2 == null) {
					TimeSlot2 = TimeSlot;
					TimeSlot = "";
					Location2 = Location;
					Location = "";
				} else {
					TimeSlot3 = TimeSlot;
					TimeSlot = "";
					Location3 = Location;
					Location = "";
					break;
				}
				Location = Location.replaceAll("'", "");
				Location = Location.replaceAll("\"", "");
				do {
					IsNotValid = false;
					System.out.println("Please enter 1 for more slots. " + "Press any other key to continue");
					KeyEntry = Sc.nextLine();
					if (KeyEntry.contains(" ")) {
						IsNotValid = true;
						System.out.println("No spaces allowed. Please try again!!!");
					}
				} while (IsNotValid);
				switch (KeyEntry) {
				case "1":
					IsLoopNeeded = true;
					break;
				default:
					IsLoopNeeded = false;
					break;
				}
			} while (IsLoopNeeded);
			Query = "";
			Query = "Insert Into food_truck_tab "
					+ "(`TruckId`,`FromDate`, `ToDate`, `Days`, `TimeSlot1`, `TimeSlot2`, `TimeSlot3`, "
					+ "`Location1`, `Location2`, `Location3`) " + "VALUES (?,?,?,?,?,?,?,?,?,?)";
			preparedStmt = con.prepareStatement(Query);
			preparedStmt.setInt(1, TruckId);
			preparedStmt.setString(2, FromDate);
			preparedStmt.setString(3, ToDate);
			preparedStmt.setString(4, Days);
			preparedStmt.setString(5, TimeSlot1);
			preparedStmt.setString(6, TimeSlot2);
			preparedStmt.setString(7, TimeSlot3);
			preparedStmt.setString(8, Location1);
			preparedStmt.setString(9, Location2);
			preparedStmt.setString(10, Location3);
			try {
				preparedStmt.execute();
				MinEntryMade = true;
			} catch (SQLException ex) {
				System.out.println("Sorry your data is not saved. Please try again.");
			}
			do {
				IsNotValid = false;
				System.out.println("Please press 1 to make an entry. Press any other key to exit");
				KeyEntry = Sc.nextLine();
				if (KeyEntry.contains(" ")) {
					IsNotValid = true;
					System.out.println("No spaces allowed. Please try again!!!");
				}
			} while (IsNotValid);
			switch (KeyEntry) {
			case "1":
				TimeSlot1 = "";
				TimeSlot2 = "";
				TimeSlot3 = "";
				Location1 = "";
				Location2 = "";
				Location3 = "";
				IsLoopNeeded = true;
				break;
			default:
				IsLoopNeeded = false;
				if (MinEntryMade == false) {
					preparedStmt = con.prepareStatement("Delete from truck_owner_tab WHERE TruckId = ?");
					preparedStmt.setInt(1, TruckId);
					preparedStmt.executeUpdate();
				}
				con.close();
				Sc.close();
				break;
			}
		} while (IsLoopNeeded);
	}
}