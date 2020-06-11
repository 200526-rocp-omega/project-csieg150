package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FrontController extends HttpServlet {
	private ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
			
		String requestUri = req.getRequestURI(); //Determine where the 'get' is coming from
		
		switch(requestUri) {
		case "/login":
			// code code code for login.doGet
			break;
		case "/user":
			// code code code for user.doGet
			break;
		}
			
	}
}
