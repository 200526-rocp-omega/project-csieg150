package dao;

import java.util.List;

import models.AbstractAccount;

public interface IAccountDAO {
	public int insert(AbstractAccount a); // Create operation
	public List<AbstractAccount> findAll(); // Read operation
	public List<AbstractAccount> findByStatus(String status);
	public AbstractAccount findByID(int id); // Read operation
	public int update(AbstractAccount u); // Update operation
	public int updateBalance(int id, double balance);
	public int delete(int id); // Delete operation
}
