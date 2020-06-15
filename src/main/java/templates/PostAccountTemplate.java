package templates;

import java.util.Objects;

import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class PostAccountTemplate{
	private int userId;
	int accountId;
	double balance;
	AccountStatus status;
	AccountType type;

	public PostAccountTemplate() {
		super();
	}



	public PostAccountTemplate(int userId, int accountId, double balance, AccountStatus status, AccountType type) {
		super();
		this.userId = userId;
		this.accountId = accountId;
		this.balance = balance;
		this.status = status;
		this.type = type;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public int getAccountId() {
		return accountId;
	}



	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}



	public double getBalance() {
		return balance;
	}



	public void setBalance(double balance) {
		this.balance = balance;
	}



	public AccountStatus getStatus() {
		return status;
	}



	public void setStatus(AccountStatus status) {
		this.status = status;
	}



	public AccountType getType() {
		return type;
	}



	public void setType(AccountType type) {
		this.type = type;
	}



	@Override
	public int hashCode() {
		return Objects.hash(accountId, balance, status, type, userId);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PostAccountTemplate)) {
			return false;
		}
		PostAccountTemplate other = (PostAccountTemplate) obj;
		return accountId == other.accountId
				&& Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& Objects.equals(status, other.status) && Objects.equals(type, other.type) && userId == other.userId;
	}



	@Override
	public String toString() {
		return "PostAccountTemplate [userId=" + userId + ", accountId=" + accountId + ", balance=" + balance
				+ ", status=" + status + ", type=" + type + "]";
	}

	public AbstractAccount toAccount() {
		return new StandardAccount(accountId,balance,status,type);
	}

	
}
