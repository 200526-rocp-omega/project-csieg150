import Service.AccountService;
import dao.AccountDAO;
import exceptions.IllegalBalanceException;
import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class Driver {

	public static void main(String[] args) {
		
		AccountService as = new AccountService();
		
		if(as.userIsOwner(2, 6)) {
			System.out.println("confirmed owner");
		} else {
			System.out.println("Not owner");
		}
		
		
		
		
	}

}
