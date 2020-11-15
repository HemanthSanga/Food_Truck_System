package controlFlow;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import BookAndCheckAppointment.BookAppointment;
import BookAndCheckAppointment.CheckAppointment;
import FeedBack.GetFeedback;
import FeedBack.GiveFeedback;
import JasperReport.appointmentReport;
import Offers.AddOffer;
import Offers.ViewOffer;
import Registration.ApprovalOfFoodTruck;
import Registration.FoodTruckRegistration;
import Registration.LoginWithVerification;
import Registration.Registration;
import Search.searchByDateAndTime;
import Search.searchByLocation;
import Search.searchByMenu;
import Search.searchByName;
import Updates.UpdateTruckSchedule;
import menu.UpdateTruckMenu;
import menu.foodMenu;
import menu.foodMenuDisplay;
import net.sf.jasperreports.engine.JRException;
public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JRException 
	{	String UserOption;
		boolean CharacterCheckFlag = false;
		boolean AdminCharacterCheckFlag = false;
		boolean TruckOwnerCharacterCheckFlag = false;
		boolean CustomerCharacterCheckFlag = false;
		boolean CustomerSubMenuCharacterCheckFlag = false;
		boolean ExitCustomerSubMenu = false;
		boolean ExitCustomerSearchMenu = true;
		String TruckOwnerOption;
		int SelectedTruck = 0;
		Scanner Sc = new Scanner(System.in);
		do {
		System.out.println("================================ ");
		System.out.println("WelCome To Food Truck System");
		System.out.println("================================ ");
		System.out.println("***Select From Following Option***");
		System.out.println("1. User Registeration");
		System.out.println("2. Login");
		System.out.println("Q. Quit");		
		UserOption = Sc.nextLine();
			 
			 switch(UserOption)
			 {
			 case "1": Registration Register = new Registration();
					   Register.UserRegistration();
					   CharacterCheckFlag = true;
				 		break;
			 case "2": LoginWithVerification LoginPage = new LoginWithVerification();
			 			LoginPage.Login();
			 			if (LoginPage.LoginType.equalsIgnoreCase("A")) 
			 			{		do {
								System.out.println("Welcome Admin: " + LoginPage.FirstNameFromDB);
								System.out.println("Press 1 To Start Approvals");
								System.out.println("Press L To Log Out");
									String AdminChoise = Sc.nextLine();
									switch(AdminChoise)
									{
									case "1" : ApprovalOfFoodTruck Approve = new ApprovalOfFoodTruck();
											   Approve.Approval();
											   AdminCharacterCheckFlag =true;
											   break;
									case "L" : AdminCharacterCheckFlag= false;
												break;
									 default :System.out.println("Other Character Not Allowed....Admin");
									 			AdminCharacterCheckFlag =true;
									 			break;
												
									}
			 						}while(AdminCharacterCheckFlag);
			 			}		
														
						if (LoginPage.LoginType.equalsIgnoreCase("O")) 
						{
							System.out.println("Welcome " + LoginPage.FirstNameFromDB);
							do {
								System.out.println("Select From Following Option");
								System.out.println("1. Food Truck Registration");
								System.out.println("2. Enter Food Menu ");
								System.out.println("3. Update Food Menu ");
								System.out.println("4. Add Food Offers  ");
								System.out.println("5. Update Schedule ");
								System.out.println("6. View the Appointment  ");
								System.out.println("7. View the FeedBack ");
								System.out.println("8. Print Appointment Report ");
								System.out.println("0. Exit");
								TruckOwnerOption = Sc.nextLine();
								switch(TruckOwnerOption)
								{
								case "1":
									FoodTruckRegistration FoodtruckRegistration = new FoodTruckRegistration();
									FoodtruckRegistration.truckRegistration(LoginPage.NameFromDB);
									TruckOwnerCharacterCheckFlag = true;
									break;
								case "2":	foodMenu EnterFoodMenu = new foodMenu();
											EnterFoodMenu.insertfoodMenu();
											TruckOwnerCharacterCheckFlag = true;
											break;
								case "3":	UpdateTruckMenu Updatemenu = new UpdateTruckMenu();
										 	Updatemenu.UpdateFoodMenu();
										 	TruckOwnerCharacterCheckFlag = true;
											break;
								case "4":	AddOffer AddFoodOffers = new AddOffer();
											AddFoodOffers.foodOffer();
											TruckOwnerCharacterCheckFlag = true;
											break;
								case "5":	UpdateTruckSchedule UpdateSchedule = new UpdateTruckSchedule();
											UpdateSchedule.UpdateSchedule();
											TruckOwnerCharacterCheckFlag = true;
											break;
								case "6":	CheckAppointment ViewAppt = new CheckAppointment();
											ViewAppt.ViewAppointmentdata();
											TruckOwnerCharacterCheckFlag = true;
											break;
								
								case "7":	GetFeedback ShowFeedBack = new GetFeedback();
											ShowFeedBack.FeedbackData();
											TruckOwnerCharacterCheckFlag = true;
											break;
								case "8":	appointmentReport Report = new appointmentReport();
											Report.JasperReport();
											TruckOwnerCharacterCheckFlag = true;
											break;
								
								case "0": 	TruckOwnerCharacterCheckFlag = false;
											ExitCustomerSearchMenu = true;
											break;
								default :
										System.out.println("Other Character not Allowed...\n Select From The Displayed Menu");
										TruckOwnerCharacterCheckFlag = true;
								}
								
							}while(TruckOwnerCharacterCheckFlag);	
								
								
						}
						if (LoginPage.LoginType.equalsIgnoreCase("C")) 
						{
							do {
								do {
									System.out.println("Welcome " + LoginPage.FirstNameFromDB);
									System.out.println("Select From Following Option");
									System.out.println("1.Search By Name ");
									System.out.println("2.Search By Location ");
									System.out.println("3.Search By Date And Time ");
									System.out.println("4.Search By Menu ");
									System.out.println("0.Exit ");
									String CustomerChoise = Sc.nextLine();
									switch(CustomerChoise)
									{
									case "1" :	searchByName SearchFootTruckByName = new searchByName();
												SelectedTruck=SearchFootTruckByName.searchName();
												CustomerCharacterCheckFlag = false;
												break;
									case "2": 	searchByLocation LocationSearch = new searchByLocation();
												SelectedTruck = LocationSearch.searchLocation();
												CustomerCharacterCheckFlag = false;
												if(SelectedTruck==0) {
													CustomerCharacterCheckFlag = true;	
												}
												break;
									case "3":	searchByDateAndTime DateTimeSearch = new searchByDateAndTime();
												SelectedTruck=  DateTimeSearch.searchTime();
												CustomerCharacterCheckFlag = false;
												if(SelectedTruck==0) {
													CustomerCharacterCheckFlag = true;	
												}
												break;
									case "4":	searchByMenu MenuSearch = new searchByMenu();
												SelectedTruck = MenuSearch.searchMenu();
												CustomerCharacterCheckFlag = false;
												if(SelectedTruck==0) {
													CustomerCharacterCheckFlag = true;	
												}
												break;
									case "0":	CustomerCharacterCheckFlag = false; 
												ExitCustomerSearchMenu = false;
												SelectedTruck = 0;
												break;
									default :	System.out.println(" No other Characters Allowed ");
												CustomerCharacterCheckFlag = true;
									}
									
											
								}while(CustomerCharacterCheckFlag);
								if(SelectedTruck>0)
								{
								do {
									System.out.println("Select From Following ");
									System.out.println("1.View Menu ");
									System.out.println("2.Book Appointment ");
									System.out.println("3.View Offer ");
									System.out.println("4.Give Feedback ");
									System.out.println("0.To Return To Search Options ");
									String SelectedSubMenu = Sc.nextLine();
									switch(SelectedSubMenu)
									{
									case "1":	foodMenuDisplay ViewMenu = new foodMenuDisplay();
												ViewMenu.DisplayMenu(SelectedTruck);
												CustomerSubMenuCharacterCheckFlag = true;
												break;
									case "2":	BookAppointment BookAppt = new BookAppointment();							
												BookAppt.BookAppointmentInput(SelectedTruck,LoginPage.NameFromDB);
												CustomerSubMenuCharacterCheckFlag = true;
												break;
									case "3":	ViewOffer FoodOffers = new ViewOffer();
												FoodOffers.foodOfferDisplay(SelectedTruck);
												CustomerSubMenuCharacterCheckFlag = true;
												break;
									case "4" : 	GiveFeedback FeedbackFromCustomer = new GiveFeedback();
												FeedbackFromCustomer.CustomerFeedback(SelectedTruck);
												CustomerSubMenuCharacterCheckFlag = true;
												break;
									case "0" : 	ExitCustomerSubMenu = true;
												CustomerSubMenuCharacterCheckFlag =false;
												SelectedTruck=0;
												break;
									default :	System.out.println(" Do Not Enter Other Characters ");
												CustomerSubMenuCharacterCheckFlag = true;
									}
								}while(CustomerSubMenuCharacterCheckFlag);	
								} 
							}while(ExitCustomerSearchMenu && ExitCustomerSubMenu );
						}																
						
			case "X":  System.out.println("Visit Again...");
			 		CharacterCheckFlag=true;
						//System.exit(0);
				 		break;
			case "Q":  	System.out.println(" Bye...");
	 					//CharacterCheckFlag=true;
	 					System.exit(0);
	 					//break;
			 default :System.out.println("Other Character Not Allowed");
			 		CharacterCheckFlag =true;
			 		break;
			 }
			} while (CharacterCheckFlag);
			 
			 
			 
			 
	}

}
