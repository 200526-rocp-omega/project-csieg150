package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;	
import util.ConnectionUtil;

public class AccountDAO implements IAccountDAO{

	@Override
	public int insert(AbstractAccount a) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the Account object for neat SQL implementation
			double balance = a.getBalance();
			AccountStatus as = a.getStatus();
			AccountType at = a.getType();
			
			// The below updates all fields
			String sql = "INSERT INTO ACCOUNTS (balance,status_id,type_id) VALUES (?, ?, ?)";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setDouble(1, balance);
			stmnt.setInt(2, as.getStatusId());
			stmnt.setInt(3, at.getTypeId());
			
			result = stmnt.executeUpdate();
			
			sql = "SELECT ISEQ$$_20534.CURRVAL FROM DUAL"; //This gets the auto-generated ID we just used for our new account
			stmnt = conn.prepareStatement(sql);
			
			ResultSet rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt(1); // Grabs the value from the first column
				a.setAccountId(id);  // Now that we know the Value, we can access our new ID and assign it to our account.
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public List<AbstractAccount> findAll() {
		List<AbstractAccount> allAccounts = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			
			String sql = "SELECT *"
					+ "FROM ACCOUNTS"
					+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id"
					+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id"; // gets all Users with the value of their role id displayed
			
			Statement stmnt = conn.createStatement();
			
			ResultSet rs = stmnt.executeQuery(sql); // Right as this is executed, the query runs to the database and grabs the info
			
			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ACCOUNTS.ID"); // Grab the account id
				double balance = rs.getDouble("ACCOUNTS.BALANCE");
				int asID = rs.getInt("ACCOUNT_STATUS.ID");
				String asStatus = rs.getString("ACCOUNT_STATUS.status");
				int atID = rs.getInt("ACCOUNT_TYPE.ID");
				String atType = rs.getString("ACCOUNT_TYPE.type");
				
				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				AbstractAccount a = new StandardAccount(id,balance,as,at);
				
				allAccounts.add(a); // add User object to the list
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<AbstractAccount>(); // If something goes wrong, return an empty list.
		}
		return allAccounts;
	}

	@Override
	public AbstractAccount findByID(int id) {
		AbstractAccount result = null;
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM ACCOUNTS "
					+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id"
					+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, id); // Defines the WHERE ID = ?
			
			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query
			
			while(rs.next()) { // While there are results:
				int accountId = rs.getInt("ACCOUNTS.id");
				double balance = rs.getDouble("ACCOUNTS.balance");
				int statusId = rs.getInt("ACCOUNT_STATUS.id");
				String statusName = rs.getString("ACCOUNT_STATUS.status");
				int typeId = rs.getInt("ACCOUNT_TYPE.id");
				String typeName = rs.getString("ACCOUNT_TYPE.type");
				
				AccountStatus as = new AccountStatus(statusId,statusName);
				AccountType at = new AccountType(typeId,typeName);
				result = new StandardAccount(accountId,balance,as,at);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int update(AbstractAccount a) {
		// Update the various fields of an account record matching the given ID
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the Account object for neat SQL implementation
			int accountId = a.getAccountId();
			double balance = a.getBalance();
			int statusId = a.getStatus().getStatusId();
			int typeId = a.getType().getTypeId();
			
			// The below updates all fields
			String sql = "UPDATE ACCOUNTS SET "
					+ "BALANCE = ?, STATUS_ID = ?, TYPE_ID = ? WHERE ID = ?"; 
			
			PreparedStatement stmnt = conn.prepareStatement(sql); //Insert values into statement
			stmnt.setDouble(1, balance);
			stmnt.setInt(2, statusId);
			stmnt.setInt(3, typeId);
			stmnt.setInt(4, accountId);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int updateBalance(int id, double balance) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the Account object for neat SQL implementation
			
			// The below updates all fields
			String sql = "UPDATE ACCOUNTS SET BALANCE = ? WHERE ID = ?"; 
			
			PreparedStatement stmnt = conn.prepareStatement(sql); //Insert values into statement
			stmnt.setDouble(1, balance);
			stmnt.setInt(2, id);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}
	
	@Override
	public int delete(int accountId) {
		// Delete the Account row that matches the given id
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			// The below updates all fields
			String sql = "DELETE FROM ACCOUNTS WHERE ID = ?"; 
			
			PreparedStatement stmnt = conn.prepareStatement(sql); //Insert values into statement
			stmnt.setInt(1, accountId);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	
	@Override
	public List<AbstractAccount> findByStatus(int statusId) {
		List<AbstractAccount> allAccounts = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.

			String sql = "SELECT *"
					+ "FROM ACCOUNTS"
					+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id"
					+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id"
					+ "WHERE ACCOUNT_STATUS.id = ?"; // gets all accounts that match the specific account status ID

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, statusId);
			
			ResultSet rs = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ACCOUNTS.ID"); // Grab the account id
				double balance = rs.getDouble("ACCOUNTS.BALANCE");
				int asID = rs.getInt("ACCOUNT_STATUS.ID");
				String asStatus = rs.getString("ACCOUNT_STATUS.status");
				int atID = rs.getInt("ACCOUNT_TYPE.ID");
				String atType = rs.getString("ACCOUNT_TYPE.type");

				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				AbstractAccount a = new StandardAccount(id,balance,as,at);

				allAccounts.add(a); // add User object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<AbstractAccount>(); // If something goes wrong, return an empty list.
		}
		return allAccounts;
	}

	public List<AbstractAccount> findByType(int typeId){
		
		List<AbstractAccount> allAccounts = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.

			String sql = "SELECT *"
					+ "FROM ACCOUNTS"
					+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id"
					+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id"
					+ "WHERE ACCOUNT_TYPE.id = ?"; // gets all accounts that match the specific account type ID

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, typeId);
			
			ResultSet rs = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ACCOUNTS.ID"); // Grab the account id
				double balance = rs.getDouble("ACCOUNTS.BALANCE");
				int asID = rs.getInt("ACCOUNT_STATUS.ID");
				String asStatus = rs.getString("ACCOUNT_STATUS.status");
				int atID = rs.getInt("ACCOUNT_TYPE.ID");
				String atType = rs.getString("ACCOUNT_TYPE.type");

				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				AbstractAccount a = new StandardAccount(id,balance,as,at);

				allAccounts.add(a); // add User object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<AbstractAccount>(); // If something goes wrong, return an empty list.
		}
		
		return allAccounts;
	}
	

}
