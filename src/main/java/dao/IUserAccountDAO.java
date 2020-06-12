package dao;

import java.util.List;

import models.AbstractAccount;
import models.AbstractUser;
import models.UserAccount;

public interface IUserAccountDAO {
	// Due to the many-many relationship of the two tables, the 'find' methods need to be lists
	public int insert(AbstractUser u, AbstractAccount a); // Add record to the table
	public List<UserAccount> findAccountsByUser(AbstractUser u); // Grab a list of all accounts associated with user
	public List<UserAccount> findUsersByAccount(AbstractAccount a); // Grab a list of all users associated with an account (joint)
	public List<UserAccount> findAll(); // Allow employee / admin to see a list of all accounts
	public int deleteByUser(AbstractUser u); // Delete all rows pertaining to the user
	public int deleteByAccount(AbstractAccount a); // Delete all rows pertaining to a specific account.
}