package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.LoginController;
import controllers.UserController;
import exceptions.FailedStatementException;
import exceptions.InvalidLoginException;
import exceptions.NotLoggedInException;
import models.AbstractUser;
import templates.MessageTemplate;

@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
	private static final ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private static final UserController uc = new UserController();
	private static final LoginController lc = new LoginController();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		rsp.setContentType("application/json");
			
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		
		try {
			switch(portions[0]) {
			
			case "":
				rsp.setStatus(200);
				rsp.getWriter().println("hi how's it goin");
				break;				
				
			case "login":
				lc.doGet(req, rsp);
				break;
				
			case "logout":
				lc.logout(req, rsp);
				break;
				
			case "users":
				if(portions.length > 1) {
					AbstractUser u = uc.accessUser(req.getSession(),portions[1]);
					rsp.setStatus(200);
					rsp.getWriter().println(om.writeValueAsString(u));
				} else {
					//TODO If not accessing a specific user, allow Employee or Admin to see list of all users.
					List<AbstractUser> users = uc.findAll(req.getSession());
					rsp.getWriter().println(om.writeValueAsString(users));
				}
				break;
				
			default: 
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
		rsp.setContentType("application/json");
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		
		try {
			switch(portions[0]) {
			case "login":
				lc.doPost(req, rsp, om);
				break;
			case "user":
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
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse rsp) 
			throws ServletException, IOException{
		
		rsp.setContentType("application/json");
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		
		try {
			switch(portions[0]) {
			case "user":
				// code code code for user.doGet
				AbstractUser u = om.readValue(req.getReader(), AbstractUser.class); // Pulls out the User from the request.
				AbstractUser user = uc.updateUser(req.getSession(),u);
				rsp.getWriter().println(om.writeValueAsString(user)); // Returns the updated user if no exception thrown.
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
		} catch(FailedStatementException e) {
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
		}
	}
}
