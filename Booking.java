package javabikes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class of the program. This class guides
 * the whole user interaction with the system, and 
 * it calls constructors and methods from other classes
 * when it is needed. 
 */
public class Booking {

	public static void main(String[] args) throws IOException {
		// print the program's header
		System.out.printf(""
				+ "\t -------- __@      __@       __@        __@       __@       __@       __~@ \n"
				+ "\t ----- _`\\<,_    _`\\<,_    _`\\<,_     _`\\<,_    _`\\<,_    _`\\<,_    _`\\<,_ \n"
				+ "\t ---- (*)/ (*)  (*)/ (*)  (*)/ (*)  (*)/ (*)  (*)/ (*)  (*)/ (*)  (*)/ (*) \n"
				+ "\t ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n\n"
				+ "\t                          WELCOME TO JAVABIKES!\n\n");

		/* create a new Customer object. While doing this, we prompt the user to enter some info 
		 * such as name, document, email..*/
		Customer currentCustomer = new Customer();
		Boolean electricOrNot;
		ArrayList<?> allBikes = null;
		int bikeID;

		// print a thank you message
		System.out.println("\n\tTHANK YOU!\n");
		System.out.println("\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		do {
			do {
				electricOrNot = chooseIfElectric();
				if (electricOrNot) {
					allBikes = getElectricBikes();
					bikeBrowser(allBikes);
				} else {
					allBikes = getBikes();
					bikeBrowser(allBikes);
				}
			} while (continueBrowsing());
			bikeID = selectBike(allBikes);
		} while (!confirmed());

		Bike currentBike = (Bike)(allBikes.get(bikeID));
		CreditCard currentCard = new CreditCard();
		askLastConfirm(currentCustomer.getName(), currentCard.getHiddenNumber(), currentBike.getPrice());
		currentCard.makePayment();
		
		Invoice currentInvoice = new Invoice(currentBike, currentCustomer, currentCard, electricOrNot);
		
		currentInvoice.printInvoice();
		currentInvoice.saveInvoice();
		try {
			updateBikeDatabase(electricOrNot, bikeID);
		} catch (IOException ex) {
			System.out.println("Error while updating the bikes database, please contact admin@javabikes.com");
		}

	}	
	

	/**
	 * This methods asks the user if he/she wants an electric
	 * bike or a normal bike.
	 * <br>
	 * <br>
	 * The input is checked: 
	 * <ul><li>"1": input accepted, the customer chose
	 * to see electric bikes.</li>
	 * <li>"2": input accepted, the customer chose to see
	 * normal bikes.</li>
	 * <li>"quit": input accepted, it will lead to the
	 * execution of the second <code>case</code> statement in
	 * the <code>switch</code> block.</li>
	 * <li>any other input: not accepted, the user will have
	 * to re-enter something</li></ul>
	 * <br>
	 * <br>
	 * This method returns <code>true</code> or <code>
	 * false</code>, depending on the user's wishes. It can
	 * also terminate the program, if the user wants to. 
	 * 
	 * 
	 * @return 		Boolean
	 */
	public static Boolean chooseIfElectric() {
		Scanner input = new Scanner(System.in);
		String choice;
		System.out.println("\n\t§ Would you like an electric bike or a normal bike?");
		System.out.println("\t - Type \"1\" if you want an electric bike.");
		System.out.println("\t - Type \"2\" if you want a normal bike.");
		System.out.println("\t - Type \"quit\" to exit the program.");
		do {
			System.out.print("\t  ----> ");
			choice = input.nextLine();
		} while (!choice.equals("1") && !choice.equals("2") && !choice.equals("quit"));
		if (choice.equals("1")) {
			return true;
		} else if  (choice.equals("2")) {
			return false;
		} else {
			System.out.println("\tSorry to see you go. See you soon!");
			System.exit(0);
			return true;
		}
	}
	
