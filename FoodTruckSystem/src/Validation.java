public class Validation {
	 static int SaveDecision = 0;
	public static int PhoneNumberValidation(String PhoneNo)
	{

	 if (PhoneNo.matches("[0-9]{10}")) 
	 {
		 SaveDecision = 0;
	  }
	    else 
	    {
	        System.out.println("Mobile Number was Invalid!----Please Enter Again..");
	        SaveDecision = 1;
	     }
	return SaveDecision;
	}
	public static int EmailValidation(String EmailAddress)
	{

	 if (EmailAddress.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) 
	 {
		 
		 SaveDecision = 0;
	  }
	    else 
	    {
	        System.out.println("Email Address was Invalid!----Please Enter Again..");
	        SaveDecision = 1;
	     }
	return SaveDecision;
	}
}