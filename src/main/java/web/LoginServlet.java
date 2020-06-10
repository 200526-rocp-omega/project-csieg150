package web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.UserService;
import templates.LoginTemplate;

public class LoginServlet extends HttpServlet{
	private ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private UserService us = new UserService();
	
	@Override // the doGet
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		BufferedReader reader = req.getReader(); // The reader takes info from the 'req'
		
		
		//The below is just an alternate way to utilize om.readValue(), instead of passing 'reader' we pass 'body'
		StringBuilder sb = new StringBuilder(); // And we will use the StringBuilder to transform the reader into a response we can use
		
		String line;
		
		while( (line = reader.readLine()) != null) { // First stores the nextLine into our line variable, and then compares if it's null
			sb.append(line); //Add the line to SB
		}
		
		String body = sb.toString(); // For the case of our login we get the username:usernameValue,password:passValue as the syntax.
		
		LoginTemplate lt = om.readValue(body, LoginTemplate.class);
		System.out.println("Our current user/pass: " + lt.getUsername() +" " + lt.getPassword());
		
		//LoginTemplate lt = om.readValue(reader,LoginTemplate.class);
		
		if(us.login(lt) != null) {
			// Successful login attempt
			rsp.setStatus(200);
		} else {
			// Unsuccessful login attempt
			rsp.setStatus(418);
		}
	}
}
