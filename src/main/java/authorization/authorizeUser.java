package authorization;

import javax.servlet.http.HttpSession;

import exceptions.AuthorizationException;
import exceptions.NotLoggedInException;
import models.AbstractUser;

public class authorizeUser {
	public static final int EMPLOYEE_ROLE = 3;
	public static final int ADMIN_ROLE = 4;
	
	
	public int authorizeUserAccess(HttpSession session, String id) {
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		int userId = -1;
		try {
			userId = Integer.parseInt(id); // Gets the integer value of the 2nd part of the URI
		} catch(NumberFormatException e) {
			userId = -99;
		}
		if((userId != currentUser.getUserId()) && (currentUser.getRole().getRoleId() < EMPLOYEE_ROLE)) {
			throw new AuthorizationException();
		}
		return userId;
	}
	
	public void authorizeFindAll(HttpSession session) {
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		if(currentUser.getRole().getRoleId() < EMPLOYEE_ROLE) {
			throw new AuthorizationException();
		}
		
		return; // If no exceptions are thrown, session is authorized
	}
	
	public void authorizeUpdate(HttpSession session, AbstractUser u) {
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser"); // Stored as 'AbstractUser' so casting should be ok.
		if((currentUser.getRole().getRoleId() < ADMIN_ROLE) || (u.getUserId() == currentUser.getUserId())) { // If not admin or the user
			throw new AuthorizationException();
		}
		return; // If no error thrown, authorized
	}
}
