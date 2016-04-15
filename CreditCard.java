package javabikes;

import java.util.Calendar;
import java.util.Scanner;

public class CreditCard {

	private String number;
	private String expiryDate;
	private String cvvCode;

	CreditCard() { 
		System.out.println("\n\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("\n\t• Enter your credit card data.");
		System.out.println("\t  > Remember: ");
		System.out.println("\t    - We accept only MasterCard and Visa cards.");
		System.out.println("\t    - The expiry date must be in format MM/YYYY");
		System.out.println("\t    - Type \"quit\" at any time to cancel the payment and your reservation.\n");
		number = inputNumber(); 
		expiryDate = inputExpiryDate();
		cvvCode = inputCvvCode(); 
	} 

	public String getNumber() {
		return number;
	}

	public String getHiddenNumber() {
		return number.substring(number.length()-4);
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getCvvCode() {
		return cvvCode;
	}

	private String inputNumber() {
		Scanner input = new Scanner(System.in);
		String cardNumber;
		do {
			do {
				System.out.print("\t  * Card Number ----> ");
				cardNumber = input.nextLine();
				if (cardNumber.equals("quit")) {
					System.out.println("\tSorry to see you go. See you soon!");
					System.exit(0);
				}
			} while (!cardNumber.matches("\\d{16}") && !cardNumber.matches("\\d{13}") && !cardNumber.matches("\\d{19}"));
		} while(!checkCardNumber(cardNumber));
		return cardNumber;
	}


	private String inputExpiryDate() {
		Scanner input = new Scanner(System.in);
		String expDate;
		int month;
		int year;
		Calendar c = Calendar.getInstance();
		int yearToday = c.get(Calendar.YEAR);
		int monthToday = c.get(Calendar.MONTH);
		Boolean dateChecker = false;
		do {
			do {
				System.out.print("\t  * Expiry Date ----> ");
				expDate = input.nextLine();
				if (expDate.equals("quit")) {
					System.out.println("\tSorry to see you go. See you soon!");
					System.exit(0);
				}
			} while (!expDate.matches("\\d{2}/\\d{4}"));
			month = Integer.parseInt(expDate.substring(0, 2));
			year = Integer.parseInt(expDate.substring(3, 7));
			if (year > yearToday) {
				dateChecker = true;
			} else if (year == yearToday && month > monthToday) {
				dateChecker = true;
			} else {
				System.out.println("\t    >> Attention: this credit card is expired.");
			}
		} while (!(month <= 12 && month > 0 && year > 0 && dateChecker));
		return expDate;
	}

	private String inputCvvCode() {
		String cvvCode;
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("\t  * CVV Code -------> ");
			cvvCode = input.nextLine();
			if (cvvCode.equals("quit")) {
				System.out.println("\tSorry to see you go. See you soon!");
				System.exit(0);
			}
		} while (!cvvCode.matches("\\d{3}"));
		return cvvCode;
	}

	public void makePayment() {
		System.out.println("\n\t>> We are processing your payment, please wait. <<");
		System.out.print("\t   Loading..  [");
		for (int i = 0; i < 31; i++) {
			System.out.print("=");
			try {
				Thread.sleep(75);
			} catch (InterruptedException ex) {
				System.out.println("Payment aborted. Exiting...");
				System.exit(0);
			} 
		}
		System.out.println("]  OK!\n");
	}

	private static Boolean checkCardNumber(String cardNumber) {
		String number = new StringBuilder(cardNumber).reverse().toString();
		int sumOdd = 0;
		int sumEven = 0;
		int multiplyEven = 0;
		Boolean validNumber;
		Boolean validVisaOrMc = false;

		for (int i = 0; i < number.length(); i++) {
			int currentDigit = Character.digit(number.charAt(i), 10);
			if (i % 2 == 0) {
				sumOdd += currentDigit;
			} else {
				multiplyEven = currentDigit * 2;
				if (multiplyEven > 9) {
					int d1 = multiplyEven % 10;
					int d2 = multiplyEven / 10;
					sumEven += d1 + d2;
				} else {
					sumEven += multiplyEven;
				}
			}
		}
		validNumber = (sumOdd + sumEven) % 10 == 0;
		if (cardNumber.startsWith("4")  ||
				cardNumber.startsWith("51") ||
				cardNumber.startsWith("52") ||
				cardNumber.startsWith("53") ||
				cardNumber.startsWith("54") ||
				cardNumber.startsWith("55")) {
			validVisaOrMc = true;
		}
		return validNumber && validVisaOrMc;
	}

}
