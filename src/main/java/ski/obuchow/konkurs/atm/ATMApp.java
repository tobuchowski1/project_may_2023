package ski.obuchow.konkurs.atm;

import com.dslplatform.json.DslJson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;


import org.rapidoid.buffer.Buf;

public class ATMApp {
	private static DslJson<List<ATM>> inoputParser = new DslJson<List<ATM>>();
	private static DslJson<List<ATMBasic>> outputParser = new DslJson<List<ATMBasic>>();

	public static byte[] solve(Buf buf) throws IOException {
		List<ATM> atms = parseATMs(buf);
		Collections.sort(atms);
		List<ATMBasic> result = new LinkedList<ATMBasic>();
		
		HashMap<Integer, Set<Integer>> visitedATMs = new HashMap<Integer, Set<Integer>>();
		atms.forEach((atm) -> {
			ATMBasic resultATM = new ATMBasic(atm.region, atm.atmId);
			Set<Integer> visitedInRegion = visitedATMs.getOrDefault(atm.region, new HashSet<Integer>());
			if (visitedInRegion.add(atm.atmId)) {
				result.add(resultATM);
			}
			visitedATMs.putIfAbsent(atm.region, visitedInRegion);
		});
		
		 
		// do estimation how big should the buffer be
		ByteArrayOutputStream os = new ByteArrayOutputStream(10000);
		outputParser.serialize(result, os);
		return os.toByteArray();
	}
	
	private static List<ATM> parseATMs(Buf buf) throws IOException {
		int size = buf.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(buf.size());
		buf.writeTo(byteBuffer);
		return inoputParser.deserializeList(ATM.class, byteBuffer.array(), size);
	}
}
