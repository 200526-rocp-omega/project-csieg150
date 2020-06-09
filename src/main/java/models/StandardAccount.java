package models;

public class StandardAccount extends AbstractAccount {

	public StandardAccount(int id, double balance, AccountStatus as, AccountType at) {
		super( id,  balance,  as,  at);
	}
	
	public StandardAccount() {
		super();
	}
	// Just a non-abstract version of Abstract account, no overrides necessary since Abstract is fully implemented.
	// No added features needed for standard account.
}
