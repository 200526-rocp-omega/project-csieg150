package Service;

import java.util.ArrayList;
import java.util.List;

import dao.AccountDAO;
import dao.UserAccountDAO;
import exceptions.FailedStatementException;
import exceptions.IllegalBalanceException;
import models.AbstractAccount;
import models.UserAccount;

public class AccountService {
private static AccountDAO aDAO = new AccountDAO();
private static UserAccountDAO uaDAO = new UserAccountDAO();
	
	public int insert(AbstractAccount u) {
		return aDAO.insert(u);
	}
	
	public List<AbstractAccount> findAll(){ // Pass the current list of accounts
		return aDAO.findAll(); // No other logic needed 
	}
	
	public AbstractAccount findByID(int id) { // Find the record with the appropriate ID
		return aDAO.findByID(id);		
	}
	
	public AbstractAccount update(AbstractAccount a) { // Update the associated ID with the new record.
		int result = aDAO.update(a);
		if(result != 1) { // If we updated more or less than 1 row something went wrong
			throw new FailedStatementException(); // throw exception
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
			throw new IllegalBalanceException();
		}
		aDAO.updateBalance(accountId, (accountFunds - amount));
	
		return aDAO.findByID(accountId);
	}
	
	public AbstractAccount deposit(int accountId, double amount) {
		if(amount < 0) {
			throw new IllegalBalanceException();
		}
		
		double accountFunds = aDAO.findByID(accountId).getBalance();
		
		aDAO.updateBalance(accountId, (accountFunds + amount));
	
		return aDAO.findByID(accountId);
	}
	
	public List<UserAccount> ownersOfAccount(int accountId) {
		// Finds the account associated with the ID, then finds all users related to it
		return uaDAO.findUsersByAccount(this.findByID(accountId)); 
	}
	
	public boolean userIsOwner(int userId, int accountId) {
		// Grabs the list of associated users and then checks if the given userId is in that list.
		List<UserAccount> usersList = this.ownersOfAccount(accountId); 
		for(UserAccount userAccount : usersList) { // For each found result
			if(userAccount.getUserId() == userId) { // compare if the userIds match
				return true; // If so return true
			}
		}
		return false; // If no match is found, return false.
	}
	
	public List<AbstractAccount> findByStatus(int statusId){
		// Find all accounts of a specified status ID
		return aDAO.findByStatus(statusId);
	}
	
	public List<AbstractAccount> findByOwner(int userId){
		
		List<UserAccount> userAccounts = uaDAO.findAccountsByUser(userId); // Grab the associated user/accounts
		
		if(userAccounts.isEmpty()) { // If the list is empty 
			throw new FailedStatementException(); // Throw an exception
		}
		
		List<AbstractAccount> accounts = new ArrayList<>(); // Make our empty List
		for(UserAccount ua : userAccounts) { //For every user/account pair
			accounts.add(this.findByID(ua.getAccountId())); // Find the associated Account with the specified ID
		}
		
		return accounts;
	}
	
	public void addUserAccount(int userId, int accountId) {
		// Adds our pair to the USERS-ACCOUNTS table.
		if(uaDAO.insert(userId, accountId) < 1) throw new FailedStatementException();
	}
}
