package ski.obuchow.konkurs.transactions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


import com.dslplatform.json.DslJson;

public class TransactionsApp {
	private static DslJson<Transaction> inputParser = new DslJson<Transaction>();
	private static DslJson<AccountBalance> outputSerializer = new DslJson<AccountBalance>();
	
	public static void solve(InputStream is, OutputStream os) throws IOException {
		List<Transaction> transactions = inputParser.deserializeList(Transaction.class, is);
		List<AccountBalance> result = solution(transactions);
		outputSerializer.serialize(result, os);
	}
	
	
	public static List<AccountBalance> solution(List<Transaction> transactions) {
		HashMap<String, AccountBalance> balances = new HashMap<>();
		int size = transactions.size();
		for (int i = 0; i<size; i++) {
			Transaction t = transactions.get(i);
			
			AccountBalance creditAccount = balances.computeIfAbsent(t.creditAccount, AccountBalance::create);
			creditAccount.balance = creditAccount.balance.add(t.amount);
			creditAccount.creditCount += 1;
			
			AccountBalance debitAccount = balances.computeIfAbsent(t.debitAccount, AccountBalance::create);
			debitAccount.balance = debitAccount.balance.subtract(t.amount);
			debitAccount.debitCount += 1;
		};
		List<AccountBalance> result = new ArrayList<AccountBalance>(balances.values());
		Collections.sort(result);
		return result;
	}
}
