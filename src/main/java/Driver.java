import dao.*;
import models.Role;
import models.User;

public class Driver {

	public static void main(String[] args) {
		IUserDAO dao = new UserDAO();
		
//		User testUser = new User(2,"sguy","pass2","Some","Guy","sguy@email.moo",new Role(1,"Standard"));
//		
//		dao.insert(testUser);
		
		for(User u : dao.findAll()) {
			System.out.println(u);
		}
		
	}

}
