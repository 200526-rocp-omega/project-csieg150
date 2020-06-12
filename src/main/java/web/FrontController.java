package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.LoginController;
import controllers.UserController;
import exceptions.InvalidLoginException;
import exceptions.NotLoggedInException;
import templates.MessageTemplate;

@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
	private static final ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private static final UserController uc = new UserController();
	private static final LoginController lc = new LoginController();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
			
		String URI = req.getRequestURI().replace("/rocp-project", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String id = null;
		if(URI.contains("/users/\\d+")) { // If formated ass /user/digit
			id = URI.substring(6);
		}
		
		try {
			switch(URI) {
			
			case "/":
				rsp.setStatus(200); // Unable to be found
				rsp.getWriter().println("hi");
				if(req.getSession().getAttribute("currentUser") != null) break; //If the user isn't logged in, get the login page
				
			case "/login":
				lc.doGet(req, rsp);
				break;
				
			case "/logout":
				lc.logout(req, rsp);
				break;
				
			case "/user":
				uc.accessUser(req,rsp,id);
				break;
				
			default: 
				if(id != null) {
					uc.accessUser(req, rsp, id);
				}
				rsp.setStatus(404); // Unable to be found
				rsp.getWriter().println("URI does not exist");
			}
		} catch(NotLoggedInException e) {
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(message);
		}
		
		
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException{

		final String URI = req.getRequestURI(); //Determine where the 'get' is coming from
		
		try {
			switch(URI) {
			case "/login":
				lc.doPost(req, rsp, om);
				break;
			case "/user":
				// code code code for user.doGet
				break;
			}
			
		}catch (NotLoggedInException e) {
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (InvalidLoginException e) {
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
		}

	}
}
