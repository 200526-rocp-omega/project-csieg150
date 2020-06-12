package Service;

import java.util.List;
import models.*;
import templates.LoginTemplate;
import dao.AbstractUserDAO;
import dao.IAbstractUserDAO;
import exceptions.InvalidLoginException;
import exceptions.NotLoggedInException;

public class UserService {
	private static IAbstractUserDAO uDAO = new AbstractUserDAO();
	public static final int EMPLOYEE_ROLE = 3; // Here to check if current user is Employee/admin or not. Put as variable for easy access and repeated use 

	
	public int insert(AbstractUser u) {
		int result = uDAO.insert(u); // determine if passed or not.
		if(result > 0) {
			int userId = uDAO.findByUsername(u.getUsername()).getUserId(); // Finds the auto-generated userID
			u.setUserId(userId); // Then sets it.
		}
		return result;
	}
	
	public List<AbstractUser> findAll(User currentUser){ // Pass in current user-list
		if(currentUser.getRole().getRoleId() < EMPLOYEE_ROLE) { // If a standard / premium user
			throw new NotLoggedInException(); // They shouldn't have access
		}
		return uDAO.findAll(); // No other logic needed 
	}
	
	public AbstractUser findByID(int id) {
		if(id<1) {
			throw new IllegalArgumentException(); // Id goes from 1 to above, anything else is an error.
		}
		return uDAO.findByID(id);		
	}
	
	public AbstractUser findByUsername(String uname) {
		if(uname.contains("\n") || uname.equals("")) { //If blank string or having a newline character
			throw new IllegalArgumentException(); // Not a valid username
		}
		return uDAO.findByUsername(uname);
	}
	
	public AbstractUser login(LoginTemplate lt) {
		// Used to check if credentials match a user in the Database and return 1 for successful login? Not sure how to start a session yet
		// Might just return a User object so the application can track their Role and ID
		AbstractUser u = this.findByUsername(lt.getUsername());
		if(u == null) {
			throw new InvalidLoginException();
		}
		
		if(u.getPassword().equals(lt.getPassword())) {
			return u;
		} 		
		System.out.println("No match");
		return u;
	}
	
	public boolean withdraw(AbstractAccount acc, int amount) {
		// Given the current user and the account they want to withdraw from, how much? 
		// If the amount is greater than balance or less than zero, throw an error
		return false;
	}
	
	public boolean deposit(AbstractAccount acc, int amount) {
		return false;
	}
	
	
}
