package FoodTruckSystem.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GiveFeedback {
		
		public int OverallExperience;
		public int Satisfaction;
		public int HowWouldYouRecommendToOthers;
		public String AnySuggestions;
		public String Name = "abhi";
		
		public void CustomerFeedback () throws ClassNotFoundException, SQLException {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectdata","root","Shams*97");
			java.sql.Statement stmt = con.createStatement();
			PreparedStatement preparedStmt;
			Scanner Sc = new Scanner(System.in);
			
			System.out.println("Dear Customer, We Value Your Feedback. Please Rate Your Experience Using The Below Options");
			
			do {
			System.out.println("Overall Experience (1 to 5)");
			 OverallExperience = Sc.nextInt();
			 
			 if((OverallExperience <= 0) || (OverallExperience>5))
			 {
				 System.out.println("Invalid Input. Number Should be between 1-5");
			 }
			}while((OverallExperience<=0) || (OverallExperience>5));
			
			
			do {
			System.out.println("satisfaction (1 to 5)");
			 Satisfaction = Sc.nextInt();
			 
			 if((Satisfaction<=0) || (Satisfaction>5))
			 {
				 System.out.println("Invalid Input. Number Should be between 1-5");
			 }
			} while ((Satisfaction<=0) || (Satisfaction>5));
			
			
			do {
			System.out.println("How Would You Recommend To Others (1 to 5)");
			 HowWouldYouRecommendToOthers = Sc.nextInt();
			 
			 if((HowWouldYouRecommendToOthers<=0) || (HowWouldYouRecommendToOthers>5))
			 {
				 System.out.println("Invalid Input. Number Should be between 1-5");
			 }
			} while ((HowWouldYouRecommendToOthers<=0) || (HowWouldYouRecommendToOthers>5));
			
			
			System.out.println("AnySuggestions");
			AnySuggestions = Sc.next();
			 
			String Query = "INSERT INTO feedback_table (`overall_experience`, `satisfaction`, `how_would_you_recommed_to_others`, `any_suggestions`, `name`) VALUES (?,?,?,?,?)";
			
			preparedStmt = con.prepareStatement(Query);
			preparedStmt.setInt(1,OverallExperience);
			preparedStmt.setInt(2,Satisfaction);
			preparedStmt.setInt(3,HowWouldYouRecommendToOthers);
			preparedStmt.setString (4,AnySuggestions);
			preparedStmt.setString(5,Name);
			
			preparedStmt.execute();
			con.close();
			
		}
}