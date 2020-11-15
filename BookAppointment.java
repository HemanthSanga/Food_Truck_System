package BookAndCheckAppointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import com.mysql.cj.jdbc.result.ResultSetImpl;

public class BookAppointment {

	public String Input;
	boolean InputMismatch;
	boolean BulkOrder;
	boolean ReplyMismatch;
	boolean Mismatch;
	public String sql;
	public String FromDate;
	public java.util.Date FromDateD;
	public String ToDate;
	public String Reply;

	public String CustomerId;
	public int BookId;
	public String TimeSlot;
	public int TruckId;
	public String Location;
	public String DateValue;
	public String Date;
	public int BulkOrderId;
	public String NoOfPerson;
	public String Dish;

	public void BookAppointmentInput(int SelectedTruck, String NameFromDB) throws ClassNotFoundException, SQLException {
		TruckId = SelectedTruck;
		CustomerId = NameFromDB;
		do {
			InputMismatch = false;
			System.out.println("Would you like to book an appointment for your order: Y or N ?");
			Scanner Sc = new Scanner(System.in);
			Input = Sc.nextLine().toUpperCase();

			if ((Input.equals("Y"))) {
				CustomerAppointment();
			} else if ((Input.equals("N"))) {
				System.exit(0);
			} else {
				System.out.println("Invalid Input, please enter either Y or N");
				InputMismatch = true;
			}
		} while (InputMismatch);

	}

