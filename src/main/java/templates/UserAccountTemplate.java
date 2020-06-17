package templates;

import java.util.Objects;

public class UserAccountTemplate {
	private int userId; // The user to upgrade
	private int accountId; // The account to take the upgrade fee from

	public UserAccountTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAccountTemplate(int userId, int accountId) {
		super();
		this.userId = userId;
		this.accountId = accountId;
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

	@Override
	public int hashCode() {
		return Objects.hash(accountId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserAccountTemplate)) {
			return false;
		}
		UserAccountTemplate other = (UserAccountTemplate) obj;
		return accountId == other.accountId && userId == other.userId;
	}

	@Override
	public String toString() {
		return "UserAccountTemplate [userId=" + userId + ", accountId=" + accountId + "]";
	}

	
}
