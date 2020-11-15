import java.sql.*;
import java.util.Scanner;
public class LoginWithVerification {
	String EnteredName;
    String Enteredpassword;
    String NameFromDB = null;
    String PasswordFromDB = null;
    
 public LoginWithVerification() 
 {
		
	}

public LoginWithVerification(String enteredName, String enteredpassword) {
		this.EnteredName = enteredName;
		this.Enteredpassword = enteredpassword;
	}

public LoginWithVerification Login() throws SQLException {
    Scanner Sc = new Scanner(System.in);
    
do
	{
		System.out.println("Log in:");
		
	    System.out.println("username: ");
	    EnteredName = Sc.next();
	
	    System.out.println("password: ");
	    Enteredpassword = Sc.next();
   
   
    	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema","root","Abhi08338@");
    	Statement Stmt = con.createStatement();
	        
    	String sql = "Select * from user_tab Where LoginId='" + EnteredName + "' and Password='" + Enteredpassword + "'";
         ResultSet  resultSet = Stmt.executeQuery(sql);
	     while(resultSet.next()) 
	     { 
	        NameFromDB = resultSet.getString("LoginId");
	        PasswordFromDB = resultSet.getString("Password");
	     }
	          if(EnteredName.equals(NameFromDB) && Enteredpassword.equals(PasswordFromDB))
	          {
	             System.out.println("You are logged in");
	             return new LoginWithVerification(EnteredName,Enteredpassword);
	          }
	          else 
	        	  System.out.println("Try Again Later");   
	        	        
	           
	        }while(true);
    		
    }


}
