package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;	
import util.ConnectionUtil;

public class AccountDAO implements IAccountDAO{

	@Override
	public int insert(StandardAccount a) {
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the Account object for neat SQL implementation
			int accountID = a.getAccountId();
			double balance = a.getBalance();
			AccountStatus as = a.getStatus();
			AccountType at = a.getType();
			
			// The below updates all fields
			String sql = "INSERT INTO ACCOUNTS (id,balance,account_status,account_type) VALUES (?, ?, ?, ?)";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, accountID);
			stmnt.setDouble(2, balance);
			stmnt.setString(3, as.getStatus());
			stmnt.setString(4, at.getType());
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public List<StandardAccount> findAll() {
		List<StandardAccount> allAccounts = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			
			String sql = "SELECT *"
					+ "FROM ACCOUNTS"
					+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status? = ACCOUNT_STATUS.id"
					+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type? = ACCOUNT_TYPE.id"; // gets all Users with the value of their role id displayed
			
			Statement stmnt = conn.createStatement();
			
			ResultSet rs = stmnt.executeQuery(sql); // Right as this is executed, the query runs to the database and grabs the info
			
			while(rs.next()) { // For each entry in the result set
				int id = rs.getInt("ACCOUNTS.ID"); // Grab the account id
				double balance = rs.getDouble("ACCOUNTS.BALANCE");
				int asID = rs.getInt("ACCOUNT_STATUS.ID");
				String asStatus = rs.getString("ACCOUNT_STATUS.??");
				int atID = rs.getInt("ACCOUNT_TYPE.ID");
				String atType = rs.getString("ACCOUNT_TYPE.??");
				
				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				StandardAccount a = new StandardAccount(id,balance,as,at);
				
				allAccounts.add(a); // add User object to the list
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<StandardAccount>(); // If something goes wrong, return an empty list.
		}
		return allAccounts;
	}

	@Override
	public StandardAccount findByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StandardAccount findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(StandardAccount u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
