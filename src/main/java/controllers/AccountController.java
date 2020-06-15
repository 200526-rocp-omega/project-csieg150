package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import Service.AccountService;
import exceptions.FailedStatementException;
import models.AbstractAccount;
import models.AbstractUser;
import templates.BalanceTemplate;
import templates.PostAccountTemplate;

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
		if(as.insert(postedAccount.toAccount()) < 1) throw new FailedStatementException(); // Insert into record Account table
		as.addUserAccount(postedAccount.getUserId(), postedAccount.getAccountId()); // Add relationship to Users-Accounts table
		return this.findAccountById(postedAccount.getAccountId());
	}
	
	public AbstractAccount update(AbstractAccount account) {
		return as.update(account);
	}
	
	public AbstractAccount withdraw(BalanceTemplate withdraw) {
		return as.withdraw(withdraw.getAccountId(), withdraw.getAmount());
	}
}
