package javabikes;
import java.util.Scanner;
/**
 * This class represents a customer of the 
 * JavaBikes self-service rental bike system. 
 */
public class Customer { 
	/**
	 * Name of the customer.
	 */
	private String name;
	
	/**
	 * Surname of the customer.
	 */
	private String surname;
	
	/**
	 * Number of the customer's document.
	 */
	private String document;
	
	/**
	 * Indicates whether the customer resides in Denmark or not.
	 */
	private Boolean isDanish;
	
	/**
	 * E-mail address of the customer, useful for sending the invoice
	 * or for reaching out in case of issues. 
	 */
	private String email;

	/** 
	 * Contructor of the <code>Customer</code> object. This constructor
	 * takes zero parameters, and uses some methods that will prompt the 
	 * user to enter its info.
	 */	
	Customer() { 
		name = inputName(); 
		surname = inputSurname();
		isDanish = inputIsDanish();
		document = inputDocument(); 
		email = inputEmail();
	} 

	/** 
	 * Getter method, it gives back the customer's name.
	 * 
	 * @return          		String.
	 */
	public String getName() {
		return name;
	}

	/** 
	 * Getter method, it gives back the customer's surname.
	 * 
	 * @return          		String.
	 */
	public String getSurname() {
		return surname;
	}

	/** 
	 * Getter method, it gives back the customer's document code. 
	 * If the customer is resident in Denmark, it will give back 
	 * the CPR number; otherwise, it will give back the passport 
	 * number.
	 * 
	 * @return          		String.
	 */
	public String getDocument() {
		return document;
	}

	/** 
	 * Getter method, it return <code>true</code> if the customer
	 * is resident in Denmark, otherwise it will give back <code>false</code>.
	 * 
	 * @return          		Boolean.
	 */
	public Boolean getIsDanish() {
		return isDanish;
	}
	
	
	/** 
	 * Getter method, it gives back the customer's e-mail address.
	 * 
	 * @return          		String.
	 */
	public String getEmail() {
		return email;
	}

	/** 
	 * This method prompts the user to enter his/her name.
	 * <br>
	 * <br>
	 * The input is checked against two different conditions:
	 * <ul><li><code>name.length() == 0</code>: the length of the name must 
	 * be bigger than zero, in order to exit the <code>do-while</code> loop.</li>
	 * <li>The name must match the <code>[A-Z][a-z]+</code> regular expression: this
	 * expression checks if the name has a first capital letter, followed by an indefinite
	 * amount of letters. This needs to be done in order to force the user to enter
	 * his/her real name. The regular expression will be changed in the future, to accept
	 * other legitimate names that are currently reject (such as Ann-Sophie). </li></ul>
	 * 
	 * @return          		String.
	 */
	private String inputName() {
		Scanner input = new Scanner(System.in);
		System.out.println("\t§ Enter your first name: ");
		do {
			System.out.print("\t  ----> ");
			name = input.nextLine();
		} while (name.length() == 0 || !(name.matches("[A-Z][a-z]+")));
		return name;
	}

	/** 
	 * This method prompts the user to enter his/her surname.
	 * <br>
	 * <br> 
	 * The input is checked against two different conditions:
	 * <ul><li><code>surname.length() == 0</code>: the length of the surname must 
	 * be bigger than zero, in order to exit the <code>do-while</code> loop.</li>
	 * <li>The surname must match the <code>[A-Z][a-z]+</code> regular expression: this
	 * expression checks if the surname has a first capital letter, followed by an indefinite
	 * amount of letters. This needs to be done in order to force the user to enter
	 * his/her real surname. The regular expression will be changed in the future, to accept
	 * other legitimate surnames that are currently reject (such as o'Brien). </li></ul>
	 * 
	 * @return          		String.
	 */
	private String inputSurname() {
		Scanner input = new Scanner(System.in);
		System.out.println("\t§ Enter your last name: ");
		do {
			System.out.print("\t  ----> ");
			surname = input.nextLine();
		} while (surname.length() == 0 || !(name.matches("[A-Z][a-z]+")));
		return surname;
	}

