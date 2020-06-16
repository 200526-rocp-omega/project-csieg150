package authorization;

import javax.servlet.http.HttpSession;

import exceptions.AuthorizationException;
import exceptions.NotLoggedInException;
import models.AbstractUser;

public class AuthService {
	public void guard(HttpSession session, String...roles) { //Check if the current user is in our specified allowed roles
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser");
		String userRole = currentUser.getRole().getRole(); // Find the role of our user.
		
		for(String role : roles) {
			if(userRole.equals(role)) {
				return; // Authorized role found
			}
		}
		
		throw new AuthorizationException(); // Not with an allowed role
	}
	
	public void guard(HttpSession session, int id, String...roles) { // Check if UserID matches currentuser's id or in allowed roles 
		
		AbstractUser currentUser = (AbstractUser) session.getAttribute("currentUser");
		
		if(id == currentUser.getUserId()) return; // If the user has the appropriate ID, they can access the information
		
		String userRole = currentUser.getRole().getRole(); // Find the role
		
		for(String role : roles) {
			if(userRole.equals(role)) {
				return; // Authorized role found
			}
		}
		
		throw new AuthorizationException(); // Not the specified user nor an allowed role
	}
	
	public void guard(HttpSession session) { // Checks if user is logged in or not
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException();
		}
	}
}
