package FeedBack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GiveFeedback {

	public int OverallExperience;
	public int Satisfaction;
	public int recommend;
	public String AnySuggestions;

	public int CustomerFeedback(int selectedTruck) throws ClassNotFoundException, SQLException {
		int TruckId = selectedTruck;
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_system", "root",
				"Abhi08338@");
		java.sql.Statement stmt = con.createStatement();
		PreparedStatement preparedStmt;
		Scanner Sc = new Scanner(System.in);

		System.out
				.println("Dear Customer, We Value Your Feedback. Please Rate Your Experience Using The Below Options");

		do {
			System.out.println("Overall Experience (1 to 5)");
			OverallExperience = Sc.nextInt();

			if ((OverallExperience <= 0) || (OverallExperience > 5)) {
				System.out.println("Invalid Input. Number Should be between 1-5");
			}
		} while ((OverallExperience <= 0) || (OverallExperience > 5));

		do {
			System.out.println("satisfaction (1 to 5)");
			Satisfaction = Sc.nextInt();

			if ((Satisfaction <= 0) || (Satisfaction > 5)) {
				System.out.println("Invalid Input. Number Should be between 1-5");
			}
		} while ((Satisfaction <= 0) || (Satisfaction > 5));

		do {
			System.out.println("How Would You Recommend To Others (1 to 5)");
			recommend = Sc.nextInt();

			if ((recommend <= 0) || (recommend > 5)) {
				System.out.println("Invalid Input. Number Should be between 1-5");
			}
		} while ((recommend <= 0) || (recommend > 5));

		System.out.println("AnySuggestions");
		AnySuggestions = Sc.next();

		String Query = "INSERT INTO feedback_table (`TruckId`,`overall_experience`, `satisfaction`, `recommend`, `any_suggestion`) VALUES (?,?,?,?,?)";

		preparedStmt = con.prepareStatement(Query);
		preparedStmt.setInt(1, TruckId);
		preparedStmt.setInt(2, OverallExperience);
		preparedStmt.setInt(3, Satisfaction);
		preparedStmt.setInt(4, recommend);
		preparedStmt.setString(5, AnySuggestions);
		preparedStmt.execute();
		con.close();
		System.out.println("Thank You For Your Valuable Feedback");
		return 1;

	}
}
