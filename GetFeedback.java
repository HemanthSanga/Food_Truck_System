package FoodTruckSystem.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.cj.jdbc.result.ResultSetImpl;

public class GetFeedback {
	public int OverallExperience;
	public int Satisfaction;
	public int HowWouldYouRecommendToOthers;
	public String AnySuggestions;
	public String Name = "abhi";
	
	public void FeedbackData () throws ClassNotFoundException, SQLException {
		
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectdata","root","Shams*97");
		java.sql.Statement stmt = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM feedback_table where Name ='"+Name+"'");
		System.out.println("overall_experience  satisfaction  how_would_you_recommed_to_others  any_suggestions      name");
		while(((ResultSetImpl) rs).next()) {
			System.out.println((" \t" + ((ResultSetImpl) rs).getInt(1))+" 	\t "+((ResultSetImpl) rs).getInt(2)+" 		\t "+((ResultSetImpl) rs).getInt(3)+" 	  	\t "+((ResultSetImpl) rs).getString(4)+"      \t "+((ResultSetImpl) rs).getString(5)+"\t");
		}
	}
}
