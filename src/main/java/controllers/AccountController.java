package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import Service.AccountService;
import authorization.authorizeUser;
import models.AbstractAccount;

public class AccountController {
	private static AccountService as = new AccountService();
	private authorizeUser au = new authorizeUser();
	
	public List<AbstractAccount> findAll(HttpSession session){
		List<AbstractAccount> allAccounts = null;

		au.authorizeFindAll(session);

		allAccounts = as.findAll();
		return allAccounts;
	}
}
