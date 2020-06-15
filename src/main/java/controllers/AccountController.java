package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import Service.AccountService;
import models.AbstractAccount;
import models.AbstractUser;

public class AccountController {
	private static AccountService as = new AccountService();
	
	public List<AbstractAccount> findAll(HttpSession session){
		return as.findAll();
	}
	
	public AbstractAccount findAccountById(int accountId) {			
		return as.findByID(accountId);
	}
	
	public boolean isOwner(HttpSession session, int accountId) {
		// Checks our current user's ID and see if it matches any owner ids from the provided account ID
		AbstractUser u = (AbstractUser) session.getAttribute("currentuser");
		return as.userIsOwner(u.getUserId(), accountId);
	}
}
