package dao;

import java.util.List;

import models.StandardAccount;

public interface IAccountDAO {
	public int insert(StandardAccount a); // Create operation
	public List<StandardAccount> findAll(); // Read operation
	public StandardAccount findByID(int id); // Read operation
	public StandardAccount findByUsername(String username);
	public int update(StandardAccount u); // Update operation
	public int delete(int id); // Delete operation
}
