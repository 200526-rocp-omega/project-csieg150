import dao.AccountDAO;
import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class Driver {

	public static void main(String[] args) {
		// This is just testing for the sake of seeing if insert worked
		AccountDAO aDAO = new AccountDAO(); 
		AccountStatus as = new AccountStatus(1,"Standard");
		AccountType at = new AccountType(2,"Open");
		AbstractAccount account = new StandardAccount(0,500.00,as,at);
		System.out.println(account);
		
		int result = aDAO.insert(account);
		if(result == 0) {
			System.out.println("Broked son");
		} else {
			System.out.println("Yay we did it");
			System.out.println(account);
		}
	}

}
