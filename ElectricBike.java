package javabikes;

/**
 * Represents an ElectricBike in the JavaBikes' catalogue. 
 * The ElectricBike class inherits all its fields from 
 * the Bike class, and adds some battery-related informations.
 *
 */
public class ElectricBike extends Bike {
	/**
	 * Battery level, in percentage, of the bike.
	 */
	private int battery;
	
    /** 
     * Contructor of the <code>ElectricBike</code> object. This object
     * inherits all the <code>Bike</code> parameters and methods,
     * and then it adds some.
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
     * @param bikeBattery		Integer number, its value is the percentage charge
     * 							of the bike's battery
     * 
     */
	public ElectricBike(int newId, String newColor, String newBikeType, 
						int newPrice, Boolean newIsAvailable, int bikeBattery) {
		super(newId, newColor, newBikeType, newPrice, newIsAvailable);
		battery = bikeBattery;
	}
	
    /** 
     * Getter method, it gives back the bike's battery level.
     * 
     * @return          		integer number, equal to the bike battery level.
     */	
	public int getBatteryLevel() {
		return battery;
	}
	
    /** 
     * Custom getter, it gives back the bike's range, in km. More testing has to be done
     * to establish the mathematical relationship between the battery level (expressed as 
     * a percentage) and the bike's range. At the moment, it seems that an increase of 
     * 2% in the battery level adds 1 kilometer to the bike' range.
     * 
     * @return          		integer number, equal to the bike's range in kilometres.
     */
	public int getKmRange() {
		return battery / 2;
	}
}
