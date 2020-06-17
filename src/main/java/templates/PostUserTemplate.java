package templates;

import java.util.Objects;

import models.AbstractUser;
import models.Role;

public class PostUserTemplate {
	private int userId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int roleId;
	
	public PostUserTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PostUserTemplate(int userId, String username, String password, String firstName, String lastName,
			String email, int roleId) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, lastName, password, roleId, userId, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PostUserTemplate)) {
			return false;
		}
		PostUserTemplate other = (PostUserTemplate) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
				&& roleId == other.roleId && userId == other.userId && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "PostUserTemplate [userId=" + userId + ", username=" + username + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", roleId=" + roleId
				+ "]";
	}
	
	public AbstractUser toUser() {
		Role role = null;
		switch(roleId) {
		case 1:
			role = new Role(1,"Standard");
			break;
		case 2:
			role = new Role(2,"Premium");
			break;
		case 3:
			role = new Role(3,"Employee");
			break;
		case 4:
			role = new Role(4,"Admin");
			break;
		default:
			role = new Role(1,"Standard");
			break;
		}
		return new AbstractUser(userId, username, password, firstName, lastName, email, role);
	}
}
