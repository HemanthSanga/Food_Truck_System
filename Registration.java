package Registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Registration {

	static int Flag = 0;
	public String LoginType;
	public String FirstName;
	public String LastName;
	public String EmailId;
	public String MobileNo;
	public String LoginID;
	public String Password;
	public String ExitOption;
	int CheckLoginIdFlag=0;

	public int UserRegistration() throws ClassNotFoundException, SQLException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema", "root",
				"Abhi08338@");
		PreparedStatement preparedStmt;
		Statement stmt = con.createStatement();
		Scanner Sc = new Scanner(System.in);
		do {

			System.out.println("Press C for Customer and O for Owner ");
			LoginType = Sc.nextLine().toUpperCase();

			int TempVariable;
			if (LoginType.equals("C") || LoginType.equals("O")) {
				do {
				System.out.println("Enter Your LoginID");
				LoginID = Sc.nextLine();
				
				String sql = "SELECT * FROM user_tab where LoginID ='"+LoginID+"' AND LoginType ='"+LoginType+"' ;";
				ResultSet  resultSet = stmt.executeQuery(sql);
				if(resultSet.next())
				{
					System.out.println(LoginID+" Already Exists");
					CheckLoginIdFlag = 1;
				}else
				{
					CheckLoginIdFlag=0;
				}
			}while(CheckLoginIdFlag!=0);

				System.out.println("Enter Your First Name");
				FirstName = Sc.nextLine();

				System.out.println("Enter Your Last Name");
				LastName = Sc.nextLine();
				do {
					System.out.println("Enter Your EmailId in Following Pattern : xxxxx@xxxx.com");
					EmailId = Sc.nextLine();
					TempVariable = Validation.EmailValidation(EmailId);

				} while (TempVariable == 1);

				do {
					System.out.println("Enter Your 10 Digit Mobile Number");
					MobileNo = Sc.nextLine();
					TempVariable = Validation.PhoneNumberValidation(MobileNo);

				} while (TempVariable == 1);

				System.out.println("Enter Your Password");
				Password = Sc.nextLine();

				String Query = "INSERT INTO user_tab (`LoginType`,`LoginId`,`Password`, `FirstName`, `SecondName`, `EmailId`, `MobileNumber`) VALUES (?,?,?,?,?,?,?)";

				preparedStmt = con.prepareStatement(Query);
				preparedStmt.setString(1, LoginType);
				preparedStmt.setString(2, LoginID);
				preparedStmt.setString(3, Password);
				preparedStmt.setString(4, FirstName);
				preparedStmt.setString(5, LastName);
				preparedStmt.setString(6, EmailId);
				preparedStmt.setString(7, MobileNo);

				preparedStmt.execute();
				con.close();
				System.out.println(FirstName + " Your Have Successfully Registered...");
			} else {
				System.out.println("Your Input was incorrect...Please Try Again...");
				Flag = 1;
			}

		} while (Flag != 0);
		return 1;
	}
}
