package javabikes;

import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This class represents an invoice. An invoice
 * object is created only if the customer completes
 * the booking and the payment of a bike. 
 */
public class Invoice {

	/**
	 * The invoice itself. 
	 */
	private String invoice;

	/**
	 * The current date in which the invoice
	 * is created. 
	 */
	private String DateString; 

	/**
	 * The current time in which the invoice
	 * is created. 
	 */
	private String TimeString;

	/**
	 * The customer that is associated with 
	 * an invoice. 
	 */
	private Customer myCustomer;


	/**
	 * Constructor of the <code>Invoice</code> object. It
	 * just creates a long string which is the invoice itself,
	 * with different data - depending on the customer choices. 
	 * <br>
	 * <br>
	 * The invoice is created through a basic template, filled
	 * with information provided by the parameters of the constructor. 
	 * Almost all the data are retrieved through the appropriate getters
	 * (for example, <code>currentBike.getColor()</code>); only the 
	 * <code>electricOrNot</code> value is "translated" from a true/false 
	 * value to a yes/no string. The date and time are retrieved from the
	 * Java's <code>Date()</code> object. 
	 * 
	 * @param currentBike		a <code>Bike</code> object, representing
	 * 							the bike chosen by the customer. 
	 * @param currentCustomer	a <code>Customer</code> object, representing
	 * 							the customer that is associated with this invoice. 
	 * @param currentCard		a <code>CreditCard</code> object, representing 
	 * 							the credit card that the customer used to pay for the
	 * 							current booking. 
	 * @param electricOrNot		a <code>Boolean</code> value, it is <code>true</code>
	 * 							if the customer chose an electric bike, otherwise it 
	 * 							is false. 
	 */
	Invoice(Bike currentBike, Customer currentCustomer, CreditCard currentCard, Boolean electricOrNot) {
		myCustomer = currentCustomer;
		invoice = "";
		String electricText; 

		if (electricOrNot) {
			electricText = "yes";
		} else {
			electricText = "no";
		}

		DateFormat dateformat  = new SimpleDateFormat ("yyyy-MM-dd");
		Date date = new Date();
		DateString = dateformat.format(date);

		DateFormat timeformat  = new SimpleDateFormat ("HH-mm-ss");
		Date time = new Date();
		TimeString = timeformat.format(time);

		invoice += "         ---------------------------------------------\n";
		invoice += "                    >  WELCOME TO JAVABIKES  <  \n";
		invoice += "         ---------------------------------------------\n\n";
		invoice += "                        ~~ BIKE INFO ~~\n";
		invoice += String.format("                Bike Type:            %9s\n", currentBike.getBikeTypeString());
		invoice += String.format("                Color:                   %6s\n", currentBike.getColor());
		invoice += String.format("                Electric:                   %3s\n\n", electricText);
		invoice += "                        ~~ YOUR INFO ~~\n";
		invoice += String.format("          Name:        %30s\n", currentCustomer.getName());
		invoice += String.format("          Surname:     %30s\n", currentCustomer.getSurname());
		invoice += String.format("          e-mail:%36s\n", currentCustomer.getEmail());
		invoice += String.format("          Document:              %20s\n\n", currentCustomer.getDocument());
		invoice += "                        ~ PAYMENT INFO ~\n";
		invoice += String.format("                %10s                %5s\n", DateString, TimeString);
		invoice += String.format("                Credit Card:   **** **** **** %4s\n", currentCard.getHiddenNumber());
		invoice += String.format("                Subtotal:                %.2f DKK\n", (currentBike.getPrice() * 0.75));		
		invoice += String.format("                VAT - 25%%:               %.2f DKK\n\n", (currentBike.getPrice() * 0.25));		
		invoice += String.format("                Total:                   %.2f DKK\n\n", ((double)currentBike.getPrice()));		
		invoice += "                          THANK YOU!\n\n";
		invoice += "                         -------- __@\n" ;
		invoice += "                         ----- _`\\<,_\n";
		invoice += "                         ---- (*)/ (*)\n";
		invoice += "                         ~~~~~~~~~~~~~\n\n";
		invoice += "                        ENJOY YOUR RIDE!\n";
		invoice += "         ---------------------------------------------\n";
		invoice += "                Howitzvej 60, 2000 Frederiksberg\n";
		invoice += "                        TLF: +45 12345678\n";
		invoice += "         ---------------------------------------------\n";
	}


	/** 
	 * Getter method, it gives back the invoice.
	 * 
	 * @return          		String
	 */
	public String getInvoice() {
		return invoice;
	}


	/**
	 * This method prints out the invoice. The first line is 
	 * needed to "scale up" all the previous console's output.
	 */
	public void printInvoice() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(invoice);
	}


	/**
	 * This method saves the invoice to a file. The name of the 
	 * file is standardized: 
	 * <ul><li>unixtime (when the invoice is created)</li>
	 * <li>"_JB_", stands for "JavaBikes"</li>
	 * <li>The number of the user's document</li>
	 * <li>".txt", the appropriate extension for a text file</li>
	 * </ul>
	 * @throws IOException
	 */
	public void saveInvoice() throws IOException {
		Path workingDir = Paths.get("").toAbsolutePath();
		String timeInvoice = Long.toString(System.currentTimeMillis() / 1000L);
		String fileName = timeInvoice + "_JB_" + myCustomer.getDocument() + ".txt";
		String fullFilePath = workingDir + "/javabikes/" + fileName;

		BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilePath));		
		bw.write(invoice);
		bw.close();

		System.out.println("\n\n\n\tThank you! Your invoice is displayed above.");
		System.out.println("\tIt was also saved to this file: " + fileName);
		System.out.println("\n\tFull file path: " + fullFilePath);
	}
}

