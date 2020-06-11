package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.UserService;
import models.AbstractUser;

public class UserController {
private UserService us = new UserService(); // Lets us access User Service methods 
	
	public void accessUser(HttpServletRequest req, HttpServletResponse rsp) 
			throws ServletException, IOException{ // Fetched when the page is loaded normally

		HttpSession session = req.getSession(); // Creates a session 
		PrintWriter writer = rsp.getWriter();
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		
		if(currentUser == null) {
			rsp.setStatus(401); //Unauthorized Status code
			writer.println("You need to log in");
			return;
		}
		
		String uri = req.getRequestURI(); // This grabs the individual URI entered at the time of the GET method in the form of /users/*
		String[] uriParts = uri.split("/");
		
		if(uriParts.length != 2) {
			rsp.setStatus(400); // Bad request
			writer.println("Bad URI request, needs to be in the format of /users/{id}");
			return;
		}
		
		int userId = Integer.getInteger(uriParts[1]); // Gets the integer value of the 2nd part of the URI
		
		if((userId != currentUser.getUserId()) && (currentUser.getRole().getRoleId() < 3)) {
			rsp.setStatus(401); //Unauthorized Status code
			writer.println("You may not access other people's accounts.");
			return;
		}
		
		AbstractUser result = us.findByID(userId);
		
	}
	
	
}
