package templates;

import java.util.Objects;

import models.AbstractAccount;
import models.AccountStatus;
import models.AccountType;
import models.StandardAccount;

public class PostAccountTemplate{
	private int userId;
	private int accountId;
	private double balance;
	private int statusId;
	private int typeId;

	public PostAccountTemplate() {
		super();
	}



	public PostAccountTemplate(int userId, int accountId, double balance, int statusId, int typeId) {
		super();
		this.userId = userId;
		this.accountId = accountId;
		this.balance = balance;
		this.statusId = statusId;
		this.typeId = typeId;
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



	public int getStatusId() {
		return statusId;
	}



	public void setStatusId(int status) {
		this.statusId = status;
	}



	public int getTypeId() {
		return typeId;
	}



	public void setTypeId(int type) {
		this.typeId = type;
	}

	public AbstractAccount toAccount() {
		AccountStatus status = null;
		AccountType type = null;
		switch(this.statusId) {
		case 1:
			status = new AccountStatus(1,"Pending");
			break;
		case 2:
			status = new AccountStatus(2,"Open");
			break;
		case 3:
			status = new AccountStatus(3,"Closed");
			break;
		case 4:
			status = new AccountStatus(4,"Denied");
			break;
		default:
			status = new AccountStatus(1,"Pending");
			break;
		}
		
		switch(this.typeId) {
		case 1:
			type = new AccountType(1,"Checking");
			break;
		case 2:
			type = new AccountType(2,"Savings");
			break;
		default:
			type = new AccountType(1,"Checking");
			break;
		}
		return new StandardAccount(accountId,balance,status,type);
	}



	@Override
	public int hashCode() {
		return Objects.hash(accountId, balance, statusId, typeId, userId);
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
				&& statusId == other.statusId && typeId == other.typeId && userId == other.userId;
	}



	@Override
	public String toString() {
		return "PostAccountTemplate [userId=" + userId + ", accountId=" + accountId + ", balance=" + balance
				+ ", statusId=" + statusId + ", typeId=" + typeId + "]";
	}
	
	

	
}
