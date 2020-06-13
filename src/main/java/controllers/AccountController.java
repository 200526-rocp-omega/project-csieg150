package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import Service.AccountService;
import models.AbstractAccount;

public class AccountController {
	private static AccountService as = new AccountService();
	
	public List<AbstractAccount> findAll(HttpSession session){
		return as.findAll();
	}
}
