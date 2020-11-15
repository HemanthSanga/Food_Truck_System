package menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UpdateTruckMenu {

	public static int TruckId;
	static String TruckName;
	public static String foodtype;
	public static String foodtype2;
	public static String dish;
	public static float price;
	public static int no;
	public static String userchoice;
	public static String userchoice2;
	public int i;
	String[] arrayfoodtype = new String[9999];
	String[] arraydish = new String[9999];
	public float[] arrayprice = new float[9999];

	public int UpdateFoodMenu() throws ClassNotFoundException, SQLException {
		try {
			Scanner Sc = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
					"Abhi08338@");
			PreparedStatement preparedStmt;
			Statement stmt = con.createStatement();

			int flag;
			int flagsql;
			do {
				System.out.println(
						"Please press 1 to delete all the entries , press 2 to change price or delete an entry");
				userchoice = Sc.next();
				System.out.println("Please provide Truck Name :");
				TruckName = Sc.next();
				String GetTruckId = "SELECT TruckId FROM truck_owner_tab where TruckName ='" + TruckName + "'";
				ResultSet resultOfTruckId = stmt.executeQuery(GetTruckId);

				while (resultOfTruckId.next()) {
					TruckId = resultOfTruckId.getInt("TruckId");

				}
				System.out.println("Please provide Foodtype :");
				foodtype = Sc.next();

				flag = 0;
				switch (userchoice) {

				case "1":
					preparedStmt = con.prepareStatement("Delete from truckmenu_tab where TruckId ='" + TruckId
							+ "' and Foodtype ='" + foodtype + "' ");
					preparedStmt.executeUpdate();
					System.out.println("Delete Was Processed Successfully");
					break;
				case "2":

					do {
						flagsql = 0;
						String sql = "SELECT * FROM truckmenu_tab where TruckId ='" + TruckId + "' and Foodtype ='"
								+ foodtype + "' ";
						ResultSet resultSet = stmt.executeQuery(sql);

						if (resultSet.next()) {
							do {
								i = i + 1;
								foodtype2 = resultSet.getString("Foodtype");
								dish = resultSet.getString("Dish");
								price = resultSet.getFloat("Price");
								arrayfoodtype[i] = resultSet.getString("Foodtype");
								arraydish[i] = resultSet.getString("Dish");
								no = no + 1;
								System.out.println(
										+no + "\tFoodtype: " + foodtype2 + "\tDish :" + dish + "\tPrice :" + price);

							} while (resultSet.next());
						} else {
							System.out.println("Please provide correct Truck Name :");
							TruckName = Sc.next();
							String GetTruckIdForIncorrectOpt = "SELECT TruckId FROM truck_owner_tab where TruckName ='"
									+ TruckName + "'";
							ResultSet resultOfTruckIdForIncorrectOpt = stmt.executeQuery(GetTruckIdForIncorrectOpt);

							while (resultOfTruckIdForIncorrectOpt.next()) {
								TruckId = resultOfTruckIdForIncorrectOpt.getInt("TruckId");

							}

							System.out.println("Please provide correct Foodtype :");
							foodtype = Sc.next();
							flagsql = 1;
						}

					} while (flagsql > 0);

					System.out.println("\nPlease press C to change the price or press D to delete an entry");
					this.UpdateMenu();
					// System.out.println("\n\t Delete Was Processed Successfully");
					break;
				default:
					System.out.println("Wrong Entry...Enter Again");
					flag = 1;
					break;
				}

			} while (flag > 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void UpdateMenu() throws ClassNotFoundException, SQLException {
		Scanner Sc = new Scanner(System.in);
		String Sql2;
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
				"Abhi08338@");
		PreparedStatement preparedStmt;
		int flag;
		do {
			userchoice2 = Sc.next();
			flag = 0;
			switch (userchoice2) {
			case "C":
				System.out.println("Please provide the entry number");
				i = Sc.nextInt();
				System.out.println("Please provide the updated price");
				arrayprice[i] = Sc.nextFloat();
				Statement stmt = con.createStatement();
				Sql2 = "UPDATE truckmenu_tab SET Price = '" + arrayprice[i] + "' where TruckId ='" + TruckId
						+ "' and Foodtype ='" + arrayfoodtype[i] + "' and Dish ='" + arraydish[i] + "'  ";
				System.out.println("Updated Request Was Processed Successfully");
				stmt.executeUpdate(Sql2);
				break;
			case "D":
				System.out.println("Please provide the entry number");
				i = Sc.nextInt();
				preparedStmt = con.prepareStatement("Delete from truckmenu_tab where TruckId ='" + TruckId
						+ "' and Foodtype ='" + arrayfoodtype[i] + "' and Dish ='" + arraydish[i] + "' ");
				preparedStmt.executeUpdate();
				System.out.println("Delete Request Was Processed Successfully");
				break;

			default:
				System.out.println("Wrong Entry...Enter Again");
				flag = 1;
				break;
			}
		} while (flag > 0);

	}

}
