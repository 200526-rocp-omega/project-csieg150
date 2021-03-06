package templates;

import java.util.Objects;

public class LoginTemplate {
	/*This is here in order to work with our LoginServlet
	 * It will catch the POSTed username and password to eventually be passed on to our Service Layer.
	 *In order to utilize ObjectMapper we need to have a class that exactly matches the JSON input we expect!*/
	
	private String username;
	private String password;
	
	public LoginTemplate() {
		super();
	}
	
	public LoginTemplate(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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
	@Override
	public int hashCode() {
		return Objects.hash(password, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LoginTemplate)) {
			return false;
		}
		LoginTemplate other = (LoginTemplate) obj;
		return Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}
	@Override
	public String toString() {
		return "LoginTemplate [username=" + username + ", password=" + password + "]";
	}
	
	
}
