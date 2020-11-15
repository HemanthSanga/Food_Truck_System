import java.sql.*;
import java.util.Scanner;
public class Registration 
{
	
		  public String LoginType;
		  public String FirstName;
		  public String LastName;
		  public String EmailId;
		  public String MobileNo;
		  public String LoginID;
		  public String Password;
		  public String ExitOption;
		  
		  
		  
		 public int UserRegistration() throws ClassNotFoundException, SQLException
		{
		  
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_truck_schema","root","Abhi08338@");
			PreparedStatement preparedStmt;
			Scanner Sc = new Scanner(System.in);
		
				System.out.println("Press O/o for Owner and C/c for Customer");
				LoginType = Sc.next();
				
				int TempVariable;
				if(LoginType.equals("c")||LoginType.equals("C")||LoginType.equals("o")||LoginType.equals("O"))
				{
					System.out.println("Enter Your LoginID");
					LoginID = Sc.next();
			
					System.out.println("Enter Your First Name");
					FirstName = Sc.next();
					
					System.out.println("Enter Your Last Name");
					LastName = Sc.next();
					do {
					System.out.println("Enter Your EmailId in Following Pattern : xxxxx@xxxx.com");
					EmailId = Sc.next();
					TempVariable=Validation.EmailValidation( EmailId);
					
					}while(TempVariable==1);
					
					do{ 
						System.out.println("Enter Your 10 Digit Mobile Number");
					MobileNo = Sc.next();
					TempVariable=Validation.PhoneNumberValidation( MobileNo);
					
						}while(TempVariable==1);
					
					System.out.println("Enter Your Password");
					Password = Sc.next();
					
					
					String Query = "INSERT INTO user_tab (`LoginType`,`LoginId`,`Password`, `FirstName`, `SecondName`, `EmailId`, `MobileNumber`) VALUES (?,?,?,?,?,?,?)";
					
					preparedStmt = con.prepareStatement(Query);
					preparedStmt.setString (1,LoginType );
					preparedStmt.setString (2,LoginID );
					preparedStmt.setString (3,Password );
					preparedStmt.setString (4,FirstName );
					preparedStmt.setString (5,LastName);
					preparedStmt.setString (6, EmailId);
					preparedStmt.setString (7, MobileNo);
					
				    preparedStmt.execute();
				    con.close();
				    
				}
				else{
				System.out.println("Your Input was incorrect: Press 0 to Exit ");
				ExitOption = Sc.next();
					if(ExitOption.equals("0"))
					System.exit(0);
				else
					{
					System.out.println("Wrong Input.. Try Again Later...Bye..");
					System.exit(0);
					}
				}
				return 0;
						
		}
	}



