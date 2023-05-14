package ski.obuchow.konkurs.transactions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;


public class TransactionsTest {
    private List<Transaction> getInput1() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction("32309111922661937852684864", "06105023389842834748547303", new BigDecimal("10.90")));
        transactions.add(new Transaction("31074318698137062235845814", "66105036543749403346524547", new BigDecimal("200.90")));
        transactions.add(new Transaction("66105036543749403346524547", "32309111922661937852684864", new BigDecimal("50.10")));
        return transactions;
    }
    
    private List<AccountBalance> getOutput1() {
        List<AccountBalance> result = new ArrayList<AccountBalance>();
        
        result.add(new AccountBalance("06105023389842834748547303", 0, 1, new BigDecimal("10.90")));
        result.add(new AccountBalance("31074318698137062235845814", 1, 0, new BigDecimal("-200.90")));
        result.add(new AccountBalance("32309111922661937852684864", 1, 1, new BigDecimal("39.20")));
        result.add(new AccountBalance("66105036543749403346524547", 1, 1, new BigDecimal("150.80")));
        
        return result;
    }
    
    @Test
    public void solution()
    {
        List<AccountBalance> result = TransactionsApp.solution(getInput1());
        List<AccountBalance> expected = getOutput1();
        compareOutputs(expected, result);
    }

    private void compareOutputs(List<AccountBalance> expected, List<AccountBalance> result) {
        assertEquals(expected.size(), result.size());
        for (int i=0; i<result.size(); i++) {
            assertEquals(expected.get(i).creditCount, result.get(i).creditCount);
            assertEquals(expected.get(i).debitCount, result.get(i).debitCount);
            assertEquals(expected.get(i).account.compareTo(result.get(i).account), 0);
            assertEquals(expected.get(i).balance.compareTo(result.get(i).balance), 0);
        }
    }
    
}
