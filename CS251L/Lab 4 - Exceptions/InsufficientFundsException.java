// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 4 - Exceptions
// Inherited Exception class for use with BankAccount class
@SuppressWarnings("serial")
public class InsufficientFundsException extends Exception {

    double m_shortfall;
    
    /**
     * Constructor that takes shortfall amount as a parameter
     * @param shortfall
     */
    public InsufficientFundsException(double shortfall) {
        super("Not enough money!");
        m_shortfall = shortfall;
    }
    
    /**
     * Getter for m_shortfall
     */
    public double getShortfall() {
        return m_shortfall;
    }
    
}
