package controllers;


import java.util.List;


import Service.UserService;
import models.AbstractUser;

public class UserController {
private UserService us = new UserService(); // Lets us access User Service methods 
	
	public AbstractUser accessUser(int id) { // Fetched when the page is loaded normally
		
		return us.findByID(id);
	}
	
	public List<AbstractUser> findAll(){
		return us.findAll();
	}
	
	public AbstractUser updateUser(AbstractUser u) { // Authorizes and allows for update.		
		return us.update(u);
	}
	
	public AbstractUser insert(AbstractUser u) {
		return us.insert(u);
	}
	
	public void upgradeUser(int userId, int accountId) {
		
	}
}
