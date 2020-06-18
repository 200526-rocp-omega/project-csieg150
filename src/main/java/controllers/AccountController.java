package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import Service.AccountService;
import Service.UserService;
import exceptions.FailedStatementException;
import models.AbstractAccount;
import models.AbstractUser;
import models.UserAccount;
import templates.BalanceTemplate;
import templates.PostAccountTemplate;
import templates.TransferTemplate;
import templates.UserAccountTemplate;

public class AccountController {
	private static AccountService as = new AccountService();
	
	public List<AbstractAccount> findAll(){
		return as.findAll();
	}
	
	public List<AbstractAccount> findByStatus(int statusId){
		return as.findByStatus(statusId);
	}
	
	public List<AbstractAccount> findByOwner(int userId){
		return as.findByOwner(userId);
	}
	
	public AbstractAccount findAccountById(int accountId) {			
		return as.findByID(accountId);
	}
	
	public boolean isOwner(HttpSession session, int accountId) {
		// Checks our current user's ID and see if it matches any owner ids from the provided account ID
		AbstractUser u = (AbstractUser) session.getAttribute("currentuser");
		return as.userIsOwner(u.getUserId(), accountId);
	}
	
	public AbstractAccount insert(PostAccountTemplate postedAccount) {
		// Take info from posted account object and add records to the appropriate tables.
		AbstractAccount account = postedAccount.toAccount();
		if(as.insert(account) < 1) throw new FailedStatementException(); // Insert into record Account table
		as.addUserAccount(postedAccount.getUserId(), account.getAccountId()); // Add relationship to Users-Accounts table
		return this.findAccountById(account.getAccountId());
	}
	
	public void addUserAccount(UserAccountTemplate userAccount, int currentUserId) {
		// Adds a UserAccount pair to our USERS-ACCOUNTS table
		
		if(as.userIsOwner(currentUserId, userAccount.getAccountId())) { // If our USER is an owner of the account
			as.addUserAccount(userAccount.getUserId(), userAccount.getAccountId()); // Add the new joint user to the account
			
		} else {
			
			UserService us = new UserService(); // We will need to find the Roles of owners, so we call user services
			
			for(UserAccount owner : as.ownersOfAccount(userAccount.getAccountId())) { // For every User/Account pair with the right account ID
				
				AbstractUser user = us.findByID(owner.getUserId()); // Get the user information from our User service
				if(user.getRole().getRoleId() > 1) { // If the owner is Premium / Employee / Admin
					as.addUserAccount(userAccount.getUserId(), userAccount.getAccountId()); // Add the pair
					return; // exit the method, we don't want to add multiple accounts on accident.
				}
			}
		}
		
	}
	
	public AbstractAccount update(AbstractAccount account) { // Update the selected account 
		return as.update(account);
	}
	
	public AbstractAccount withdraw(BalanceTemplate withdraw) { // Withdraw from the specified account
		return as.withdraw(withdraw.getAccountId(), withdraw.getAmount());
	}
	
	public AbstractAccount deposit(BalanceTemplate deposit) { // Deposit to the specified account
		return as.deposit(deposit.getAccountId(), deposit.getAmount());
	}
	
	public void transfer(TransferTemplate transfer) {
		// The below simply withdraws from the Source account ID and deposits into the Target account ID in the same ammount. 
		this.withdraw(new BalanceTemplate(transfer.getSourceAccountId(),transfer.getAmount()));
		this.deposit(new BalanceTemplate(transfer.getTargetAccountId(),transfer.getAmount()));
	}
	
	public void passTime(int numOfMonths) {
		// Give all savings accounts "numOfMonths" amount of interest.
		as.passTime(numOfMonths);
	}
	
	public List<AbstractAccount> findByOwnerAndStatus(int userId, int statusId){
		// Find Accounts belonging to the specified userId with a specific status
		List<AbstractAccount> results =  as.findByStatusAndOwner(userId, statusId);
		if(results.isEmpty()) {
			return null; // If no results just return null
		}
		return results;
	}
}
