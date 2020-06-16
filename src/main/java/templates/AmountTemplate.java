package templates;

import java.util.Objects;

public class AmountTemplate { // Represents an amount posted to /accounts/:accountId?[deposit||withdraw]
	
	private double amount;

	public AmountTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AmountTemplate(double amount) {
		super();
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AmountTemplate)) {
			return false;
		}
		AmountTemplate other = (AmountTemplate) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount);
	}

	@Override
	public String toString() {
		return "AmountTemplate [amount=" + amount + "]";
	}
	
}
