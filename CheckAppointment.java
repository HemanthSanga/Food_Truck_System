package BookAndCheckAppointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.result.ResultSetImpl;

public class CheckAppointment {
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

	public String CustomerId = "1";
	public int BookId;
	public String TimeSlot;
	public int TruckId = 1;
	public String Location;
	public String DateValue;
	public String Date;
	public int BulkOrderId;
	public String NoOfPerson;
	public String Dish;

	public int ViewAppointmentdata() throws ClassNotFoundException, SQLException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
				"Abhi08338@");
		java.sql.Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM customer_appointment_tab where TruckId ='" + TruckId + "'");
		// System.out.println(" CustomerId BookId TimeSlot Location Date BulkOrderId
		// NoOfPerson Dish");
		while (((ResultSetImpl) rs).next()) {
			if (((ResultSetImpl) rs).getString(1) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(1)));
			}
			if (((ResultSetImpl) rs).getString(2) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(2)));
			}
			if (((ResultSetImpl) rs).getString(3) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(3)));
			}
			if (((ResultSetImpl) rs).getString(4) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(4)));
			}
			if (((ResultSetImpl) rs).getString(5) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(5)));
			}
			if (((ResultSetImpl) rs).getString(6) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(6)));
			}
			if (((ResultSetImpl) rs).getString(7) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(7)));
			}
			if (((ResultSetImpl) rs).getString(8) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(8)));
			}
			if (((ResultSetImpl) rs).getString(9) != null) {
				System.out.print((" \t" + ((ResultSetImpl) rs).getString(9)));
			}
			System.out.println("");
		}
		return BookId;

	}
}
