package javabikes;
/**
 * A dummy exception. 
 */
public class CustomException extends Exception
{
    //Parameterless Constructor
    public CustomException() {}

    //Constructor that accepts a message
    public CustomException(String message)
    {
       super(message);
    }
}