	/**
	 * This methods asks the user if he/she wants to 
	 * continue to browse the bike's database, or if he/she
	 * already chose a bike and can move on, to book it. 
	 * <br>
	 * <br>
	 * The input is checked: 
	 * <ul><li>"yes": input accepted, it will lead to 
	 * the execution of the first <code>case</code> statement in
	 * the <code>switch</code> block.</li>
	 * <li>"quit": input accepted, it will lead to the
	 * execution of the second <code>case</code> statement in
	 * the <code>switch</code> block.</li>
	 * <li>null: also accepted, it will lead to the execution of
	 * the <code>default</code> statement in the 
	 * <code>switch</code> block.</li>
	 * <li>any other input: not accepted, the user will have
	 * to re-enter something</li></ul>
	 * <br>
	 * <br>
	 * This method returns <code>true</code> or <code>
	 * false</code>, depending on the user's wishes. It can
	 * also terminate the program, if the user wants to. 
	 * 
	 * @return 		Boolean
	 */
	public static Boolean continueBrowsing() {
		Scanner input = new Scanner(System.in);
		String choice;
		System.out.println("\n\n\t§ Continue to browse the bikes catalogue?");
		System.out.println("\t - Type \"yes\" if you want to see other bikes.");
		System.out.println("\t - Type \"quit\" to exit the program.");
		System.out.println("\t - Press enter to book a bike!");
		do {
			System.out.print("\t  ----> ");
			choice = input.nextLine();
		} while (!choice.equals("yes") && !choice.equals("quit") && choice == null);

		switch (choice) {
		case "yes": 
			return true;
		case "quit": 
			System.out.println("\tSorry to see you go. See you soon!");
			System.exit(0);
		default: 
			return false;
		}
	}
	

