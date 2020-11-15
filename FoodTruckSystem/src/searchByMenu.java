import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class searchByMenu {
	public String FoodTypeOption;
	public String FoodType;
	public String SelectedRow;
	public int TruckId;
	int size;
	int count;
	boolean IsNotNumeric;
	boolean IsLoopNeeded;

	public int searchMenu() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "123456789");
		Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		do {
			System.out.println("Please enter your choice of Food Type");
			System.out.println("Please enter 1 for German");
			System.out.println("Please enter 2 for Indian");
			System.out.println("Please enter 3 for Chinese");
			System.out.println("Please enter 4 for Continental");
			FoodTypeOption = Sc.nextLine();
			if (FoodTypeOption.matches("\\d+")) {
				IsNotNumeric = false;
			} else {
				System.out.println("You have entered a non numeric number");
			}
			switch (FoodTypeOption) {
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
				System.out.println("Please enter a valid input from below");
				IsNotNumeric = true;
				break;
			}
		} while (IsNotNumeric);
		try {
			stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("Select truck_owner_tab.TruckName, "
					+ "truckmenu_tab.FoodType, truckmenu_tab.Dish, truckmenu_tab.Price, truck_owner_tab.TruckId "
					+ "From truck_owner_tab "
					+ "inner join truckmenu_tab on truck_owner_tab.TruckId = truckmenu_tab.TruckId "
					+ "where FoodType = '" + FoodType + "' and ApprovedFlag = '1'");
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
