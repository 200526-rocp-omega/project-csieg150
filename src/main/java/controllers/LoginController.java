package controllers;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.UserService;
import exceptions.InvalidLoginException;
import models.AbstractUser;
import templates.LoginTemplate;

public class LoginController {
	UserService us = new UserService();

	public void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		rsp.setStatus(200); // Status ok
		PrintWriter writer = rsp.getWriter();
		HttpSession session = req.getSession(); // Creates a session 
		
		if(session.getAttribute("currentUser") != null) { // If a currentUser already exists
			writer.println("Logged in already"); // They're logged in
		} else {
			writer.println("You need to log in"); // They're not logged in
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse rsp, ObjectMapper om)
		throws ServletException, IOException{
		/*the doPost method is used */
		
		PrintWriter writer = rsp.getWriter();
		
		HttpSession session = req.getSession(); // Creates a session 
		if(session.getAttribute("currentUser") != null) { // Checks if logged in
			rsp.setStatus(400); // bad request
			writer.println("Logged in already");
			return; // We are already logged in so no need to to it again.
		}
			
		LoginTemplate lt = om.readValue(req.getReader(), LoginTemplate.class);

		//LoginTemplate lt = om.readValue(reader,LoginTemplate.class);
		AbstractUser u = us.login(lt); // store credentials in the thing.
		
		if(u != null) {
			// Successful login attempt
			rsp.setStatus(200);
			writer.println(om.writeValueAsString(u));
			session.setAttribute("currentUser", u); // applies the 'credentials' to the session so we can see who it is in other requests 
		} else {
			// Unsuccessful login attempt
			throw new InvalidLoginException();
		}
	}
	
	public void logout(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException{
		HttpSession session = req.getSession(); // grab our session info
		PrintWriter writer = rsp.getWriter();
		
		if(session.getAttribute("currentUser") != null) { // If our user has a session:
			session.invalidate(); // Totally destroys their session
			rsp.setStatus(200); // Successful logout. 'OK'
			writer.println("You have been logged out successfully.");
			return;
		} 
		// If they don't have a session:
		rsp.setStatus(400); // Bad request
		writer.println("You aren't logged in, can't log out!");
	}
}
