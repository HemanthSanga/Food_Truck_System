package Registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LoginWithVerification {
	public String EnteredName;
	String Enteredpassword;
	public String FirstNameFromDB = null;
	String PasswordFromDB = null;
	String NameFromDB = null;
	public String LoginType;

	public LoginWithVerification() {

	}

	public LoginWithVerification(String FirstNameFromDB, String LoginType) {
		this.FirstNameFromDB = FirstNameFromDB;
		this.LoginType = LoginType;
	}

	public LoginWithVerification Login() throws SQLException {
		Scanner Sc = new Scanner(System.in);

		do {
			System.out.println("\t****Log in*****");

			System.out.println("\tLoginId: ");
			EnteredName = Sc.nextLine();

			System.out.println("\tPassword: ");
			Enteredpassword = Sc.nextLine();

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema", "root",
					"Abhi08338@");
			Statement Stmt = con.createStatement();

			String sql = "Select * from user_tab Where LoginId='" + EnteredName + "' and Password='" + Enteredpassword
					+ "'";
			ResultSet resultSet = Stmt.executeQuery(sql);
			while (resultSet.next()) {
				FirstNameFromDB = resultSet.getString("FirstName");
				PasswordFromDB = resultSet.getString("Password");
				LoginType = resultSet.getString("LoginType");
				NameFromDB = resultSet.getString("LoginId");
			}
			if (EnteredName.equals(NameFromDB) && Enteredpassword.equals(PasswordFromDB)) {
				System.out.println("You Logged in Successfully...");
				return new LoginWithVerification(FirstNameFromDB, LoginType);
			} else
				System.out.println("Try Again Later");

		} while (true);

	}

}
