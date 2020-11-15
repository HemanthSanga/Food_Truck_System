
import java.util.Scanner;

public class Menu {
	public static void main(String args[]){
		int choice,telephoneNumber;
		 boolean quit = false;
		String firstName,secondName,emailId,userName,password ;
		System.out.println("FOOD TRUCK SYSTEM");
		do {
			System.out.println("Press 1 to Register:");
			System.out.println("Press 2 to Login:");
			System.out.println("Press 0 to exit");
			System.out.println("Please input Your Option:");
			Scanner Sc = new Scanner(System.in);
			choice = Sc.nextInt();
			
			switch(choice)
			{
			
			 case 0:

                 quit = true;

                 break;
			case 1:System.out.println("Register");
					
					break;
			
			case 2:System.out.println("Login");
			
					break;
		
			}
		}while(!quit);
	}

}
