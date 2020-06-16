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
import exceptions.IllegalBalanceException;
import exceptions.InvalidLoginException;
import exceptions.NotLoggedInException;
import models.AbstractAccount;
import models.AbstractUser;
import templates.BalanceTemplate;
import templates.MessageTemplate;
import templates.PassTimeTemplate;
import templates.PostAccountTemplate;
import templates.TransferTemplate;

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
		MessageTemplate message = null;
		
		try {
			switch(portions[0]) {
			
			case "":
				rsp.setStatus(200); // 200 OK
				message = new MessageTemplate("This is / . 'post' to /login with your credentials to access more of the site");
				rsp.getWriter().println(om.writeValueAsString(message));
				break;				
				
			case "login":
				lc.doGet(req, rsp, message);
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
						message = new MessageTemplate("This is not a valid resource");
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
						message = new MessageTemplate("This is not a valid resource");
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
						message = new MessageTemplate("This is not a valid resource");
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
			message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
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
		MessageTemplate message = null;
		
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

					BalanceTemplate withdraw = om.readValue(req.getReader(), BalanceTemplate.class); // Fetch our account ID and amount to change
					if(ac.isOwner(session, withdraw.getAccountId()) == false) { // If the user is not an owner of the account
						as.guard(session, "Admin"); // Check if they are an admin
					}
					// Getting past means user is an owner of the account or an admin
					ac.withdraw(withdraw); // Withdraw the amount from the specified account
					rsp.setStatus(200); // OK
					message = new MessageTemplate("$" + withdraw.getAmount() + "has been withdrawn from Account #" + withdraw.getAccountId());
					rsp.getWriter().println(om.writeValueAsString(message)); // Write the updated account values.
					break;
					
				case "deposit":
					
					BalanceTemplate deposit = om.readValue(req.getReader(), BalanceTemplate.class); // Fetch our account ID and amount to change
					if(ac.isOwner(session, deposit.getAccountId()) == false) { // If the user is not an owner of the account
						as.guard(session, "Admin"); // Check if they are an admin
					}
					// Getting past means user is an owner of the account or an admin
					ac.deposit(deposit); // Deposit the amount to the specified account
					rsp.setStatus(200); // OK
					message = new MessageTemplate("$" + deposit.getAmount() + " has been deposited from Account #" + deposit.getAccountId());
					rsp.getWriter().println(om.writeValueAsString(message)); // Write the updated account values.
					break;
				
				case "transfer":
					//TODO
					TransferTemplate transfer = om.readValue(req.getReader(),TransferTemplate.class); // Fetch source and target ids and transfer amount
					if(ac.isOwner(session, transfer.getSourceAccountId()) == false) { // If the user is not an owner of the account
						as.guard(session, "Admin"); // Check if they are an admin
					} 
					// Getting past means user is an owner of the source account or an admin
					rsp.setStatus(200); // OK
					message = new MessageTemplate("$" + transfer.getAmount() + " has been transfered from Account #" + transfer.getSourceAccountId()
					+ " to Account #" + transfer.getTargetAccountId());
					rsp.getWriter().println(om.writeValueAsString(message));
					break;
				
				default:
					rsp.setStatus(404);
					message = new MessageTemplate("Resource not found");
					rsp.getWriter().println(om.writeValueAsString(message));
				}
				break;
				
			case "passTime":
				//TODO
				as.guard(session, "Admin"); //Check if user is admin
				PassTimeTemplate passTime =  om.readValue(req.getReader(),PassTimeTemplate.class); // Grab our template from the body
				ac.passTime(passTime.getNumOfMonths()); //Pass the time by the specified number of months
				
				message = new MessageTemplate(passTime.getNumOfMonths() + " months of compound interest have been accrued on all Savings accounts");
				rsp.getWriter().println(om.writeValueAsString(message));
			}
			
		}catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (InvalidLoginException e) { // If they put in bad credentials
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		} catch (IllegalBalanceException e) {
			rsp.setStatus(400);
			message = new MessageTemplate("The amount to withdraw and deposit must be greater than 0");
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
		MessageTemplate message = null;
		
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
			message = new MessageTemplate("The incoming token has expired");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		}  catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		}
	}
}
