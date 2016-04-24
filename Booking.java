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

		Customer currentCustomer = new Customer();
		Boolean electricOrNot;
		ArrayList<?> allBikes = null;
		int bikeID;

		System.out.println("\n\tTHANK YOU!\n");
		System.out.println("\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		do {
			electricOrNot = chooseIfElectric();
			if (electricOrNot) {
				allBikes = getElectricBikes();
				bikeBrowser(allBikes);
			} else {
				allBikes = getBikes();
				bikeBrowser(allBikes);
			}
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
	 * The input can be: 
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
	 * @return Boolean				a Boolean value, that is true if the 
	 * 								user wants to see electric bikes, it
	 * 								is false if the user wants to see normal
	 * 								bikes. 
	 */
	public static Boolean chooseIfElectric() {
		Scanner input = new Scanner(System.in);
		String choice;
		System.out.println("\n\t> Would you like an electric bike or a normal bike?");
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
			System.out.println("\tWe hope to see you again! Bye :)");
			System.exit(0);
			return true;
		}
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
	 * confirm the choice, or if he/she wants to browse the
	 * bikes' database again. It returns <code>true</code> or <code>
	 * false</code>, depending on the user's wishes. It can
	 * also terminate the program, if the user wants so. 
	 * <br>
	 * <br>
	 * The input can be: 
	 * <ul><li>"yes": input accepted, the method will return
	 * true. </li>
	 * <li>"quit": input accepted, the program will exit.</li>
	 * <li>"no": input accepted, the method will return false
	 * and the user will browse the bikes' database again.</li></ul>
	 * <br>
	 * <br>
	 * @return Boolean				a Boolean value, that is true if the 
	 * 								user wants to continue to browse
	 * 								the bikes' catalog, otherwise it will
	 * 								be false.
	 * 
	 */
	public static Boolean confirmed() {
		Scanner input = new Scanner(System.in);
		String choice;
		System.out.println("\n\t> Do you confirm your choice? ");
		System.out.println("\t - Type \"yes\" if you confirm your choice.");
		System.out.println("\t - Type \"no\" if you want to browse again the bike's database.");
		System.out.println("\t - Type \"quit\" to exit the program.");

		do {
			System.out.print("\t  ----> ");
			choice = input.nextLine();
		} while (!choice.equals("yes") && !choice.equals("quit") && !choice.equals("no"));

		switch (choice) {
		case "yes": 
			return true;
		case "quit": 
			System.out.println("\tWe hope to see you again! Bye :)");
			System.exit(0);
		case "no": 
			return false;
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
	 * @return arrayBikes				ArrayList of Bike objects. 
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
	 * @return arrayBikes			ArrayList of ElectricBike objects. 
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
	 * This methods asks the user if he/she wants to 
	 * browse a specific bike type (men's, women's or kids'
	 * bikes), or if he/she wants to browse all bikes. 
	 * <br>
	 * <br>
	 * The input can be: 
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
	 * @return Boolean				a Boolean value, that is true if the 
	 * 								user wants to select a bike type and
	 * 								that is false if the user wants to 
	 * 								see all available bikes.
	 */
	public static Boolean selectType() {
		String choice;
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t> Browse all bikes or select a bike type? (men, women or kids bikes)");
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
	 * The input can be: 
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
	 * @return choice			a String, it can be "m" or "w"
	 * 							or "k", depending on the user's 
	 * 							wishes. 
	 */
	public static String whichType() {
		String choice;
		Scanner input = new Scanner(System.in);
		System.out.println("\n\t> Select a bike type from the list: ");
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
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-8s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object thisObject : arrayBikes) {
				ElectricBike thisBike = (ElectricBike)thisObject;
				if (thisBike.getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |  %-6s |  %-7s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", 
							thisBike.getBikeTypeString(), thisBike.getBatteryLevel() + " %", 
							thisBike.getKmRange() + " km.");
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object thisObject : arrayBikes) {
				Bike thisBike = (Bike)thisObject;
				if (thisBike.getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", 
							thisBike.getBikeTypeString());
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
		switch(bikeType) {
		case "m":
			System.out.printf("\n\n\tAVAILABLE MEN BIKES: \n");	
			break;
		case "w": 
			System.out.printf("\n\n\tAVAILABLE WOMEN BIKES: \n");
			break;
		case "k":
			System.out.printf("\n\n\tAVAILABLE KIDS BIKES: \n");		
			break;
		default:
			System.out.printf("\n\n\tAVAILABLE BIKES: \n");		
		}

		if (arrayBikes.get(0) instanceof ElectricBike) {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-8s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object thisObject : arrayBikes) {
				ElectricBike thisBike = (ElectricBike)thisObject;
				if (thisBike.getIsAvailable() && thisBike.getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |  %-6s |  %-7s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.", 
							thisBike.getBikeTypeString(), thisBike.getBatteryLevel() + " %",
							thisBike.getKmRange() + " km.");
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object thisObject : arrayBikes) {
				Bike thisBike = (Bike)thisObject;
				if (thisBike.getIsAvailable() && thisBike.getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							thisBike.getBikeId(), thisBike.getColor(), thisBike.getPrice() + " kr.",
							thisBike.getBikeTypeString());
				}
			}
		}
	}

	/**
	 * This method is called when the user wants to select a bike.
	 * <br>
	 * <br>
	 * The user has to enter the identification number (ID) of the
	 * chosen bike. The input must satisfy three conditions: it has
	 * to be an Integer, it can't be bigger than the size of the 
	 * ArrayList of bikes and it must be the ID of a bike that is 
	 * available. 
	 * <br>
	 * <br>
	 * <ul>
	 * <li>If the input is not an Integer, a <code>InputMismatchException</code>
	 * is raised and caught; the user will receive a warning message that reminds him/her 
	 * that the input must be a number.</li>
	 * <li>If the input is a number higher than the size of the ArrayList of 
	 * bikes, a <code>IndexOutOfBoundsException</code> is raised and caught; 
	 * the user will receive a warning message that tells him/her that the entered
	 * input is too high.</li>
	 * <li>If the input is a valid ID, but it is the ID of a bike that is not
	 * available, a generic <code>Exception</code> is raised and caught, and the 
	 * user will be warned that the chosen bike is not available.</li></ul>
	 * <br>
	 * <br>
	 * Each time an exception is thrown and caught, the <code>do-while</code>
	 * loop will restart, because the <code>inputOk</code> variable is not 
	 * set to <code>true</code>; this variable will be <code>true</code> if 
	 * and only if all the three conditions on the input are satisfied. When
	 * this happens, <code>!inputOk</code> will evaluate to false, and the 
	 * loop will end. 
	 * 
	 * @param arrayBikes 		an ArrayList of generic objects: it uses the ?
	 * 							notation, so any ArrayList is accepted. This is 
	 * 							done in order to be able to pass to this method
	 * 							both an ArrayList of <code>Bike</code> objects 
	 * 							and an ArrayList of <code>ElectricBike</code> 
	 * 							objects.	 
	 * 
	 * @return ID				an Integer representing the identification code
	 * 							of the bike that the user selected. 
	 */
	public static int selectBike(ArrayList<?> arrayBikes) {
		int ID = 0;
		Boolean inputOk = false;
		System.out.println("\n\t> Which bike do you want to book? Insert its ID.");
		System.out.print("\t  ----> ");

		do {
			try {
				Scanner input = new Scanner(System.in);
				ID = input.nextInt();
				if (((Bike)arrayBikes.get(ID)).getIsAvailable()) {
					inputOk = true;
				} else {
					throw new Exception();
				}
			} catch (InputMismatchException ex) {
				System.out.print("\t  ----> Enter an integer: ");
				continue;
			} catch (IndexOutOfBoundsException ex) {
				System.out.print("\t  ----> ID too big, enter it again: ");
				continue;
			} catch (Exception ex) {
				System.out.print("\t  ----> Bike not available, choose another one: ");
				continue;
			}
		} while (!inputOk);

		return ID;
	}


	/**
	 * This method is executed right before charging the user's credit card.
	 * It asks the user for an active confirm of his/her previous choices: 
	 * it displays the last four numbers of the user's credit card and the 
	 * price of the selected bike. If the user confirms the choice, the 
	 * booking will be completed, unless exceptions occur during the payment. 
	 * 
	 * The input can be: 
	 * <ul>
	 * <li>"quit": input accepted, the program will abort the current booking and
	 * will terminate.</li>
	 * <li>any other input: accepted, the user confirmed the previous choices 
	 * and his/her credit card will be charged.</li>
	 * </ul>
	 * 
	 * @param name					the name of the customer. 
	 * @param lastNumbers			last four digits of the customer's credit card.
	 * @param price					price of the bike chosen by the customer. 
	 */
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
			System.out.println("\tWe hope to see you again! Bye :)");
			System.exit(0);
		}
	}


	/**
	 * Updates the bikes' database, by removing the bike that the customer just booked.
	 * This methods checks whether the customer booked a normal bike or an electric bike, 
	 * and selects the appropriate database ("bikes.txt" or "electricbikes.txt"). It then
	 * proceeds to update the chosen database.
	 * <br>
	 * <br>
	 * Since it is not possible to change a single value in a single line of a text file,
	 * the only way to update the bikes' database is the following: 
	 * <ul>
	 * <li>Create a new temporary file.</li>
	 * <li>Copy the content of the old file into the temporary file, line by line</li>
	 * <li>When the line with the booked bike is about to be copied, the <code>lineNumber 
	 * == bikeID</code> boolean expression will evaluate to true, and that single line 
	 * will be modified, through the <code>replace(oldString, newString)</code> method. 
	 * This method will replace "true" with "false", removing the bike from the available
	 * bikes</li>
	 * <li>The lines after the one with the booked bike are copied without modifications.</li>
	 * <li>The old file is deleted.</li>
	 * <li>The temporary file is renamed, with the same name as the old file; this is needed
	 * because when the program will restart, it will load bikes from "bikes.txt" or from 
	 * "electricbikes.txt", and if it can't find one of those two files it will terminate.</li>
	 * </ul>
	 * 
	 * 
	 * @param electricOrNot				a Boolean value, it is true if the customer booked an
	 * 									electric bike, otherwise it is false. 
	 * @param bikeID					the identification number of the booked bike. 
	 * @throws IOException
	 */
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
