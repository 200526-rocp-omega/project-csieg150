import dao.*;
import models.User;

public class Driver {

	public static void main(String[] args) {
		IUserDAO dao = new UserDAO();
		
		for(User u : dao.findAll()) {
			System.out.println(u);
		}
	}

}
