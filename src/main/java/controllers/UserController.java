package controllers;


import java.util.List;


import Service.UserService;
import exceptions.FailedStatementException;
import models.AbstractUser;
import models.Role;
import templates.TransferTemplate;

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
	
	public void upgradeUser(int userId, int accountId, AccountController ac) {
		AbstractUser user = us.findByID(userId);
		if(user.getRole().getRoleId() != 1) {
			throw new FailedStatementException(); // If the user account is already premium / employee / admin, they shouldn't be accessing.
		}
		TransferTemplate transfer = new TransferTemplate(accountId,5,100);
		ac.transfer(transfer); // Try to transfer from the given ID to our Admin account (the 'bank' account
		
		user.setRole(new Role(2,"Premium"));
		us.update(user);
	}
}
