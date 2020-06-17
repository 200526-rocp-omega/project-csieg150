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
import templates.AmountTemplate;
import templates.BalanceTemplate;
import templates.MessageTemplate;
import templates.PassTimeTemplate;
import templates.PostAccountTemplate;
import templates.PostUserTemplate;
import templates.TransferTemplate;
import templates.UserAccountTemplate;

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
			
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", "").toLowerCase(); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		MessageTemplate message = new MessageTemplate("Resource not found"); // Default message
		
		rsp.setStatus(404); // Default Status code
		
		try {
			switch(portions[0]) {
			
			case "":
				rsp.setStatus(200); // 200 OK
				message = new MessageTemplate("This is / . 'post' to /users?login with your credentials to access more of the site");
				rsp.getWriter().println(om.writeValueAsString(message));
				break;			
				
			case "users":
				if(req.getQueryString() != null) {
					switch(req.getQueryString().toLowerCase()) {
					
					case "login":
						lc.doGet(req, rsp, message, om);
						break;
						
					case "logout":
						lc.logout(req, rsp, message, om);
						break;
						
					// No default case just yet - we'll have more opportunities for querystring later
					}
					return;
				}
				
				as.guard(session);
				if(portions.length > 1) {
					// If URI structured as /user/(something) - try and parse that something to see if it's a userId
					// If it is, then access that user information if the currentuser is allowed to 
					
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
					rsp.setStatus(200); // OK
					rsp.getWriter().println(om.writeValueAsString(users));
				}
				break;
			
			case "accounts":
				as.guard(session);
				
				if(portions.length==1) { // If the URI is just 'accounts'
					as.guard(session, "Employee", "Admin"); // Checks if employee or admin
					List<AbstractAccount> accounts = ac.findAll(); // Get all accounts
					rsp.getWriter().println(om.writeValueAsString(accounts));
					break;
				}
				switch(portions[1]) {
				case "status": 
					// Find all accounts with a specific 'statusId' in portions[2]
					
					as.guard(session, "Employee", "Admin"); // Check if they have permission first
					
					try {
						
						int statusId = Integer.parseInt(portions[2]); // Check what status ID to get
						List<AbstractAccount> accounts = ac.findByStatus(statusId); // grab the list of accounts
						rsp.getWriter().println(om.writeValueAsString(accounts)); // Write to HttpServletResponse
						break;
						
					} catch(NumberFormatException e) {
						rsp.setStatus(404);
						message = new MessageTemplate("Resource not found");
						rsp.getWriter().println(om.writeValueAsString(message));
					}
					break;
					
				case "owner":
					// Find all accounts related to a specific 'ownerId' in portions[2]
					int userId = -99; // Dummy value

					try {

						userId = Integer.parseInt(portions[2]); // Check what user ID to get
						as.guard(session, userId, "Employee", "Admin"); // Check if they have permission first


					} catch(NumberFormatException e) { // Catch in case there's not a valid resource
						
						rsp.setStatus(404);
						message = new MessageTemplate("Resource not found");
						rsp.getWriter().println(om.writeValueAsString(message));
						
					}

					if(req.getQueryString() != null) { // If there's a query string

						try {
							String[] query = req.getQueryString().toLowerCase().split("="); // First should be statusId, second is the id
							int statusId = Integer.parseInt(query[1]); // Try to parse it, should be status
							
							List<AbstractAccount> results = ac.findByOwnerAndStatus(userId, statusId); // Fetch the list
							rsp.setStatus(200); // OK
							rsp.getWriter().println(om.writeValueAsString(results)); // Print results
							
						} catch (NumberFormatException e) { //Invalid parse
							rsp.setStatus(404);
							message = new MessageTemplate("Resource not found");
							rsp.getWriter().println(om.writeValueAsString(message));
						}
						
					} else { // If no query string, then just grab the specified ID
						
						List<AbstractAccount> accounts = ac.findByOwner(userId); // grab the list of associated accounts
						rsp.getWriter().println(om.writeValueAsString(accounts)); // Write to HttpServletResponse
						break;
						
					}
					
					
					
				default: // If not accounts/status or accounts/owner
					
					try { // Try to parse from accounts/(accountId)
						
						int accountId = Integer.parseInt(portions[1]); // Parse our account ID
						System.out.println(accountId);
						
						if(!(ac.isOwner(session, accountId))) { // If our current user isn't a listed owner
							as.guard(session, "Employee", "Admin"); // Check if they are employee or admin
						}
						// By passing through they're either an owner or an employee/admin
						rsp.setStatus(200);
						AbstractAccount account = ac.findAccountById(accountId); // Grab the account
						rsp.getWriter().println(om.writeValueAsString(account)); // Print the value.
						
					} catch(NumberFormatException e) {
						
						rsp.getWriter().println(om.writeValueAsString(message)); // 404 resource not found
						
					}
				}
				break;
				
			default: 
				rsp.getWriter().println(om.writeValueAsString(message));
			}
		} catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			message = new MessageTemplate("You are not logged in. Go to /users?login and POST your credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		} catch (Exception e) { // If all else fails, might be SQLException or Jackson's ObjectMapper exceptions, maybe IOException or something else
			rsp.setStatus(400);
			message = new MessageTemplate("Unknown Error. Consult the stack trace for more details. Make sure any POSTed info matches what's expected, or if updating info that the info exists to begin with.");
			rsp.getWriter().println(om.writeValueAsString(message));
			e.printStackTrace();
		}
		
		
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException{
		
		rsp.setContentType("application/json");
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", "").toLowerCase(); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		
		MessageTemplate message = new MessageTemplate("Resource not found"); // Default message
		rsp.setStatus(404); // Default Status code
		
		try {
			
			if(req.getQueryString() != null) {
				if(portions[0].equals("users") && req.getQueryString().toLowerCase().equals("login")) {
					lc.doPost(req, rsp, message, om);
					return;
				}
			}			
			
			as.guard(session); // The following switch statements require a login. This blocks out any unsigned users
			
			switch(portions[0]) {
				
			case "users":
				// insert into the user table and return the stuff in the format:
				// userId = 0, username, password, firstName, lastName, email (needs _*@_*.*_ regex format), roleId (1-4) 
				as.guard(session, "Employee","Admin"); //Rather have it so employees are the ones to instantiate an account. This isn't gmail, it's a bank
				PostUserTemplate postedUser = om.readValue(req.getReader(), PostUserTemplate.class);				
				AbstractUser insertedUser = uc.insert(postedUser.toUser());
				rsp.setStatus(201); // 201 created
				rsp.getWriter().println(om.writeValueAsString(insertedUser));
				break;
			
			case "accounts":

				if(portions.length == 1) { // Just /accounts - posting there is for creating accounts or passtime, depending on query
					
					if(req.getQueryString() != null) {
						if(req.getQueryString().toLowerCase().equals("passtime")) { // if /accounts?passTime
							
							// Accrue an amount of compound interest per month 
							as.guard(session, "Admin"); //Check if user is admin
							PassTimeTemplate passTime =  om.readValue(req.getReader(),PassTimeTemplate.class); // Grab our template from the body
							ac.passTime(passTime.getNumOfMonths()); //Pass the time by the specified number of months
							
							rsp.setStatus(200);//Ok
							message = new MessageTemplate(passTime.getNumOfMonths() + " months of compound interest have been accrued on all Savings accounts");
							rsp.getWriter().println(om.writeValueAsString(message));
							return;
						}
					}
					
					
					PostAccountTemplate postedAccount = om.readValue(req.getReader(), PostAccountTemplate.class); // Get values
					int userId = postedAccount.getUserId(); // Find associated userID
					as.guard(session, userId, "Employee", "Admin"); // Check if Employee or admin, or belongs to the user
					if (uc.accessUser(userId) == null) throw new FailedStatementException(); // Extra check to make sure User exists
					AbstractAccount account = ac.insert(postedAccount); // Insert our records
					
					rsp.setStatus(201); // 201 Created
					rsp.getWriter().println(om.writeValueAsString(account)); // Return the entered value.
					return;
				}
				
				if(req.getQueryString() != null) { // If we have a query string
					
					int accountId = -99; // Dummy value to make sure our parse works.
					
					try { // Try and parse the source userId from the URI
						
						accountId = Integer.parseInt(portions[1]);
						
					} catch (NumberFormatException e) {
						
						rsp.getWriter().println(om.writeValueAsString(message));
						
					}
					
					switch(req.getQueryString().toLowerCase()) { // To make for more restful endpoints I want to involve query strings
					
					case "withdraw":
						
						AmountTemplate amount = om.readValue(req.getReader(), AmountTemplate.class); // fetch amount posted
						BalanceTemplate withdraw = new BalanceTemplate(accountId,amount.getAmount()); // Fetch our account ID and amount to change
					
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
						rsp.getWriter().println(om.writeValueAsString(message));
					}
				} else {
					rsp.getWriter().println(om.writeValueAsString(message));
				}
				
				break;
				
			default:
				rsp.getWriter().println(om.writeValueAsString(message));
			}
			
		}catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			message = new MessageTemplate("You are not logged in. Go to /users?login and POST your credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (InvalidLoginException e) { // If they put in bad credentials
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid login credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid POST request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		} catch (IllegalBalanceException e) {
			rsp.setStatus(400);
			message = new MessageTemplate("The amount must be greater than $0. Any withdraws or transfers must be no greater than the source account balance");
			rsp.getWriter().println(om.writeValueAsString(message));
		} catch (Exception e) { // If all else fails, might be SQLException or Jackson's ObjectMapper exceptions or something else
			rsp.setStatus(400);
			message = new MessageTemplate("Unknown Error. Consult the stack trace for more details. Make sure any POSTed info matches what's expected, or if updating info that the info exists to begin with.");
			rsp.getWriter().println(om.writeValueAsString(message));
			e.printStackTrace();
		}

	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse rsp) 
			throws ServletException, IOException{
		
		rsp.setContentType("application/json"); //Formats our output in responses to be JSON
		
		String URI = req.getRequestURI().replace("/rocp-project", "").replaceFirst("/", "").toLowerCase(); //Determine where the 'get' is coming from. Removes leading 	project name
		String[] portions = URI.split("/");
		HttpSession session = req.getSession();
		MessageTemplate message = new MessageTemplate("Resource not found");
		rsp.setStatus(404);
		
		try {
			as.guard(session); // Ensures our user is logged in, otherwise they can't access
			
			switch(portions[0]) {
			case "users":
				
				if(req.getQueryString() != null) {
					if(req.getQueryString().toLowerCase().equals("upgrade")) { // If they PUT to users?upgrade
						
						AbstractUser currentuser = (AbstractUser) session.getAttribute("currentuser"); //Get the user
						
						if(currentuser.getRole().getRoleId() > 1) { // If not a 'Standard' user
							
							UserAccountTemplate userToUpgrade = om.readValue(req.getReader(), UserAccountTemplate.class); // Read the PUT
							
							as.guard(session, userToUpgrade.getUserId(), "Admin"); // If the current user is upgrading their account or an admin

							uc.upgradeUser(userToUpgrade.getUserId(),userToUpgrade.getAccountId(), ac);
							rsp.setStatus(200); // OK
							message = new MessageTemplate("User #" + userToUpgrade.getUserId() + " has been made Premium. $100 deducted from Account #" + userToUpgrade.getAccountId());
							rsp.getWriter().println(om.writeValueAsString(message));
						} else {
							throw new AuthorizationException(); // Not authorized to do this
						}
						
					}
				}
				
				AbstractUser u = om.readValue(req.getReader(), AbstractUser.class); // Pulls out the User from the request.
				
				as.guard(session, u.getUserId(), "Admin"); // Checks if either the appropriate User or an Admin
				AbstractUser user = uc.updateUser(u);
				rsp.setStatus(200); // OK
				rsp.getWriter().println(om.writeValueAsString(user)); // Returns the updated user if no exception thrown.
				break;
				
			case "accounts":
				
				if(req.getQueryString() !=  null) {
					if(req.getQueryString().toLowerCase().equals("addjointuser")) { // If a Premium / Employee / Admin wants to add a user to an account
						
						as.guard(session, "Premium","Employee","Admin"); // First check they are an allowed role
						
						UserAccountTemplate putUserAccount = om.readValue(req.getReader(), UserAccountTemplate.class); // Get PUT information
						
						AbstractUser currentuser = (AbstractUser) session.getAttribute("currentuser");
						
						ac.addUserAccount(putUserAccount, currentuser.getUserId());
						
						rsp.setStatus(200); // OK
						message = new MessageTemplate("User #" + putUserAccount.getUserId() + " added as joint owner to Account #" + putUserAccount.getAccountId());
						break;
					}
				}
				
				AbstractAccount account = om.readValue(req.getReader(), AbstractAccount.class); // Pull the account info from the request
				as.guard(session, "Admin"); // Only allow Admins to perform this kind of update.
				AbstractAccount updatedAccount = ac.update(account);
				rsp.setStatus(200); // 200 OK
				rsp.getWriter().println(om.writeValueAsString(updatedAccount)); // Return the updated accounts
				break;
				
			default:
				
				rsp.getWriter().println(om.writeValueAsString(message));
			}
			
		}catch (NotLoggedInException e) { //If user isn't logged in
			rsp.setStatus(401);
			message = new MessageTemplate("You are not logged in. Go to /users?login and POST your credentials");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		}  catch (FailedStatementException e) { // If there's some kind of unexpected SQL result (like update not hitting any rows)
			rsp.setStatus(400);
			message = new MessageTemplate("Invalid request");
			rsp.getWriter().println(om.writeValueAsString(message));
			
		} catch (AuthorizationException e) { // If the current user doesn't meet our authorization conditions.
			rsp.setStatus(401);
			message = new MessageTemplate("You are not authorized");
			rsp.getWriter().println(om.writeValueAsString(message));
		} catch (Exception e) { // If all else fails, might be SQLException or Jackson's ObjectMapper exceptions or something else
			rsp.setStatus(400);
			message = new MessageTemplate("Unknown Error. Consult the stack trace for more details. Make sure any POSTed info matches what's expected, or if updating info that the info exists to begin with.");
			rsp.getWriter().println(om.writeValueAsString(message));
			e.printStackTrace();
		}
	}
}
