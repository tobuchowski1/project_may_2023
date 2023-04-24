package ski.obuchow.konkurs.transactions;

import com.dslplatform.json.CompiledJson;

public class AccountBalance {
	public String account;
	public int debitCount;
	public int creditCount;
	public double balance;

	@CompiledJson
	public AccountBalance(String account, int debitCount, int creditCount, double balance) {
		this.account = account;
		this.debitCount = debitCount;
		this.creditCount = creditCount;
		this.balance = balance;
	}
}
