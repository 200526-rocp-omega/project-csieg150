package Service;

import java.util.List;

import dao.AccountDAO;
import exceptions.FailedStatementException;
import models.AbstractAccount;

public class AccountService {
private static AccountDAO aDAO = new AccountDAO();
	
	public int insert(AbstractAccount u) {
		return aDAO.insert(u);
	}
	
	public List<AbstractAccount> findAll(){ // Pass the current account-list
		return aDAO.findAll(); // No other logic needed 
	}
	
	public AbstractAccount findByID(int id) {
		return aDAO.findByID(id);		
	}
	
	public AbstractAccount update(AbstractAccount a) {
		int result = aDAO.update(a);
		if(result != 1) {
			throw new FailedStatementException();
		}
		return aDAO.findByID(a.getAccountId()); // Returns appropriate record to verify update
	}
	
	public AbstractAccount withdraw(int accountId, double amount) {
		// Given the current user and the account they want to withdraw from, how much? 
		// If the amount is greater than balance or less than zero, throw an error
		if(amount < 0) {
			throw new FailedStatementException();
		}
		AbstractAccount dbAccount = aDAO.findByID(accountId);
		double accountFunds = dbAccount.getBalance();
		
		if(accountFunds - amount < 0) { // Can't overdraw, could set some minimum alternatively.
			throw new FailedStatementException();
		}
		aDAO.updateBalance(accountId, (accountFunds - amount));
	
		return aDAO.findByID(accountId);
	}
	
	public AbstractAccount deposit(int accountId, double amount) {
		if(amount < 0) {
			throw new FailedStatementException();
		}
		AbstractAccount dbAccount = aDAO.findByID(accountId);
		double accountFunds = dbAccount.getBalance();
		
		aDAO.updateBalance(accountId, (accountFunds + amount));
	
		return aDAO.findByID(accountId);
	}
}
