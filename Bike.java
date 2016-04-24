package javabikes;

/** 
 * Represents a bike in the JavaBikes' catalog. 
 */
public class Bike {

	/** 
	 * Identification number of the bike. 
	 */
	private int id;

	/** 
	 * Color of the bike. 
	 */
	private String color;

	/** 
	 * Type of the bike, can be "m" or "w" or "k". 
	 */
	private String bikeType;

	/** 
	 * Bike's price. 
	 */
	private int price;

	/** 
	 * Bike's availability, it is true if the bike can be booked, 
	 * otherwise it is false. 
	 */
	private Boolean isAvailable;


	/** 
	 * Contructor of the <code>Bike</code> object. 
	 *
	 * @param newId     		Identification number of the bike.
	 * 
	 * @param newColor    		Bike's color. 
	 * 
	 * @param newBikeType		Bike's type. A bike can be either for men, 
	 * 							women or kids. 
	 * 
	 * @param newPrice 			Price of the bike. 
	 * 
	 * @param newIsAvailable	It must be <code>true</code> or <code>
	 * 							false</code>. If it is <code>true</code>,
	 * 							the bike is in the database and it is available,
	 * 							otherwise if <code>false</code> the bike is 
	 * 							in the database but it's booked by someone else.
	 * 
	 */
	public Bike (int newId, String newColor, String newBikeType, int newPrice, Boolean newIsAvailable) {
		id = newId;
		color = newColor;
		bikeType = newBikeType;
		price = newPrice;
		isAvailable = newIsAvailable;
	}


	/** 
	 * Getter method, it gives back the bike's identification number.
	 * 
	 * @return          		integer number, equal to the bike's ID.
	 */	
	public int getBikeId() {
		return id;
	}


	/** 
	 * Getter method, it gives back the bike's color.
	 * 
	 * @return          		a string, equal to the bike's color.
	 */	
	public String getColor() {
		return color;
	}


	/** 
	 * Getter method, it gives back the bike's type as it is stored in
	 * the database.
	 * 
	 * @return          		a string, can be "m" (bike for men) or "w" 
	 * 							(bike for women) or "k" (bike for kids).
	 */	
	public String getBikeType() {
		return bikeType;
	}


	/** 
	 * Getter method, it gives back the bike's price.
	 * 
	 * @return          		integer number, equal to the bike's price.
	 */	
	public int getPrice() {
		return price;
	}


	/** 
	 * Getter method, it gives back <code>true</code> if the bike is 
	 * available, otherwise it gives back <code>false</code>.
	 * 
	 * @return          		Boolean value. 
	 */	
	public Boolean getIsAvailable() {
		return isAvailable;
	}


	/** 
	 * Custom getter, it gives back the bike's type in a printable format.
	 * 
	 * @return          		a string.
	 */	
	public String getBikeTypeString() {
		switch(bikeType) {
		case "m":
			return "for man";
		case "w":
			return "for women";
		case "k":
			return "for kids";
		default :
			return "";
		}
	}

}