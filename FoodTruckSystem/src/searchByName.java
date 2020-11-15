import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class searchByName {
	public String TruckName;
	boolean IsLoopNeeded;
	int size;
	int TruckId;

	public int searchName() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema", "root",
				"123456789");
		Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		do {
			IsLoopNeeded = false;
			System.out.println("Please enter your desired Food Truck Name to Search");
			TruckName = Sc.nextLine();
			TruckName = TruckName.replaceAll("'", "");
			TruckName = TruckName.replaceAll("\"", "");
			if (TruckName.trim().isEmpty()) {
				IsLoopNeeded = true;
				System.out.println("Truck Name cannot be empty");
			}
		} while (IsLoopNeeded);
		try {
			stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("Select truck_owner_tab.TruckName, "
					+ "food_truck_tab.FromDate, food_truck_tab.ToDate, food_truck_tab.Days, food_truck_tab.TimeSlot1, "
					+ "food_truck_tab.Location1, food_truck_tab.TimeSlot2, food_truck_tab.Location2, "
					+ "food_truck_tab.TimeSlot3, food_truck_tab.Location3, truck_owner_tab.TruckId From truck_owner_tab "
					+ "inner join food_truck_tab on truck_owner_tab.TruckId = food_truck_tab.TruckId "
					+ "where TruckName = '" + TruckName + "' and ApprovedFlag = '1'");

			int nCol = result.getMetaData().getColumnCount();
			List<String[]> table = new ArrayList<>();
			while (result.next()) {
				size = nCol;
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					Object obj = result.getObject(iCol);
					row[iCol - 1] = (obj == null) ? null : obj.toString();
				}
				table.add(row);
			}
			if (!table.isEmpty()) {
				int i = 0;
				for (String[] row : table) {
					for (String s : row) {
						i++;
						if (i != (size)) {
							if (s != null) {
								System.out.print(s + " ");
							}
						}
					}
					i = 0;
					System.out.println();
					System.out.println();
				}
				String FirstRow[] = table.get(0);
				TruckId = Integer.parseInt(FirstRow[size - 1]);
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
