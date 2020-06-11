package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.UserService;
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
		
		BufferedReader reader = req.getReader(); // The reader takes info from the 'req'
		
		
		//The below is just an alternate way to utilize om.readValue(), instead of passing 'reader' we pass 'body'
		StringBuilder sb = new StringBuilder(); // And we will use the StringBuilder to transform the reader into a response we can use
		
		String line;
		
		while( (line = reader.readLine()) != null) { // First stores the nextLine into our line variable, and then compares if it's null
			sb.append(line); //Add the line to SB
		}
		
		String body = sb.toString(); // For the case of our login we get the username:usernameValue,password:passValue as the syntax.
		
		
		LoginTemplate lt = om.readValue(body, LoginTemplate.class);

		//LoginTemplate lt = om.readValue(reader,LoginTemplate.class);
		AbstractUser u = us.login(lt); // store credentials in the thing.
		
		if(u != null) {
			// Successful login attempt
			rsp.setStatus(200);
			writer.println(om.writeValueAsString(u));
			session.setAttribute("currentUser", u); // applies the 'credentials' to the session so we can see who it is in other requests 
		} else {
			// Unsuccessful login attempt
			rsp.setStatus(400);
			writer.println("Username or password incorrect");
		}
	}
}
