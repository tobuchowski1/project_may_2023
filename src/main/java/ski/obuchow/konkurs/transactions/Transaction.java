package ski.obuchow.konkurs.transactions;

import java.math.BigDecimal;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

public class Transaction {
	public final String debitAccount;
	public final String creditAccount;
	
	// to maximize speed we could go with double type for the transaction amount and account balances
	// However we are dealing with money here and precision is very important
	@JsonAttribute(converter = FormatDecimal2.class)
	public final BigDecimal amount;

	@CompiledJson
	public Transaction(String debitAccount, String creditAccount, BigDecimal amount) {
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
	}
}
