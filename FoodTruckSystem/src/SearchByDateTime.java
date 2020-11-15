import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class searchByDateAndTime {
	String SelectedRow;
	int TruckId;
	int size;
	int count;
	boolean IsNotNumeric;
	boolean IsNotValid;
	String TimeOption;
	String Date;
	String TimeSlot;

	public int searchTime() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "123456789");
		Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		do {
			System.out.println("Please enter From Date:(YYYY-MM-DD)");
			Date = Sc.nextLine();
			IsNotValid = false;
			if (Date.contains(" ")) {
				IsNotValid = true;
				System.out.println("No spaces allowed. Please try again!!!");
				continue;
			}
			DateFormat FromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			FromDateFormat.setLenient(false);
			try {
				FromDateFormat.parse(Date);
			} catch (ParseException ex) {
				System.out.println("Date " + Date + " is not valid");
				IsNotValid = true;
			}
		} while (IsNotValid);
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

		try {
			stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("Select truck_owner_tab.TruckName, "
					+ "food_truck_tab.FromDate, food_truck_tab.ToDate, food_truck_tab.Days, food_truck_tab.TimeSlot1, "
					+ "food_truck_tab.Location1, food_truck_tab.TimeSlot2, food_truck_tab.Location2, "
					+ "food_truck_tab.TimeSlot3, food_truck_tab.Location3, truck_owner_tab.TruckId "
					+ "From truck_owner_tab "
					+ "inner join food_truck_tab on truck_owner_tab.TruckId = food_truck_tab.TruckId "
					+ "where( ( TimeSlot1 = '" + TimeSlot + "' ) OR ( TimeSlot2 = '" + TimeSlot
					+ "' ) OR ( TimeSlot3 = '" + TimeSlot + "' ) ) AND ( ( FromDate <= '" + Date
					+ "' ) AND ( ToDate >= '" + Date + "' ) ) AND ApprovedFlag = '1'");
			int nCol = result.getMetaData().getColumnCount();
			count = 0;
			List<String[]> table = new ArrayList<>();
			while (result.next()) {
				size = nCol + 1;
				String[] row = new String[size];
				for (int iCol = 0; iCol <= nCol; iCol++) {
					if (iCol == 0) {
						row[iCol] = Integer.toString(++count);
						continue;
					}
					Object obj = result.getObject(iCol);
					row[iCol] = (obj == null) ? null : obj.toString();
				}
				table.add(row);
			}
			if (!table.isEmpty()) {
				int i = 0;
				for (String[] row : table) {
					for (String s : row) {
						i++;
						if (i != (size)){
						if (s != null) {
							System.out.print(s + " ");
						}	
					}	
					}
					i = 0;
					System.out.println();
					System.out.println();
				}
				do {
					IsNotNumeric = false;
					System.out.println("Please enter Food Truck Number to select");
					SelectedRow = Sc.nextLine();
					if (!SelectedRow.matches("\\d+") || SelectedRow.contains(" ")) {
						System.out.println("You have entered an invalid number");
						IsNotNumeric = true;
					} else {
						int rowno = Integer.parseInt(SelectedRow);
						rowno--;
						if ((rowno < count) && (rowno >= 0)) {
							String Row[] = table.get(rowno);
							TruckId = Integer.parseInt(Row[size - 1]);
						} else {
							System.out.println("Select a number within the range");
							IsNotNumeric = true;
						}
					}
				} while (IsNotNumeric);
			} else {
				TruckId = 0;
				System.out.println("No Search results found.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return TruckId;
	}
}