	/** 
	 * This method asks the user if he/she resides in Denmark. This information 
	 * is needed because if the customer is resident in Denmark, the document 
	 * asked will be the CPR number, otherwise it will be the passport number. 
	 * <br>
	 * <br>
	 * The user has to reply "yes" or "no", any other input will result in the 
	 * do-while's condition evaluating to true, and the loop will be executed again. 
	 * <br>
	 * <br>
	 * Once the user enters a correct input, this input is checked again: if it is "yes", 
	 * then <code>isDanish</code> will be true, otherwise <code>isDanish</code> will be false. 
	 * 
	 * @return          		Boolean.
	 */
	private Boolean inputIsDanish() {
		String danish;
		Scanner input = new Scanner(System.in);
		System.out.println("\t§ Are you a Danish resident? Enter \"yes\" or \"no\": ");
		do {
			System.out.print("\t  ----> ");
			danish = input.nextLine().toLowerCase();
		} while (!(danish.equals("yes")) && !(danish.equals("no")));	

		// set the boolean variable isDanish to true or false, based on the user's previous answer
		if (danish.matches("yes")) {
			isDanish = true;
		} else {
			isDanish = false;
		};
		return isDanish;
	}

	/** 
	 * This method is used to get the users' document number.
	 * <br>
	 * <br> 
	 * If the customer resides in Denmark, he/she will be asked to enter the 
	 * CPR number, that will be checked against the <code>\d{6}-\d{4}</code> regular 
	 * expression (six digits, a "-" and then 4 digits). Until the user's input
	 * doesn't match the regular expression, the <code>do-while</code> loop will 
	 * continue to be executed. 
	 * <br>
	 * <br>
	 * If the customer does not reside in Denmark, he/she will be asked to enter
	 * the passport number. In this case, the only condition on the input is that it
	 * must be longer than 0. Checking if the passport number is valid will be implemented
	 * in the future, since the great diversity in the format of this numbers can lead to 
	 * implementation bugs, if not done cautiously.
	 * 
	 * @return          		String.
	 */
	private String inputDocument() {
		Scanner input = new Scanner(System.in);
		if (isDanish) {
			System.out.println("\t§ Enter your CPR number, in the xxxxxx-yyyy format: ");
			do {
				System.out.print("\t  ----> ");
				document = input.nextLine();	
			} while (!document.matches("\\d{6}-\\d{4}"));
		} else {
			System.out.println("\t§ Enter your passport number: ");
			do {
				System.out.print("\t  ----> ");
				document = input.nextLine();
			} while (document.length() == 0);
		}
		return document;
	}

	/** 
	 * This method is used to get the users' e-mail address. 
	 * <br>
	 * <br>
	 * The input is checked against a regular expression, that will evaluate
	 * whether it is or not a valid email address. 
	 * <br>
	 * <br>
	 * A valid e-mail address is composed only by letters (both uppercase 
	 * and lowercase), numbers and some symbols (only ".", "_" or "-"). It
	 * then has a "@" symbol, followed by the domain. The domain must be composed
	 * by at least two strings, with a "." between them. The first string can have
	 * both letters, numbers and the "-" symbol, while the last string must have only 
	 * letters, and at least two of them.
	 * <br>
	 * <br>
	 * The regular expression can be divided as follows: 
	 * <ul><li><code>[_A-Za-z0-9-\\+]+</code> it must start with a string with only letters,
	 * numbers and some symbols.</li>
	 * <li><code>(\\.[_A-Za-z0-9-]+)*</code> same as before, it is an optional (because of the *) substring; 
	 * describes the case if the e-mail address has a "." in it.</li>
	 * <li><code>@</code> must be present in any e-mail.</li>
	 * <li><code>[A-Za-z0-9-]+</code> almost the same as the first string, the only difference is that it 
	 * does not accept the "_" character.</li>
	 * <li><code>(\\.[A-Za-z0-9]+)*</code> almost the same as the second string ("_" is not accepted), it
	 * is optional.</li>
	 * <li><code>(\\.[A-Za-z]{2,})</code> checks the Top-Level Domain of the e-mail. To 
	 * be valid, it must be longer than two characters, and those characters can be only letters.</li> 
	 * </ul>
	 * @return          		String.
	 */
	private String inputEmail() {
		Scanner input = new Scanner(System.in);
		System.out.println("\t§ Enter your email address: ");
		do {
			System.out.print("\t  ----> ");
			email = input.nextLine();
		} while (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"));
		return email;
	}
} 