package ski.obuchow.konkurs.transactions;

import java.math.BigDecimal;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

public class AccountBalance implements Comparable<AccountBalance> {
	public String account;
	public int debitCount;
	public int creditCount;
	@JsonAttribute(converter = FormatDecimal2.class)
	public BigDecimal balance;

	@CompiledJson
	public AccountBalance(String account, int debitCount, int creditCount, BigDecimal balance) {
		this.account = account;
		this.debitCount = debitCount;
		this.creditCount = creditCount;
		this.balance = balance;
	}

	@Override
	public int compareTo(AccountBalance other) {
		return this.account.compareTo(other.account);
	}
}
