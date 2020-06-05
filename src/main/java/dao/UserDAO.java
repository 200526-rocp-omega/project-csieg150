package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Role;
import models.User;
import util.ConnectionUtil;

public class UserDAO implements IUserDAO {

	@Override
	public int insert(User u) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the User object for neat SQL implementation
			int id = u.getUserId();
			String uname = u.getUsername();
			String pass = u.getPassword();
			String fName = u.getFirstName();
			String lName = u.getLastName();
			String email = u.getEmail();
			int roleID = u.getRole().getRoleId();
			
			// The below updates all fields
			String sql = "UPDATE USERS SET "
					+ "USERNAME = " + uname +", PASSWORD = " + pass + ", FIRST_NAME = " + fName 
					+ ", LAST_NAME = " + lName + ", EMAIL = " + email 
					+ " WHERE ID = " + id; 
			
			Statement stmnt = conn.createStatement();
			
			result = stmnt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public List<User> findAll() { // Find all records
		List<User> allUsers = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id"; // gets all Users with the value of their role id displayed
			
			Statement stmnt = conn.createStatement();
			
			ResultSet rs = stmnt.executeQuery(sql); // Right as this is executed, the query runs to the database and grabs the info
			
			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ID"); // Grab the user id
				String username = rs.getString("USERNAME"); // grab username
				String password = rs.getString("PASSWORD"); // grab pass
				String fName = rs.getString("FIRST_NAME"); // grab first name
				String lName = rs.getString("LAST_NAME"); // grab last
				String email = rs.getString("EMAIL"); // grab email
				int roleID = rs.getInt("ROLE_ID"); // grab role id
				String role = rs.getString("ROLE_NAME"); // grab role name
				
				Role r = new Role(roleID, role); // make role object
				User u = new User(id,username,password,fName,lName,email,r); // make user object
				allUsers.add(u); // add User object to the list
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<User>(); // If something goes wrong, return an empty list.
		}
		return allUsers;
	}

	@Override
	public User findByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(User u) { // Updates User data and returns the number of changed rows.
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the User object for neat SQL implementation
			int id = u.getUserId();
			String uname = u.getUsername();
			String pass = u.getPassword();
			String fName = u.getFirstName();
			String lName = u.getLastName();
			String email = u.getEmail();
			int roleID = u.getRole().getRoleId();
			
			// The below updates all fields
			String sql = "UPDATE USERS SET "
					+ "USERNAME = " + uname +", PASSWORD = " + pass + ", FIRST_NAME = " + fName 
					+ ", LAST_NAME = " + lName + ", EMAIL = " + email 
					+ " WHERE ID = " + id; 
			
			Statement stmnt = conn.createStatement();
			
			result = stmnt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
