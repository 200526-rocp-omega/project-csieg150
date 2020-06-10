package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Role;
import models.AbstractUser;
import util.ConnectionUtil;

public class AbstractUserDAO implements IAbstractUserDAO {

	@Override
	public int insert(AbstractUser u) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the user object for neat SQL implementation
			String uname = u.getUsername();
			String pass = u.getPassword();
			String fName = u.getFirstName();
			String lName = u.getLastName();
			String email = u.getEmail();
			int roleID = u.getRole().getRoleId();
			
			// The below updates all fields
			String sql = "INSERT INTO USERS (username,password,first_name,last_name,email,role_id) VALUES (?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setString(1, uname);
			stmnt.setString(2, pass);
			stmnt.setString(3, fName);
			stmnt.setString(4, lName);
			stmnt.setString(5, email);
			stmnt.setInt(6, roleID);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public List<AbstractUser> findAll() { // Find all records
		List<AbstractUser> allAbstractUsers = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id"; // gets all users with the value of their role id displayed
			
			Statement stmnt = conn.createStatement();
			
			ResultSet rs = stmnt.executeQuery(sql); // Right as this is executed, the query runs to the database and grabs the info
			
			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ID"); // Grab the AbstractAbstractUser id
				String uname = rs.getString("USERNAME"); // grab AbstractAbstractUsername
				String password = rs.getString("PASSWORD"); // grab pass
				String fName = rs.getString("FIRST_NAME"); // grab first name
				String lName = rs.getString("LAST_NAME"); // grab last
				String email = rs.getString("EMAIL"); // grab email
				int roleID = rs.getInt("ROLE_ID"); // grab role id
				String role = rs.getString("ROLE_NAME"); // grab role name
				
				Role r = new Role(roleID, role); // make role object
				AbstractUser u = new AbstractUser(id,uname,password,fName,lName,email,r); // make AbstractAbstractUser object
				allAbstractUsers.add(u); // add AbstractAbstractUser object to the list
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<AbstractUser>(); // If something goes wrong, return an empty list.
		}
		return allAbstractUsers;
	}

	@Override
	public AbstractUser findByID(int id) { // ID is primary key
		AbstractUser result = null;
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id WHERE USERS.ID = ?";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, id); // Defines the WHERE ID = ?
			
			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query
			
			while(rs.next()) { // While there are results:
				int uid = rs.getInt("ID"); // Grab the AbstractAbstractUser id
				String username = rs.getString("USERNAME"); // grab AbstractAbstractUsername
				String password = rs.getString("PASSWORD"); // grab pass
				String fName = rs.getString("FIRST_NAME"); // grab first name
				String lName = rs.getString("LAST_NAME"); // grab last
				String email = rs.getString("EMAIL"); // grab email
				int roleID = rs.getInt("ROLE_ID"); // grab role id
				String role = rs.getString("ROLE_NAME"); // grab role name

				Role r = new Role(roleID, role); // make role object
				result = new AbstractUser(uid,username,password,fName,lName,email,r); // make AbstractAbstractUser object
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}
	
	@Override
	public AbstractUser findByUsername(String uname) { // AbstractAbstractUsernames are unique so only 1 AbstractAbstractUser per AbstractAbstractUsername
		AbstractUser result = null;
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id WHERE USERNAME = ?";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setString(1, uname); // Defines the WHERE ID = ?
			
			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query
			
			while(rs.next()) { // While there are results:
				int uid = rs.getInt("ID"); // Grab the user id
				String username = rs.getString("USERNAME"); // grab username
				String password = rs.getString("PASSWORD"); // grab pass
				String fName = rs.getString("FIRST_NAME"); // grab first name
				String lName = rs.getString("LAST_NAME"); // grab last
				String email = rs.getString("EMAIL"); // grab email
				int roleID = rs.getInt("ROLE_ID"); // grab role id
				String role = rs.getString("ROLE_NAME"); // grab role name

				Role r = new Role(roleID, role); // make role object
				result = new AbstractUser(uid,username,password,fName,lName,email,r); // make AbstractAbstractUser object
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int update(AbstractUser u) { // Updates user data and returns the number of changed rows.
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the AbstractAbstractUser object for neat SQL implementation
			int id = u.getUserId();
			String uname = u.getUsername();
			String pass = u.getPassword();
			String fName = u.getFirstName();
			String lName = u.getLastName();
			String email = u.getEmail();
			int roleID = u.getRole().getRoleId();
			
			// The below updates all fields
			String sql = "UPDATE USERS SET "
					+ "USERNAME = ?, PASSWORD = ?, FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ? WHERE ID = ?"; 
			
			PreparedStatement stmnt = conn.prepareStatement(sql); //Insert values into statement
			stmnt.setString(1, uname);
			stmnt.setString(2, pass);
			stmnt.setString(3, fName);
			stmnt.setString(4, lName);
			stmnt.setString(5, email);
			stmnt.setInt(6, roleID);
			stmnt.setInt(7, id);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int delete(int id) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the AbstractAbstractUser object for neat SQL implementation
			
			// The below deletes where the ID is equal to input.
			String sql = "DELETE FROM USERS WHERE ID = ?"; 
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, id);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public AbstractUser findByEmail(String email) {
		AbstractUser result = null;
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id WHERE EMAIL = ?";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setString(1, email); // Defines the WHERE EMAIL = ?
			
			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query
			
			while(rs.next()) { // While there are results:
				int uid = rs.getInt("ID"); // Grab the AbstractAbstractUser id
				String username = rs.getString("USERNAME"); // grab AbstractAbstractUsername
				String password = rs.getString("PASSWORD"); // grab pass
				String fName = rs.getString("FIRST_NAME"); // grab first name
				String lName = rs.getString("LAST_NAME"); // grab last
				String mail = rs.getString("EMAIL"); // grab email
				int roleID = rs.getInt("ROLE_ID"); // grab role id
				String role = rs.getString("ROLE_NAME"); // grab role name

				Role r = new Role(roleID, role); // make role object
				result = new AbstractUser(uid,username,password,fName,lName,mail,r); // make AbstractAbstractUser object
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public boolean checkPassword(String user, String pass) {
		boolean result = false;
		if(this.findByUsername(user) == null) { // If the 'find by user' yields no result then no need to continue.
			System.out.println("User not found.");
			return result;
		}
		System.out.println("Ok we did see: " + user);
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setString(1, user); // Defines the WHERE USERNAME = ?

			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query

			while(rs.next()) { // While there are results:
				String password = rs.getString("PASSWORD"); // grab pass
				System.out.println("The passwords, the one there is: " + password + " / and the one we entered is: " + pass);
				if(password.equals(pass)) {
					System.out.println("It do match");
					result = true;
				}  // If the passwords match, 
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

}
