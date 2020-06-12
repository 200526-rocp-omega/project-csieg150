package models;

import java.util.Objects;

public class UserAccount {
	// This model is used to store information from the USERS-ACCOUNTS join-table and to work with the UserAccountDAO
	private int accountId;
	private int userId;
	
	public UserAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserAccount(int userId, int accountId) {
		super();
		this.accountId = accountId;
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserAccount)) {
			return false;
		}
		UserAccount other = (UserAccount) obj;
		return accountId == other.accountId && userId == other.userId;
	}

	@Override
	public String toString() {
		return "UserAccount [accountId=" + accountId + ", userId=" + userId + "]";
	}
	
	
}
