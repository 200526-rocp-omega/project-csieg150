package Service;

import java.util.List;
import models.*;
import dao.AbstractUserDAO;
import dao.IAbstractUserDAO;

public class UserService {
	private static IAbstractUserDAO uDAO = new AbstractUserDAO();
	
	public int insert(AbstractUser u) {
		int result = uDAO.insert(u); // determine if passed or not.
		if(result > 0) {
			int userId = uDAO.findByUsername(u.getUsername()).getUserId(); // Finds the auto-generated userID
			u.setUserId(userId); // Then sets it.
		}
		return result;
	}
	
	public List<AbstractUser> findAll(User currentUser){ // Pass in current user-list
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		return uDAO.findAll(); // No other logic needed 
	}
	
	public AbstractUser findByID(AbstractUser currentUser, int id) {
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		if(id<1) {
			throw new IllegalArgumentException(); // Id goes from 1 to above, anything else is an error.
		}
		return uDAO.findByID(id);		
	}
	
	public AbstractUser findByUsername(AbstractUser currentUser, String uname) {
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		if(uname.contains("\n") || uname.equals("")) { //If blank string or having a newline character
			throw new IllegalArgumentException(); // Not a valid username
		}
		return uDAO.findByUsername(uname);
	}
	
	public int login(AbstractUser u) {
		// Used to check if credentials match a user in the Database and return 1 for successful login? Not sure how to start a session yet
		// Might just return a User object so the application can track their Role and ID
		
		return 0;
	}
	
	public int logout(int loginStatus) {
		// If the user is logged in, allow them to log back out and set the current user to 'guest' or whatever to remove access
		return 0;
	}
	
	public boolean withdraw(AbstractUser currentUser, AbstractAccount acc, int amount) {
		// Given the current user and the account they want to withdraw from, how much? 
		// If the amount is greater than balance or less than zero, throw an error
		return false;
	}
	
	public boolean deposit(AbstractUser currentUser, AbstractAccount acc, int amount) {
		return false;
	}
	
	
}
