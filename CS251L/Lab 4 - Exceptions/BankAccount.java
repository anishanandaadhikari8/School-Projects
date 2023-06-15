// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 4 - Exceptions
// First lab using exceptions. Withdraw() will throw an InsufficientFundsException
// if the withdrawl is too large for the account.

public class BankAccount {
    int m_accountID;
    double m_balance;
    
    public BankAccount(int accountNum) {
        m_accountID = accountNum;
        m_balance = 0;
    }
    
    /**
     * Getter for account number.
     */
    public int getAccountID() {
        return m_accountID;
    }
    
    /**
     * Getter for account balance.
     */
    public double getBalance() {
        return m_balance;
    }
    
    /**
     * Method for adding money to your bank account balance
     * @param deposit
     *          Amount to deposit.
     */
    public void deposit(double deposit) {
        m_balance += deposit;
    }
    
    /**
     * Attempts to withdraw an amount from the bank account balance.
     * If the amount is too large an exception will be thrown.
     * @param withdrawl
     *          Amount to be subtracted from the account balance.
     * @throws InsufficientFundsException
     *          Exception that is thrown when account balance would be
     *          brought below 0
     */
    public void withdraw(double withdrawl) throws InsufficientFundsException {
        
        if(m_balance - withdrawl < 0) {
            throw new InsufficientFundsException(Math.abs(m_balance - withdrawl));
        }
        else {
            m_balance -= withdrawl;
        }
    }
    
}
