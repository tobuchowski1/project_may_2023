package ski.obuchow.konkurs.atm;

import com.dslplatform.json.DslJson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ArrayList;


import org.rapidoid.buffer.Buf;

public class ATMApp {
	private static DslJson<List<ATM>> inoputParser = new DslJson<List<ATM>>();
	private static DslJson<List<ATMBasic>> outputParser = new DslJson<List<ATMBasic>>();

	public static byte[] solve(Buf buf) throws IOException {
		List<ATM> atms = parseATMs(buf);
		
		List<ATMBasic> result = solutionLinear(atms);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream(30*result.size());
		outputParser.serialize(result, os);
		return os.toByteArray();
	}
	
	public static List<ATMBasic> solution(List<ATM> atms) {
		Collections.sort(atms);
		List<ATMBasic> result = new LinkedList<ATMBasic>();
		
		Set<Integer> visitedATMs = new HashSet<Integer>();
		int prev = 0;
		for (ATM atm: atms) {
			if (prev != atm.region) {
				prev = atm.region;
				visitedATMs.clear();
			}
			if (visitedATMs.add(atm.atmId)) {
				result.add(new ATMBasic(atm.region, atm.atmId));
			}
		}
		
		 
		return result;
	}
	
	public static List<ATMBasic> solutionLinear(List<ATM> atms) {
		List<ATMBasic> result = new LinkedList<ATMBasic>();
		int regionMax = 0;
		for (ATM atm: atms) {
			regionMax = Math.max(regionMax, atm.region);
		}
		List<List<ATM>> atmByRegion = new ArrayList<List<ATM>>(regionMax + 1);
		for (int i=0; i<=regionMax; i++)
			atmByRegion.add(new LinkedList<ATM>());
		
		for (ATM atm: atms) {
			atmByRegion.get(atm.region).add(atm);
		}
		
		for (List<ATM> atmReg: atmByRegion) {
			Set<Integer> includedATMs = new HashSet<Integer>();
			for (int i=0; i<4; i++) {
				for (ATM atm: atmReg) {
					if (atm.priority.value == i) {
						if (includedATMs.add(atm.atmId)) {
							result.add(new ATMBasic(atm.region, atm.atmId));
						}
					}
				}
			}
		}
		 
		return result;
	}
	
	private static List<ATM> parseATMs(Buf buf) throws IOException {
		int size = buf.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(buf.size());
		buf.writeTo(byteBuffer);
		return inoputParser.deserializeList(ATM.class, byteBuffer.array(), size);
	}
}
