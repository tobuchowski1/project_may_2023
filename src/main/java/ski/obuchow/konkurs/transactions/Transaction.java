package ski.obuchow.konkurs.transactions;

import com.dslplatform.json.CompiledJson;

public class Transaction {
	public final String debitAccount;
	public final String creditAccount;
	public final double amount;

	@CompiledJson
	public Transaction(String debitAccount, String creditAccount, double amount) {
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
	}
}
