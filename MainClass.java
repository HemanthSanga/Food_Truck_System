package ControlFlow;

import java.sql.SQLException;
import java.util.*;

import Registration.*;
import Search.*;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int Option = 0;
		int RegistrationSuccessFlag = 0;
		int CharacterCheckFlag;
		System.out.println("\t================================ ");
		System.out.println("  \t WelCome To Food Truck System");
		System.out.println("\t================================ ");

		do {
			do {
				System.out.println("\n\t***Select From Following Option***");
				System.out.println("\t1. Register");
				System.out.println("\t2. Login");
				System.out.println("\t3. Search Food Truck By Date and Time");
				System.out.println("\t0. Exit");
				Scanner Sc = new Scanner(System.in);

				try {
					Option = Sc.nextInt();
					CharacterCheckFlag = 0;
				} catch (InputMismatchException Ex) {
					System.out.println("Character's Not Allowed");
					CharacterCheckFlag = 1;

				}
			} while (CharacterCheckFlag == 1);
			switch (Option) {

			case 1:
				Registration Register = new Registration();
				try {
					RegistrationSuccessFlag = Register.UserRegistration();
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					
					
				}
				break;
			case 2:
				LoginWithVerification LoginPage = new LoginWithVerification();
				try {
					LoginPage.Login();
					System.out.println("\n\tWelcome : " + LoginPage.FirstNameFromDB);
					if(LoginPage.LoginType.equalsIgnoreCase("A")){
						ApprovalOfFoodTruck Approve =new ApprovalOfFoodTruck();
							Approve.Approval();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} 
				break;
			case 3:
				SearchByDateTime SBDT = new SearchByDateTime();
				SBDT.searchTime();
				break;
			case 0:
				System.out.println("\t\tThank You...Visit Again...");
				System.exit(0);
				break;

			default:
				System.out.println(" Please Tyr Again..");

			}
			
		} while (RegistrationSuccessFlag == 1);

	}
}