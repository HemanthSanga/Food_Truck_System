package FoodTruckSystem.Project;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		
		/*GiveFeedback Feedback = new GiveFeedback();
		try {
			try {
				Feedback.CustomerFeedback();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		
		}*/
		
		
		
		/*GetFeedback Feedbackdata = new GetFeedback();
		try {
			try {
				Feedbackdata.FeedbackData();
			}catch (SQLException e) {
				
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
		
		
		BookAppointment UserInput = new BookAppointment();
		try {
				UserInput.CustomerInput();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			{
		
						
		BookAppointment Appointment = new BookAppointment();
		try {
			Appointment.CustomerAppointment();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();	
		}
		
		}
			
		BookAppointment BulkOrder = new BookAppointment();
		try {
			BulkOrder.CustomerBulkOrder();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();	
		}
		
		}
		
		CheckAppointment Appointmentdata = new CheckAppointment();
		try {
			Appointmentdata.Appointmentdata();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();	
		}
	}	
	
	}