	/**
	 * This method loads the bike's database into an ArrayList of 
	 * Bike objects. The bike's database is a comma-delimited
	 * text file. 
	 * <br>
	 * <br>
	 * ArrayList is used since it can have a variable length: an
	 * Array has a fixed length, and it would lead to bugs if the 
	 * amount of bikes in the database changes over time. 
	 * <br>
	 * <br>
	 * When it is executed, it checks the current working
	 * directory (the root directory of the project) and it
	 * appends to that "/javabikes/bikes.txt": "/javabikes" is the 
	 * folder/package in which the "bikes.txt" database file is located. 
	 * This is needed in order to allow the project to be exported
	 * and imported on different machines, without having to remap 
	 * the path to the database file. 
	 * <br>
	 * <br>
	 * Once the file is located, a <code>BufferedReader</code> object
	 * is created, called <code>reader</code>. A <code>while</code> 
	 * loop goes through each line of the <code>reader</code>, and it stops 
	 * only when it finds a blank line: there are no more bikes, the 
	 * database is completely loaded.
	 * <br>
	 * <br>
	 * Each line of the database is split into an Array of strings,
	 * and then those values are passed to the <code>Bike</code>'s
	 * constructor: a <code>Bike</code> object called <code>newBike</code>
	 * is created. <code>newBike</code> is then added to the ArrayList, 
	 * through the <code>add(newBike)</code> method. 
	 *  
	 * @return			ArrayList of Bike objects. 
	 * @throws IOException
	 */
	public static ArrayList<Bike> getBikes() throws IOException {
		ArrayList<Bike> arrayBikes = new ArrayList<Bike>();
		Path workingDirectory = Paths.get("").toAbsolutePath();
		File path = new File (workingDirectory + "/javabikes/bikes.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String line;
		int tmpId = 0;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			Bike newBike = new Bike(tmpId, values[0], values[1], 
					Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]));
			arrayBikes.add(newBike); 
			tmpId += 1;
		}
		return arrayBikes;
	}
	
	/**
	 * This method loads the electric bike's database into an 
	 * ArrayList of ElectricBike objects. The bike's database 
	 * is a comma-delimited text file.
	 * <br>
	 * <br>
	 * This method is almost the same as <code>getBikes()</code>,
	 * with just minor differences due to the different types of 
	 * object that it deals with.
	 * <br>
	 * <br>
	 * ArrayList is used since it can have a variable length: an
	 * Array has a fixed length, and it would lead to bugs if the 
	 * amount of electric bikes in the database changes over time. 
	 * <br>
	 * <br>
	 * When it is executed, it checks the current working
	 * directory (the root directory of the project) and it
	 * appends to that "/javabikes/electricbikes.txt": "/javabikes" 
	 * is the folder/package in which the "electricbikes.txt" 
	 * database file is located. 
	 * This is needed in order to allow the project to be exported
	 * and imported on different machines, without having to remap 
	 * the path to the database file. 
	 * <br>
	 * <br>
	 * Once the file is located, a <code>BufferedReader</code> object
	 * is created, called <code>reader</code>. A <code>while</code> 
	 * loop goes through each line of the <code>reader</code>, and 
	 * it stops only when it finds a blank line: there are no more 
	 * electric bikes, the database is completely loaded.
	 * <br>
	 * <br>
	 * Each line of the database is split into an Array of strings,
	 * and then those values are passed to the <code>ElectricBike</code>'s
	 * constructor: a <code>ElectricBike</code> object called <code>newBike</code>
	 * is created. <code>newBike</code> is then added to the ArrayList, 
	 * through the <code>add(newBike)</code> method. 
	 *  
	 * @return			ArrayList of ElectricBike objects. 
	 * @throws IOException
	 */
	public static ArrayList<ElectricBike> getElectricBikes() throws IOException {
		ArrayList<ElectricBike> arrayBikes = new ArrayList<ElectricBike>();
		Path workingDirectory=Paths.get("").toAbsolutePath();
		File path = new File (workingDirectory + "/javabikes/electricbikes.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String line;
		int tmpId = 0;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");		
			ElectricBike newBike = new ElectricBike(tmpId, values[0], values[1], 
					Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]), 
					Integer.parseInt(values[4]));
			arrayBikes.add(newBike); 
			tmpId += 1;
		}
		return arrayBikes;
	}
	
	
	/**
	 * This method is called after the user chose between normal bikes
	 * and electric bikes. Depending on the <code>selectType()</code> result, 
	 * it will call the <code>printBikes()</code> method only with the <code>
	 * arrayBikes</code> parameter, or it will overload that method with the
	 * result of <code>whichType()</code>. For more details on <code>selectType()</code>,
	 * <code>printBikes()</code> and <code>whichType()</code> methods, see 
	 * their detailed description. 
	 * 
	 * @param arrayBikes 		an ArrayList of generic objects: it uses the ?
	 * 							notation, so any ArrayList is accepted. This is 
	 * 							done in order to be able to pass to this method
	 * 							both an ArrayList of <code>Bike</code> objects 
	 * 							and an ArrayList of <code>ElectricBike</code> 
	 * 							objects.
	 */
	public static void bikeBrowser(ArrayList<?> arrayBikes) {
		if (selectType()) {
			printBikes(arrayBikes, whichType());
		} else {
			printBikes(arrayBikes);
		}
	}
	
	
	/**
	 * This methods asks the user if he/she wants to 
	 * browse a specific bike type (men's, women's or kids'
	 * bikes), or if he/she wants to browse all bikes. 
	 * <br>
	 * <br>
	 * The input is checked: 
	 * <ul><li>"1": input accepted, the customer wants
	 * to see all available bikes; the method will return
	 * false.</li>
	 * <li>"2": input accepted, the customer wants to select
	 * a bike type; the method will return true.</li> 
	 * <li>any other input: not accepted, the user will have
	 * to re-enter something</li></ul>
	 * <br>
	 * <br>
	 * This method returns <code>true</code> or <code>
	 * false</code>, depending on the user's wishes.
	 * 
	 * @return 		Boolean
	 */
	public static Boolean selectType() {
		String choice;
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t§ Check all bikes or select a bike type?");
		System.out.println("\t - Type \"1\" to print all available bikes.");
		System.out.println("\t - Type \"2\" to select a bike type.");

		do {
			System.out.print("\t  ----> ");
			choice = input.nextLine();
		} while (!choice.equals("1") && !choice.equals("2"));

		if (choice.equals("2")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * This methods asks the user which type of bikes he/she 
	 * wants to see.  
	 * <br>
	 * <br>
	 * The input is checked: 
	 * <ul><li>"m": input accepted, the customer wants
	 * to see men's bikes.</li>
	 * <li>"w": input accepted, the customer wants
	 * to see women's bikes.</li>
	 * <li>"k": input accepted, the customer wants
	 * to see kids' bikes.</li>
	 * <li>any other input: not accepted, the user will have
	 * to re-enter something</li></ul>
	 * <br>
	 * <br>
	 * This method returns the customer's choice ("m", "w" 
	 * or "k"); basically, it is needed only to sanitize the 
	 * user's input, to prevent unhandled exceptions to occur. 
	 * 
	 * @return 		String
	 */
	public static String whichType() {
		String choice;
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t§ Select a bike type from the list: ");
		System.out.println("\t - Type \"m\" to select man's bikes.");
		System.out.println("\t - Type \"w\" to select woman's bikes.");
		System.out.println("\t - Type \"k\" to select kids' bikes.");
		do {
			System.out.print("\t  ----> ");
			choice = input.nextLine();
		} while (!choice.equals("m") && !choice.equals("w") && !choice.equals("k"));
		return choice;
	}
	
	/**
	 * This methods prints out a table with the available bikes.
	 * <br>
	 * <br>
	 * It receives an ArrayList of generic objects, it checks if those objects
	 * belong to the ElectricBike class or not. If they do, it will print 
	 * the ID, color, price, bike type, battery level and km range for each bike,
	 * only if the bike is available.
	 * <br>
	 * <br> 
	 * If the ArrayList is made of normal bikes, it will act as above, except 
	 * that it will not print the two last columns (battery level and km range),
	 * since a normal bike has no battery (and no km range). 
	 * 
	 * 
	 * @param arrayBikes 		an ArrayList of generic objects: it uses the ?
	 * 							notation, so any ArrayList is accepted. This is 
	 * 							done in order to be able to pass to this method
	 * 							both an ArrayList of <code>Bike</code> objects 
	 * 							and an ArrayList of <code>ElectricBike</code> 
	 * 							objects.
	 * 
	 */
	public static void printBikes(ArrayList<?> arrayBikes) {
		System.out.println("\n\n\tAVAILABLE BIKES: \n");		

		if (arrayBikes.get(0) instanceof ElectricBike) {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-9s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object thisObject : arrayBikes) {
				ElectricBike thisBike = (ElectricBike)thisObject;
				if (thisBike.getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s | %-7s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", 
							thisBike.getBikeTypeString(), thisBike.getBatteryLevel() + " %", thisBike.getKmRange());
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object thisObject : arrayBikes) {
				Bike thisBike = (Bike)thisObject;
				if (thisBike.getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", thisBike.getBikeTypeString());
				}
			}
		}
	}
	
	
	/**
	 * Overloads the <code>printBikes(arrayBikes)</code> method; it
	 * does so by specifying an additional parameter: a <code>String</code> called
	 * <code>bikeType</code>. It acts almost exactly as <code>printBikes(arrayBikes)</code>,
	 * but it prints out only the bikes of the specified <code>bikeType</code>. 
	 * <br>
	 * <br>
	 * This method receives an ArrayList of generic objects, it checks if those objects
	 * belong to the ElectricBike class or not. If they do, it will print 
	 * the ID, color, price, bike type, battery level and km range for each bike,
	 * only if the bike is available and if it is of the correct type (men's, women's
	 * or kids' bike).
	 * <br>
	 * <br> 
	 * If the ArrayList is made of normal bikes, it will act as above, except 
	 * that it will not print the two last columns (battery level and km range),
	 * since a normal bike has no battery (and no km range). 
	 * 
	 * 
	 * @param arrayBikes 		an ArrayList of generic objects: it uses the ?
	 * 							notation, so any ArrayList is accepted. This is 
	 * 							done in order to be able to pass to this method
	 * 							both an ArrayList of <code>Bike</code> objects 
	 * 							and an ArrayList of <code>ElectricBike</code> 
	 * 							objects.	
	 * 
	 * @param bikeType			a String that describes the bikeType that the 
	 * 							user wants to see. It can be "m", "w" or "k". 
	 *
	 */
	public static void printBikes(ArrayList<?> arrayBikes, String bikeType) {
		System.out.println("\n\n\tAVAILABLE BIKES: \n");		
		if (arrayBikes.get(0) instanceof ElectricBike) {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-9s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object thisObject : arrayBikes) {
				ElectricBike thisBike = (ElectricBike)thisObject;
				if (thisBike.getIsAvailable() && thisBike.getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s | %-7s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", 
							thisBike.getBikeTypeString(), thisBike.getBatteryLevel() + " %", thisBike.getKmRange());
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object thisObject : arrayBikes) {
				Bike thisBike = (Bike)thisObject;
				if (thisBike.getIsAvailable() && thisBike.getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", thisBike.getBikeTypeString());
				}
			}
		}
	}

	public static int selectBike(ArrayList<?> arrayBikes) {
		int ID = 0;
		System.out.println("\t§ Which bike do you want to book? Insert its ID.");
		Boolean inputOk = false;
		System.out.print("\t  ----> ");
		while (!inputOk) {
			try {
				Scanner input = new Scanner(System.in);
				ID = input.nextInt();
				if (((Bike)arrayBikes.get(ID)).getIsAvailable()) {
					inputOk = true;
				} else {
					throw new CustomException();
				}
			} catch (InputMismatchException ex) {
				System.out.print("\t  ----> Enter an integer: ");
				continue;
			} catch (IndexOutOfBoundsException ex) {
				System.out.print("\t  ----> ID too big, enter it again: ");
				continue;
			} catch (CustomException ex) {
				System.out.print("\t  ----> Bike not available, choose another one: ");
				continue;
			}
		}		
		return ID;
	}
	public static Boolean confirmed() {
		String userInput;
		Boolean userBoolean;
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t§ Do you confirm your choice? Enter \"yes\" or \"no\": ");
		do {
			System.out.print("\t    ----> ");
			userInput = input.nextLine().toLowerCase();
		} while (!(userInput.equals("yes")) && !(userInput.equals("no")));	

		if (userInput.matches("yes")) {
			userBoolean = true;
		} else {
			userBoolean = false;
		};
		return userBoolean;
	}

	public static void askLastConfirm(String name, String lastNumbers, int price) {
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.printf("\n\tDear %s, we will charge your card that ends with %s for %s kr.\n", 
				name, lastNumbers, price);
		System.out.println("\n\t>> This is the last chance to cancel your order. <<");
		System.out.println("\t  - Type \"quit\" to cancel it.");
		System.out.println("\t  - Press enter to continue.");
		System.out.print("\t    ----> ");
		if (input.nextLine().equals("quit")) {
			System.out.println("\tSorry to see you go. See you soon!");
			System.exit(0);
		}
	}

	public static void updateBikeDatabase(Boolean electricOrNot, int bikeID) throws IOException {
		Path workingDirectory = Paths.get("").toAbsolutePath();
		String dbFile;
		if (electricOrNot) {
			dbFile = "electricbikes.txt";
		} else {
			dbFile = "bikes.txt";
		}

		// get the current bike database and a temporary file 
		String oldFileName = workingDirectory + "/javabikes/" + dbFile;
		String tmpFileName = workingDirectory + "/javabikes/tmp" + dbFile;

		// initialize buffered reader and writer
		BufferedReader br = new BufferedReader(new FileReader(oldFileName));
		BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFileName));

		// temporary variables
		String lineTwo;
		int lineNumber = 0;

		// loop through the old file until chosen bike 
		while ((lineTwo = br.readLine()) != null) {
			if (lineNumber == bikeID) {
				/* current bike, replace true with false 
				 * bike is not available anymore */
				lineTwo = lineTwo.replace("true", "false");
				bw.write(lineTwo + "\n");
			} else {
				/* if it's not the chosen bike, 
				 * just copy the same line */
				bw.write(lineTwo + "\n");
			}
			lineNumber += 1;
		} 

		// close buffered reader
		if (br != null) {
			br.close();
		}

		// close buffered writer
		if (bw != null) {
			bw.close();
		}

		// delete old file
		File oldFile = new File(oldFileName);
		oldFile.delete();

		// rename temp file to default names
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile);

	}
}
