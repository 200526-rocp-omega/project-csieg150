package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import Service.UserService;
import exceptions.NotLoggedInException;
import models.AbstractUser;

public class UserController {
private UserService us = new UserService(); // Lets us access User Service methods 
public static final int EMPLOYEE_ROLE = 3; // Here to check if current user is Employee/admin or not. Put as variable for easy access and repeated use 
	
	public AbstractUser accessUser(HttpSession session, String id) 
			throws ServletException, IOException{ // Fetched when the page is loaded normally

		
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		int userId = -1;
		try {
			userId = Integer.parseInt(id); // Gets the integer value of the 2nd part of the URI
		} catch(NumberFormatException e) {
			userId = -99;
		}
		if((userId != currentUser.getUserId()) && (currentUser.getRole().getRoleId() < EMPLOYEE_ROLE)) {
			throw new NotLoggedInException();
		}
		
		return us.findByID(userId);
	}
	
	public List<AbstractUser> findAll(HttpSession session){
		List<AbstractUser> allUsers = null;
		
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		if(currentUser.getRole().getRoleId() < EMPLOYEE_ROLE) {
			throw new NotLoggedInException();
		}
		
		allUsers = us.findAll();
		return allUsers;
	}
	
	
}
