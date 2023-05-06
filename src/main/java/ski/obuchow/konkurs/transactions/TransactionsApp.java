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
		
		List<AccountBalance> result = solution(transactions);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream(transactionBytes*result.size());
		outputSerializer.serialize(result, os);
		return os.toByteArray();
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
	
	private static List<Transaction> parseTransactions(Buf buf) throws IOException {
		int size = buf.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(buf.size());
		buf.writeTo(byteBuffer);
		return inputParser.deserializeList(Transaction.class, byteBuffer.array(), size);
	}
	
}
