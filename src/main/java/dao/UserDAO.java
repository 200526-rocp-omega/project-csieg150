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
		// TODO Auto-generated method stub
		return 0;
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
				int id = rs.getInt("USERS.id"); // Grab the user id
				String username = rs.getString("username"); // grab username
				String password = rs.getString("password"); // grab pass
				String fName = rs.getString("first_name"); // grab first name
				String lName = rs.getString("last_name"); // grab last
				String email = rs.getString("email"); // grab email
				int roleID = rs.getInt("role_id"); // grab role id
				String role = rs.getString("role"); // grab role name
				
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
	public int update(User u) {
		// TODO Auto-generated method stub
		return 0;
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
