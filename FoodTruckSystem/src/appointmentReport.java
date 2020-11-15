import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class appointmentReport {
	public static String CustomDateForReport;
	public static String AppointmentDate;
	boolean IsNotValid;
	static int TruckId;

	public static void main(String[] args) throws SQLException, ClassNotFoundException, JRException {
		String AppointmentDate;
		boolean IsNotValid;
		int TruckId;
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema?useSSL=false",
				"root", "lenovopc.3");
		Scanner Sc = new Scanner(System.in);
		TruckId = 1;
		do {
			System.out.println("Please enter Date to Search:(YYYY-MM-DD)");
			AppointmentDate = Sc.nextLine();
			IsNotValid = false;
			if (AppointmentDate.contains(" ")) {
				IsNotValid = true;
				System.out.println("No spaces allowed. Please try again!!!");
				continue;
			}
			DateFormat FromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			FromDateFormat.setLenient(false);
			try {
				FromDateFormat.parse(AppointmentDate);
			} catch (ParseException ex) {
				System.out.println("Date " + AppointmentDate + " is not valid");
				IsNotValid = true;
			}
		} while (IsNotValid);
		ResultSet resultSet = con.prepareStatement(
				"SELECT CustomerId, Timeslot, Location, Date FROM customer_appointment_tab WHERE TruckId = '" + TruckId
						+ "' AND Date = '" + AppointmentDate + "'")
				.executeQuery();
		FastReportBuilder fastReportBuilder = new FastReportBuilder();
		DynamicReport dynamicReport = fastReportBuilder
				.addColumn("Customer Id", "CustomerId", String.class.getName(), 20)
				.addColumn("Time Slot", "TimeSlot", String.class.getName(), 20)
				.addColumn("Location", "Location", String.class.getName(), 20)
				.addColumn("Date", "Date", String.class.getName(), 20).setTitle("Appointment List")
				.setSubtitle("Report Generated at " + new Date()).setPrintBackgroundOnOddRows(true)
				.setUseFullPageWidth(true).build();
		JRResultSetDataSource jrDataSource = new JRResultSetDataSource(resultSet);
		JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(),
				jrDataSource);
		JasperViewer.viewReport(jasperPrint);
	}
}
