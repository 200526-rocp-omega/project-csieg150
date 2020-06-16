import dao.AccountDAO;
import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class Driver {

	public static void main(String[] args) {
		
		AccountDAO aDAO = new AccountDAO();
		
		for(AbstractAccount account : aDAO.findByType(2)) {
			System.out.println(account);
		}
		
		
	}

}
