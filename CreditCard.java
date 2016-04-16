package javabikes;

import java.util.Calendar;
import java.util.Scanner;

/**
 * Represents the credit card used by the customer 
 * to pay for the bike's rent. At the moment, the system
 * accepts only valid MasterCard and Visa cards. 
 */
public class CreditCard {

	/**
	 * Number of the credit card. It is saved as a 
	 * String, since it is easier to manipulate.
	 */
	private String number;
	
	/**
	 * Expiry date of the credit card, saved in 
	 * the MM/YYYY format. 
	 */
	private String expiryDate;
	
	/**
	 * CVV code of the credit card. 
	 */
	private String cvvCode;
	
	
	/**
	 * Constructor of the <code>CreditCard</code> object. It 
	 * prints out some useful informations, and then it 
	 * asks the user to enter the card's data through the
	 * <code>inputNumber()</code>, <code>inputExpiryData()</code> and 
	 * <code>inputCvvCode()</code> methods. 
	 */
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

	/** 
	 * Getter method, it gives back the card's number.
	 * 
	 * @return          		String.
	 */
	public String getNumber() {
		return number;
	}

	/** 
	 * Custom getter method, it gives back the last four
	 * digits of the card's number. 
	 * 
	 * @return          		String.
	 */
	public String getHiddenNumber() {
		return number.substring(number.length()-4);
	}

	/** 
	 * Getter method, it gives back the card's expiry
	 * date. 
	 * 
	 * @return          		String.
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/** 
	 * Getter method, it gives back the card's CVV
	 * code.
	 * 
	 * @return          		String.
	 */
	public String getCvvCode() {
		return cvvCode;
	}
	
	
	/** 
	 * This methods prompts the user to enter the number of 
	 * the credit card he/she wishes to use for the payment. 
	 * <br>
	 * <br>
	 * The input is checked twice:
	 * <ul><li>In order to exit the inner do-while loop, the 
	 * input must be composed only by a fixed number of digits. 
	 * This number is equal to 13, 16 or 19, which is the number
	 * of digits of valid Mastercard and Visa cards. </li>
	 * <li>In order to exit the outer do-while loop, the input
	 * must be a valid credit card number. The method 
	 * <code>checkCardNumber(cardNumber)</code> is explained below; 
	 * it returns true if the number is valid, otherwise
	 * it returns false</li></ul>
	 * <br>
	 * The only other input that is accepted is the string "quit". 
	 * If the user enters this string, the booking is aborted and
	 * the program terminates. 
	 * 
	 * @return 				String. 
	 */
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


	/** 
	 * This methods prompts the user to enter the expiry date of 
	 * the credit card he/she wishes to use for the payment. 
	 * <br>
	 * <br>
	 * The input is checked twice:
	 * <ul><li>In order to exit the inner do-while loop, the 
	 * input must respect the fixed expiry date format: it is checked
	 * against a regular expression that allows only a MM/YYYY input.</li>
	 * <li>In order to exit the outer do-while loop, the input
	 * must be a valid expiry date. A valid date can be either in the
	 * current year (but the month must be greater than the current month) or 
	 * in the following years (any month is fine). To check if 
	 * this is true, the input is splitted in two variables (month 
	 * and year) and they are checked against the current month and year,
	 * received from the Calendar object. If the date is valid, the 
	 * <code>dateChecker</code> variable will be set to true.</li></ul>
	 * <br>
	 * A remark: the outer do-while loop would be satisfied also with a 
	 * date such as 18/2020, but months exist only between 1 and 12; in order
	 * to avoid any possible errors, the outer do-while loop exits only if 
	 * <code>dateChecker</code> is true AND the month is not bigger or equal
	 * to 12. 
	 * <br>
	 * <br>
	 * The only other input that is accepted is the string "quit". 
	 * If the user enters this string, the booking is aborted and
	 * the program terminates. 
	 * 
	 * @return 				String. 
	 */
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
		} while (!(month <= 12 && dateChecker));
		return expDate;
	}

	/** 
	 * This methods prompts the user to enter the CVV code of 
	 * the credit card he/she wishes to use for the payment. 
	 * <br>
	 * <br>
	 * The CVV code does not have a fixed template or style: the 
	 * only condition that the input must satisfy in order to be 
	 * valid is that it must be composed by three digits. 
	 * <br>
	 * <br>
	 * The only other input that is accepted is the string "quit". 
	 * If the user enters this string, the booking is aborted and
	 * the program terminates. 
	 * 
	 * @return 				String. 
	 */
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

	/** 
	 * This methods fakes the payment. It just prints out 
	 * a loading bar, by using the <code>Thread.sleep()</code> method in a 
	 * <code>for</code> loop. The loop is executed 31 times (just because
	 * this number fits the width of the other output of the program); each 
	 * time it is executed, it prints out a "=" and then it stops for 75 milliseconds. 
	 * <br>
	 * <br>
	 * If the <code>Thread.sleep()</code> is interrupted, it will raise an 
	 * exception that is being cought; this exception will cause the payment
	 * to be aborted, and the program to exit. 
	 * 
	 */
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

	
	/** 
	 * This methods checks whether the card number is valid. Since
	 * the programs accepts only valid MasterCard and Visa cards,
	 * the number must start with either "4" (Visa) or "51-55" 
	 * (MasterCard). Moreover, the number of a credit card is not 
	 * random: it must be checked against the Luhn's algorithm, that 
	 * can detect single-digits errors and almost all transposition
	 * of adjacent digits.
	 * <br>
	 * <br>
	 * To check if a card number satisfies the Luhn's algorithm, the 
	 * process is fairly simple:
	 * <ul><li>Reverse the number: first line of the method.</li>
	 * <li>Sum all the odd digits: <code>sumOdd += currentDigit</code>.</li>
	 * <li>Multiply each even digit by two; if the result is greater 
	 * than nine, sum the digits of the result. Then, sum all the partial
	 * results (either evenDigit*2 or sum of the digits of evenDigit*2). This
	 * sum is stored in the <code>sumEven</code> variable. </li>
	 * <li>Only if the sum of <code>sumOdd</code> and <code>sumEven</code> can be
	 * divided by 10, the number is valid.</li></ul>
	 * <br>
	 * If the number is valid, <code>validNumber</code> will be true, otherwise, 
	 * it will be false.
	 * <br>
	 * <br>
	 * To check if the card is a valid MasterCard or Visa, it is enough 
	 * to check the first one or two digits of it:
	 * <ul><li>If the first digit is a "4", it is a valid Visa card.</li>
	 * <li>If the first digit is a "5", then the second digit must be checked, and it
	 * must be equal to a number between 1 and 5.</li></ul>
	 * <br>
	 * If the number starts with a the right digit, <code>validVisaOrMc</code>
	 * will be true, otherwise it will be false. 
	 * <br>
	 * <br>
	 * This method will return true if and only if the number satisfies the 
	 * Luhn's algorithm AND it is a MasterCard or Visa card: 
	 * <code>return validNumber AND validVisaOrMc</code>.
	 * 
	 * @param cardNumber 	number of the credit card that has to be checked.  
	 * 
	 * @return 				Boolean
	 */
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
