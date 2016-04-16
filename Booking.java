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

	public static ArrayList<Bike> getBikes() throws IOException {
		// create an ArrayList object called arrayBikes
		ArrayList<Bike> arrayBikes = new ArrayList<Bike>();

		// get the current working directory
		Path workingDirectory = Paths.get("").toAbsolutePath();

		// get the full path to the bikes' database
		File path = new File (workingDirectory + "/javabikes/bikes.txt");

		// initialize the file reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

		// some temporary variables needed 
		String line;
		int tmpId = 0;

		/* read each line of the file, split it into the values array and add a Bike object to
		 * the bikess array. */
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");				
			arrayBikes.add(new Bike(tmpId, values[0], values[1], 
					Integer.parseInt(values[2]), 
					Boolean.parseBoolean(values[3]))); 
			tmpId += 1;
		}

		// return the bikes array
		return arrayBikes;
	}
	public static ArrayList<ElectricBike> getElectricBikes() throws IOException {
		// create an ArrayList object called arrayBikes
		ArrayList<ElectricBike> arrayBikes = new ArrayList<ElectricBike>();

		// get the current working directory
		Path workingDirectory=Paths.get("").toAbsolutePath();

		// get the full path to the bikes' database
		File path = new File (workingDirectory + "/javabikes/electricbikes.txt");

		// initialize the file reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

		// some temporary variables needed 
		String line;
		int tmpId = 0;

		/* read each line of the file, split it into the values array and add a Bike object to
		 * the bikess array. */
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");				
			arrayBikes.add(new ElectricBike(tmpId, values[0], values[1], 
					Integer.parseInt(values[2]), 
					Boolean.parseBoolean(values[3]),
					Integer.parseInt(values[4]))); 
			tmpId += 1;
		}

		// return the bikes array
		return arrayBikes;
	}

	public static void bikeBrowser(ArrayList<?> arrayBikes) {
		if (selectType()) {
			printBikes(arrayBikes, whichType());
		} else {
			printBikes(arrayBikes);
		}
	}
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
	public static void printBikes(ArrayList<?> arrayBikes) {
		System.out.println("\n\n\tAVAILABLE BIKES: \n");		

		if (arrayBikes.get(0) instanceof ElectricBike) {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-9s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object b : arrayBikes) {
				if (((ElectricBike) b).getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s | %-7s |\n",
							((ElectricBike) b).getBikeId(), ((ElectricBike) b).getColor(), ((ElectricBike) b).getPrice() + " kr.", 
							((ElectricBike) b).getBikeTypeString(), ((ElectricBike) b).getBatteryLevel() + " %", ((ElectricBike) b).getKmRange());
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object b : arrayBikes) {
				if (((Bike) b).getIsAvailable()) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							((Bike) b).getBikeId(), ((Bike) b).getColor(), ((Bike) b).getPrice() + " kr.", ((Bike) b).getBikeTypeString());
				}
			}
		}
	}
	public static void printBikes(ArrayList<?> arrayBikes, String bikeType) {
		System.out.println("\n\n\tAVAILABLE BIKES: \n");		
		if (arrayBikes.get(0) instanceof ElectricBike) {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s | %-7s | %-9s |\n", "ID", "Color", "Price", "Bike type", "Battery", "KM Range");
			for (Object b : arrayBikes) {
				if (((ElectricBike) b).getIsAvailable() && ((ElectricBike) b).getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s | %-7s |\n",
							((ElectricBike) b).getBikeId(), ((ElectricBike) b).getColor(), ((ElectricBike) b).getPrice() + " kr.", 
							((ElectricBike) b).getBikeTypeString(), ((ElectricBike) b).getBatteryLevel() + " %", ((ElectricBike) b).getKmRange());
				}
			}
		} else {
			System.out.printf("\t  | %-2s | %-7s | %-6s | %-9s |\n", "ID", "Color", "Price", "Bike type");
			for (Object b : arrayBikes) {
				if (((Bike) b).getIsAvailable() && ((Bike) b).getBikeType().equals(bikeType)) {
					System.out.printf("\t  | %-2d | %-7s | %6s | %-9s |\n",
							((Bike) b).getBikeId(), ((Bike) b).getColor(), ((Bike) b).getPrice() + " kr.", ((Bike) b).getBikeTypeString());
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
