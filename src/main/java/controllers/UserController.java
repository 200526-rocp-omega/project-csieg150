package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.UserService;
import exceptions.NotLoggedInException;
import models.AbstractUser;

public class UserController {
private UserService us = new UserService(); // Lets us access User Service methods 
public static final int EMPLOYEE_ROLE = 3; // Here to check if current user is Employee/admin or not. Put as variable for easy access and repeated use 
	
	public void accessUser(HttpServletRequest req, HttpServletResponse rsp, String id) 
			throws ServletException, IOException{ // Fetched when the page is loaded normally

		HttpSession session = req.getSession(); // Creates a session 
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		
		if(currentUser == null) {
			throw new NotLoggedInException();
		}
		
		int userId = Integer.getInteger(id); // Gets the integer value of the 2nd part of the URI
		
		if((userId != currentUser.getUserId()) && (currentUser.getRole().getRoleId() < EMPLOYEE_ROLE)) {
			throw new NotLoggedInException();
		}
		
		AbstractUser result = us.findByID(userId);
		
	}
	
	
}
