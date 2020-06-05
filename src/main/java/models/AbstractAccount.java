package models;

public abstract class AbstractAccount {
	  private int accountId; // primary key
	  private double balance;  // not null
	  private AccountStatus status;
	  private AccountType type;
}
