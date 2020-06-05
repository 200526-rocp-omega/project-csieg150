package dao;

import java.util.List;

import models.User;

public interface IUserDAO {
	public int insert(User u); // Create operation
	public List<User> findAll(); // Read operation
	public User findByID(int id); // Read operation
	public User findByUsername(String username);
	public int update(User u); // Update operation
	public int delete(int id); // Delete operation
}
