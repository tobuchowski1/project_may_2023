package ski.obuchow.konkurs.transactions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.nio.ByteBuffer;
import org.rapidoid.buffer.Buf;


import com.dslplatform.json.DslJson;

public class TransactionsApp {
	private static DslJson<Transaction> inputParser = new DslJson<Transaction>();
	private static DslJson<AccountBalance> outputSerializer = new DslJson<AccountBalance>();
	private static final int transactionBytes = 105;
	
	public static byte[] solve(Buf buf) throws IOException {
		List<Transaction> transactions = parseTransactions(buf);
		HashMap<String, AccountBalance> balances = new HashMap<>();
		transactions.forEach(
				(t) -> {
					if (!balances.containsKey(t.creditAccount)) {
						balances.put(t.creditAccount, new AccountBalance(t.creditAccount, 0, 1, t.amount));
					} else {
						AccountBalance accountBalance = balances.get(t.creditAccount);
						accountBalance.balance = accountBalance.balance.add(t.amount);
						accountBalance.creditCount += 1;
					}
					
					if (!balances.containsKey(t.debitAccount)) {
						balances.put(t.debitAccount, new AccountBalance(t.debitAccount, 1, 0, t.amount.negate()));
					} else {
						AccountBalance accountBalance = balances.get(t.debitAccount);
						accountBalance.balance = accountBalance.balance.subtract(t.amount);
						accountBalance.debitCount += 1;
					}
				}
		);
		
		
		List<AccountBalance> result = new ArrayList<AccountBalance>(balances.values());
		Collections.sort(result);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream(transactionBytes*result.size());
		outputSerializer.serialize(result, os);
		return os.toByteArray();
	}
	
	private static List<Transaction> parseTransactions(Buf buf) throws IOException {
		int size = buf.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(buf.size());
		buf.writeTo(byteBuffer);
		return inputParser.deserializeList(Transaction.class, byteBuffer.array(), size);
	}
	
}
