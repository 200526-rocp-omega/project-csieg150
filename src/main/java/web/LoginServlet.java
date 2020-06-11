package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.UserService;
import models.AbstractUser;
import templates.LoginTemplate;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{
	private ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private UserService us = new UserService();
	
	@Override // the doGet is called when someone enters the webpage from the URI
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		rsp.setStatus(200);
		PrintWriter writer = rsp.getWriter();
		HttpSession session = req.getSession(); // Creates a session 
		if(session.getAttribute("currentUser") != null) {
			writer.println("Logged in already");
		} else {
			writer.println("You need to log in");
		}
	}
	
	@Override // Normally post and such is inaccessible unless something like a FORM, JavaScript, or POSTMATE is used
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		PrintWriter writer = rsp.getWriter();
		
		HttpSession session = req.getSession(); // Creates a session 
		if(session.getAttribute("currentUser") != null) {
			rsp.setStatus(200);
			writer.println("Logged in already");
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
