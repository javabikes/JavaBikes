package javabikes;

import java.util.Date;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Invoice {

	private String invoice;
	private String DateString; 
	private String TimeString;
	private Customer myCustomer;

	Invoice(Bike currentBike, Customer currentCustomer, CreditCard currentCard, Boolean electricOrNot) {
		myCustomer = currentCustomer;
		invoice = "";
		String electricText; 
		if (electricOrNot) {
			electricText = "yes";
		} else {
			electricText = "no";
		}

		// Get date and format it
		DateFormat dateformat  = new SimpleDateFormat ("yyyy-MM-dd");
		Date date = new Date();
		DateString = dateformat.format(date);

		// Get time and format it
		DateFormat timeformat  = new SimpleDateFormat ("HH-mm-ss");
		Date time = new Date();
		TimeString = timeformat.format(time);
		
		invoice += "\t ---------------------------------------------\n";
		invoice += "\t\t    §  WELCOME TO JAVABIKES  §  \n";
		invoice += "\t ---------------------------------------------\n\n";
		invoice += "\t\t\t~~ BIKE INFO ~~\n";
		invoice += String.format("\t\tBike Type:            %9s\n", currentBike.getBikeTypeString());
		invoice += String.format("\t\tColor:                   %6s\n", currentBike.getColor());
		invoice += String.format("\t\tElectric:                   %3s\n\n", electricText);
		invoice += "\t\t\t~~ YOUR INFO ~~\n";
		invoice += String.format("\t  Name:        %30s\n", currentCustomer.getName());
		invoice += String.format("\t  Surname:     %30s\n", currentCustomer.getSurname());
		invoice += String.format("\t  e-mail:%36s\n", currentCustomer.getEmail());
		invoice += String.format("\t  Document:              %20s\n\n", currentCustomer.getDocument());
		invoice += "\t\t\t~ PAYMENT INFO ~\n";
		invoice += String.format("\t\t%10s                %5s\n", DateString, TimeString);
		invoice += String.format("\t\tCredit Card:   **** **** **** %4s\n", currentCard.getHiddenNumber());
		invoice += String.format("\t\tSubtotal:                %.2f DKK\n", (currentBike.getPrice() * 0.75));		
		invoice += String.format("\t\tVAT - 25%%:               %.2f DKK\n\n", (currentBike.getPrice() * 0.25));		
		invoice += String.format("\t\tTotal:                   %.2f DKK\n\n", ((double)currentBike.getPrice()));		
		invoice += "\t\t\t  THANK YOU!\n\n";
		invoice += "\t\t\t -------- __@\n" ;
		invoice += "\t\t\t ----- _`\\<,_\n";
		invoice += "\t\t\t ---- (*)/ (*)\n";
		invoice += "\t\t\t ~~~~~~~~~~~~~\n\n";
		invoice += "\t\t\tENJOY YOUR RIDE!\n";
		invoice += "\t ---------------------------------------------\n";
		invoice += "\t\tHowitzvej 60, 2000 Frederiksberg\n";
		invoice += "\t\t\tTLF: +45 12345678\n";
		invoice += "\t ---------------------------------------------\n";
	}

	public String getInvoice() {
		return invoice;
	}
	
	public void printInvoice() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(invoice);
	}
	
	public void saveInvoice() throws IOException {
		Path workingDir = Paths.get("").toAbsolutePath();
		String timeInvoice = Long.toString(System.currentTimeMillis() / 1000L);
		String fileName = timeInvoice + "_JB_" + myCustomer.getDocument() + ".txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(workingDir + "/javabikes/" + fileName));
		bw.write(invoice);
		bw.close();
		
		System.out.println("\n\n\n\tThank you! You're invoice is displayed above.");
		System.out.println("\tIt was also saved in this file: " + fileName);
	}
}



