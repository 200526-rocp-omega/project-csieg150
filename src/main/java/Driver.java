import Service.AccountService;
import dao.AccountDAO;
import dao.UserAccountDAO;
import exceptions.IllegalBalanceException;
import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class Driver {

	public static void main(String[] args) {
		
		AccountService as = new AccountService();
		
		System.out.println(as.findByType(2));
		as.passTime(2);
		
		System.out.println(as.findByType(2));
	}

}
