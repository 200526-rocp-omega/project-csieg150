package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import authorization.AuthService;
import controllers.AccountController;
import controllers.LoginController;
import controllers.UserController;
import exceptions.AuthorizationException;
import exceptions.FailedStatementException;
import exceptions.InvalidLoginException;
import exceptions.NotLoggedInException;
import models.AbstractAccount;
import models.AbstractUser;
import templates.MessageTemplate;
import templates.PostAccountTemplate;

@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
	private static final ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private static final UserController uc = new UserController();
	private static final LoginController lc = new LoginController();
	private static final AccountController ac = new AccountController();
	private static final AuthService as = new AuthService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
		
		rsp.setContentType("application/json");
			
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		
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
					int userId = -99; // Dummy value to make sure the try block works.
					try {
						userId = Integer.parseInt(portions[1]);
					} catch (NumberFormatException e) {
						throw new FailedStatementException();
					}
					as.guard(session, userId, "Employee", "Admin");
					AbstractUser u = uc.accessUser(userId);
					rsp.setStatus(200);
					rsp.getWriter().println(om.writeValueAsString(u));
				} else {
					// If not accessing a specific user, allow Employee or Admin to see list of all users.
					as.guard(session, "Employee", "Admin");
					List<AbstractUser> users = uc.findAll();
					rsp.getWriter().println(om.writeValueAsString(users));
				}
				break;
			
			case "accounts":
				if(portions.length==1) { // If the URI is just 'accounts'
					as.guard(session, "Employee", "Admin"); // Checks if employee or admin
					List<AbstractAccount> accounts = ac.findAll(); // Get all accounts
					rsp.getWriter().println(om.writeValueAsString(accounts));
					break;
				}
				switch(portions[1]) {
				case "status": 
					//TODO Find all accounts with a specific 'statusId' in portions[2]
					as.guard(session, "Employee", "Admin"); // Check if they have permission first
					
					try {
						
						int statusId = Integer.parseInt(portions[2]); // Check what status ID to get
						List<AbstractAccount> accounts = ac.findByStatus(statusId); // grab the list of accounts
						rsp.getWriter().println(om.writeValueAsString(accounts)); // Write to HttpServletResponse
						break;
						
					} catch(NumberFormatException e) {
						rsp.setStatus(404);
						MessageTemplate message = new MessageTemplate("This is not a valid resource");
						rsp.getWriter().println(om.writeValueAsString(message));
					}
					break;
					
				case "owner":
					//TODO Find all accounts related to a specific 'ownerId' in portions[2]
					
					try {
						
						int userId = Integer.parseInt(portions[2]); // Check what user ID to get
						as.guard(session, userId, "Employee", "Admin"); // Check if they have permission first
						List<AbstractAccount> accounts = ac.findByOwner(userId); // grab the list of associated accounts
						rsp.getWriter().println(om.writeValueAsString(accounts)); // Write to HttpServletResponse
						break;
						
					} catch(NumberFormatException e) {
						rsp.setStatus(404);
						MessageTemplate message = new MessageTemplate("This is not a valid resource");
						rsp.getWriter().println(om.writeValueAsString(message));
					}
					break;
				default:
					try {
						int accountId = Integer.parseInt(portions[1]); // Parse our account ID
						
						if(!(ac.isOwner(session, accountId))) { // If our current user isn't a listed owner
							as.guard(session, "Employee", "Admin"); // Check if they are employee or admin
						}
						// By passing through they're either an owner or an employee/admin
						AbstractAccount account = ac.findAccountById(accountId); // Grab the account
						rsp.getWriter().println(om.writeValueAsString(account)); // Print the value.
					} catch(NumberFormatException e) {
						rsp.setStatus(404);
						MessageTemplate message = new MessageTemplate("This is not a valid resource");
						rsp.getWriter().println(om.writeValueAsString(message));
					}
				}
				break;
				
			default: 
				if(portions.length == 2) {
					//TODO We will assume they're trying to get the account with a specified ID
				}
				rsp.setStatus(404); // Unable to be found
				rsp.getWriter().println("URI does not exist");
			}
		} catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (InvalidLoginException e) { // If they put in bad credentials
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		}
		
		
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException{
		
		rsp.setContentType("application/json");
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		
		try {
			switch(portions[0]) {
			
			case "login":
				lc.doPost(req, rsp, om);
				break;
				
			case "user":
				//TODO insert into the user table and return the stuff.
				break;
			
			case "accounts":
				//TODO take in account information as well as the 'main' userId
				if(portions.length == 1) {
					
					PostAccountTemplate postedAccount = om.readValue(req.getReader(), PostAccountTemplate.class); // Get values
					int userId = postedAccount.getUserId(); // Find associated userID
					as.guard(session, userId, "Employee", "Admin"); // Check if Employee or admin, or belongs to the user
					if (uc.accessUser(userId) == null) throw new FailedStatementException(); // Extra check to make sure User exists
					AbstractAccount account = ac.insert(postedAccount); // Insert our records
					
					rsp.setStatus(201); // 201 Created
					rsp.getWriter().println(om.writeValueAsString(account)); // Return the entered value. 
				}
				switch(portions[1]) {

				
				case "withdraw":
					//TODO
					
					break;
					
				case "deposit":
					//TODO
					
					break;
				
				case "transfer":
					//TODO
					
					break;
				
				case "passTime":
					//TODO
					
					break;
				
				default:
					
				}
				break;
			}
			
		}catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (InvalidLoginException e) { // If they put in bad credentials
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		}

	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse rsp) 
			throws ServletException, IOException{
		
		rsp.setContentType("application/json"); //Formats our output in responses to be JSON
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", ""); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		
		try {
			switch(portions[0]) {
			case "users":
				AbstractUser u = om.readValue(req.getReader(), AbstractUser.class); // Pulls out the User from the request.
				
				as.guard(session, u.getUserId(), "Admin"); // Checks if either the appropriate User or an Admin
				AbstractUser user = uc.updateUser(u);
				rsp.getWriter().println(om.writeValueAsString(user)); // Returns the updated user if no exception thrown.
				break;
				
			case "accounts":
				AbstractAccount account = om.readValue(req.getReader(), AbstractAccount.class); // Pull the account info from the request
				as.guard(session, "Admin"); // Only allow Admins to perform this kind of update.
				AbstractAccount updatedAccount = ac.update(account);
				rsp.setStatus(200); // 200 OK
				rsp.getWriter().println(om.writeValueAsString(updatedAccount)); // Return the updated accounts
			}
			
		}catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		}  catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			MessageTemplate message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			MessageTemplate message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		}
	}
}
