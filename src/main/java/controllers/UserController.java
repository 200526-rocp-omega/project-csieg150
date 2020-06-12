package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import Service.UserService;
import authorization.authorizeUser;
import models.AbstractUser;

public class UserController {
private UserService us = new UserService(); // Lets us access User Service methods 
private authorizeUser au = new authorizeUser();
	
	public AbstractUser accessUser(HttpSession session, String id) 
			throws ServletException, IOException{ // Fetched when the page is loaded normally

		int userId = au.authorizeUserAccess(session,id);
		
		return us.findByID(userId);
	}
	
	public List<AbstractUser> findAll(HttpSession session){
		List<AbstractUser> allUsers = null;
		
		au.authorizeFindAll(session);
		
		allUsers = us.findAll();
		return allUsers;
	}
	
	public AbstractUser updateUser(HttpSession session, AbstractUser u) { // Authorizes and allows for update.
		au.authorizeUpdate(session, u);
		
		return us.update(u);
	}
}
