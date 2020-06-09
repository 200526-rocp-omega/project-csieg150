package dao;

import java.util.List;

import models.AbstractUser;

public interface IAbstractUserDAO {
	public int insert(AbstractUser u); // Create operation
	public List<AbstractUser> findAll(); // Read operation
	public AbstractUser findByID(int id); // Read operation
	public AbstractUser findByUsername(String AbstractUsername);
	public AbstractUser findByEmail(String email);
	public int update(AbstractUser u); // Update operation
	public int delete(int id); // Delete operation
}
