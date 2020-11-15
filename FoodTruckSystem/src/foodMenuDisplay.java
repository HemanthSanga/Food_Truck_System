import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;


public class foodMenuDisplay {
	
	String foodtype2;
	String dish;
	String price;
	int no = 0, Flag = 0, userChoice;

	public void displayFoodMenu() throws ClassNotFoundException, SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "lenovopc.3");
		java.sql.Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		TruckId = 1;
		String sql = "SELECT * FROM truckmenu_tab where TruckId =" + TruckId;
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			foodtype2 = resultSet.getString("Foodtype");
			dish = resultSet.getString("Dish");
			price = resultSet.getString("Price");
			no = no + 1;
			System.out.println(+no + "\tFoodtype: " + foodtype2 + "\tDish :" + dish + "\tPrice :" + price);
		}
	}

}