	public void CustomerAppointment() throws ClassNotFoundException, SQLException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
				"Abhi08338@");
		java.sql.Statement stmt = con.createStatement();
		PreparedStatement preparedStmt;
		Scanner Sc = new Scanner(System.in);

		System.out.println("Please enter the below details to book an appointment:");

		boolean IsNotNumeric = false;
		do {
			System.out.println("Please enter your desired timeslot:");
			System.out.println("Please enter 1 for slot 06:00 to 10:00");
			System.out.println("Please enter 2 for slot 12:00 to 16:00");
			System.out.println("Please enter 3 for slot 18:00 to 22:00");
			TimeSlot = Sc.nextLine();

			if (TimeSlot.matches("\\d+")) {
				IsNotNumeric = false;
			} else {
				System.out.println("You have entered a non numeric number");
			}
			switch (TimeSlot) {
			case "1":
				TimeSlot = "06:00 to 10:00";
				String sql1 = "SELECT TimeSlot1,Location1 FROM food_truck_tab where TimeSlot1='" + TimeSlot
						+ "' AND TrucKId = '" + TruckId + "'";
				ResultSet RS1 = stmt.executeQuery(sql1);
				if (!RS1.next()) {

					System.out.println("No Truck Service Available For This TimeSlot ");
					IsNotNumeric = true;
				} else {
					Location = RS1.getString(2);
				}

				break;
			case "2":
				TimeSlot = "12:00 to 16:00";
				String sql2 = "SELECT TimeSlot2,Location2 FROM food_truck_tab where TimeSlot2='" + TimeSlot
						+ "' AND TrucKId = '" + TruckId + "'";
				ResultSet RS2 = stmt.executeQuery(sql2);
				if (!RS2.next()) {

					System.out.println("No Truck Service Available For This TimeSlot ");
					IsNotNumeric = true;
				} else {
					Location = RS2.getString(2);
				}
				break;
			case "3":
				TimeSlot = "18:00 to 22:00";
				String sql3 = "SELECT TimeSlot3,Location3 FROM food_truck_tab where TimeSlot3='" + TimeSlot
						+ "' AND TrucKId = '" + TruckId + "'";
				ResultSet RS3 = stmt.executeQuery(sql3);
				if (!RS3.next()) {

					System.out.println("No Truck Service Available For This TimeSlot ");
					IsNotNumeric = true;
				} else {
					Location = RS3.getString(2);
				}
				break;
			default:
				System.out.println("Please enter a valid input from below");
				IsNotNumeric = true;
			}
		} while (IsNotNumeric);

		boolean IsNotValid = false;
		String sql = "SELECT FromDate, ToDate FROM food_truck_tab where TruckId='" + TruckId + "'";
		ResultSet RS = stmt.executeQuery(sql);
		while (((ResultSetImpl) RS).next()) {
			FromDate = RS.getString(1);
			ToDate = RS.getString(2);
		}

		do {
			System.out.println("Enter the Date (in the format YYYY-MM-DD) from the following date range:" + FromDate
					+ " to " + ToDate + " ");
			DateValue = Sc.nextLine();
			IsNotValid = false;
			if (DateValue.contains(" ")) {
				IsNotValid = true;
				System.out.println("No spaces allowed. Please try again!!!");
				continue;
			}
			DateFormat FromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			FromDateFormat.setLenient(false);
			try {
				FromDateFormat.parse(DateValue);
				Date Dt = FromDateFormat.parse(DateValue);
				Date DateFr = FromDateFormat.parse(FromDate);
				Date DateTo = FromDateFormat.parse(ToDate);
				if (!((Dt.after(DateFr)) && (Dt.before(DateTo)))) {
					System.out.println("Date " + DateValue + " is not in Range");
					IsNotValid = true;
				}
			} catch (ParseException ex) {
				System.out.println("Date " + DateValue + " is not valid");
				IsNotValid = true;
			}
		} while (IsNotValid);

		do {
			InputMismatch = false;
			System.out.println("Do you wish to book a bulk order: Y or N ?");
			Reply = Sc.nextLine().toUpperCase();

			if ((Reply.equals("Y"))) {
				CustomerBulkOrder();
			} else if ((Reply.equals("N"))) {
				System.out.println("Thank you for booking an appointment for your order");
			} else {
				System.out.println("Invalid Input, please enter either Y or N");
				InputMismatch = true;
			}
		} while (InputMismatch);

		String Query = "INSERT INTO customer_appointment_tab (`TruckId`,`CustomerId`, `TimeSlot`,`Location`, `Date`,`BulkOrderId`, `NoOfPerson`,`Dish`) VALUES (?,?,?,?,?,?,?,?)";

		preparedStmt = con.prepareStatement(Query);

		preparedStmt.setInt(1, TruckId);
		preparedStmt.setString(2, CustomerId);
		preparedStmt.setString(3, TimeSlot);
		preparedStmt.setString(4, Location);
		preparedStmt.setString(5, DateValue);
		preparedStmt.setInt(6, BulkOrderId);
		preparedStmt.setString(7, NoOfPerson);
		preparedStmt.setString(8, Dish);

		preparedStmt.execute();
		con.close();

		System.exit(0);
	}

	public void CustomerBulkOrder() throws ClassNotFoundException, SQLException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
				"Abhi08338@");
		java.sql.Statement stmt = con.createStatement();
		PreparedStatement preparedStmt;
		Scanner Sc = new Scanner(System.in);

		do {
			ReplyMismatch = false;

			System.out.println("Please enter the number of persons for bulk order");
			NoOfPerson = Sc.nextLine();
			int result = Integer.parseInt(NoOfPerson);

			if (result <= 50) {
				ReplyMismatch = true;
				System.out.println("Number of Persons should be more than 50");
			}

		} while (ReplyMismatch);

		do {

			InputMismatch = false;
			int i = 0;
			String sql = "SELECT Dish FROM truckmenu_tab where TruckId='" + TruckId + "'";
			ResultSet RS = stmt.executeQuery(sql);
			while (RS.next()) {
				if (RS.getString(1) != null) {
					i++;
					System.out.println(" " + i + " " + RS.getString(1));
				}
			}
			System.out.println("Enter Dish From Above Option");
			Dish = Sc.nextLine();
		} while (InputMismatch);
		Random RandomNumber = new Random();
		BulkOrderId = RandomNumber.nextInt(100);
		System.out.println("Your Booking is Confirmed Pls Refer This Number for Further Communication:" + BulkOrderId);
		System.out.println("Thank you for booking an appointment for your bulk order");
	}

}
