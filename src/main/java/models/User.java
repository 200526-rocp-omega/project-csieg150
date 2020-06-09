package models;

public class User extends AbstractUser{ // Abstracted out to super-class, no extra functionality here
	public User(int userId, String username, String password, String firstName, String lastName, String email,
			Role role) {
		super();
		this.setUserId( userId);
		this.setUsername(username);
		this.setPassword(password);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setRole(role);
	}
	
	public User() {
		super();
	}
	
	

}
