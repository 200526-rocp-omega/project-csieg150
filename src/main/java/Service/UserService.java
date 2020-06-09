package Service;

import java.util.List;

import dao.UserDAO;
import models.User;

public class UserService {
	private static UserDAO uDAO = new UserDAO();
	
	public int insert(User u) {
		int result = uDAO.insert(u); // determine if passed or not.
		if(result > 0) {
			int userId = uDAO.findByUsername(u.getUsername()).getUserId(); // Finds the auto-generated userID
			u.setUserId(userId); // Then sets it.
		}
		return result;
	}
	
	public List<User> findAll(User currentUser){ // Pass in current user-list
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		return uDAO.findAll(); // No other logic needed 
	}
	
	public User findByID(User currentUser, int id) {
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		if(id<1) {
			throw new IllegalArgumentException(); // Id goes from 1 to above, anything else is an error.
		}
		return uDAO.findByID(id);		
	}
	
	public User findByUsername(User currentUser, String uname) {
		if(currentUser.getRole().getRoleId() < 3) { // If a standard / premium user
			return null; // They shouldn't have access
		}
		if(uname.contains("\n") || uname.equals("")) { //If blank string or having a newline character
			throw new IllegalArgumentException(); // Not a valid username
		}
		return uDAO.findByUsername(uname);
	}
}
