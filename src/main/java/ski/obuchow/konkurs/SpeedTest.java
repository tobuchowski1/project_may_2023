package ski.obuchow.konkurs;

import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

public class SpeedTest {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
	private static final int port = 8080;
	private static final String host = "localhost";
	private static DslJson<TestDataWrapper> serializer = new DslJson<TestDataWrapper>(Settings.basicSetup());
	
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		String dir = args[0];
		String inputDir = dir + "/in";
		HashMap<String, TestData> results = new HashMap<String, TestData>();
		
		results.put("transactionUniqueSmall", singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-unique-10.json", 10000));
		results.put("transactionUnique", singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-unique-100000.json", 100));
		results.put("transactionRepeated", singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-repeated-100000.json", 100));
		results.put("clanRandom", singleThreadedTimeComparison(getUrl("/onlinegame/calculate"), inputDir + "/clan-random-20k.json", 100));
		results.put("clanSquare", singleThreadedTimeComparison(getUrl("/onlinegame/calculate"), inputDir + "/clan-square-20k.json", 100));
		results.put("atmRandom", singleThreadedTimeComparison(getUrl("/atms/calculateOrder"), inputDir + "/atm-random-1M.json", 100));
		results.put("atmDup", singleThreadedTimeComparison(getUrl("/atms/calculateOrder"), inputDir + "/atm-random-dup-1M.json", 100));
		
		TestDataWrapper wrapped = new TestDataWrapper(results);
		
		FileOutputStream timeFile = new FileOutputStream(dir + "/lastTimes.json");
		serializer.serialize(wrapped, timeFile);
		timeFile.close();
		
		FileInputStream baselineFile = new FileInputStream(dir + "/baseline.json");
		TestDataWrapper baseline = serializer.deserialize(TestDataWrapper.class, baselineFile);
		baselineFile.close();
		
		for (Map.Entry<String, TestData> entry: results.entrySet()) {
			String key = entry.getKey();
			TestData val = entry.getValue();
			TestData baseVal = baseline.map.get(key); 
			double diff = (baseVal.minTime - val.minTime)/baseVal.minTime;
			double diff90th = (baseVal.time90th - val.time90th)/baseVal.time90th;
			String color = ANSI_GREEN;
			
			if (diff > 0.1 || diff90th > 0.1) {
				color = ANSI_RED;
			}
			
			String isOK = "OK";
			if (!val.hash.equals(baseVal.hash))
				isOK = "ERROR";
			
			BigDecimal diffDecimal = new BigDecimal(diff).setScale(3, RoundingMode.HALF_UP);
			BigDecimal diff90thDecimal = new BigDecimal(diff90th).setScale(3, RoundingMode.HALF_UP);
			
			System.out.println(color + key + ": " + diffDecimal + "  " + diff90thDecimal + "  " + isOK + ANSI_RESET);
		}
	}
	
	private static String getUrl(String endpoint) {
		return "http://" + host + ":" + port + endpoint; 
	}
	
	private static TestData singleThreadedTimeComparison(String URL, String filePath, int count) throws IOException {
		Path path = Paths.get(filePath);
		byte[] out = Files.readAllBytes(path);
		ArrayList<Double> times = new ArrayList<Double>();
		String hash = "";
		int failures = 0;
		for (int i=0; i<count; i++) {
			long startTime = System.nanoTime();
			
			URL url = new URL(URL);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			
			
			http.setFixedLengthStreamingMode(out.length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			
			try(OutputStream os = http.getOutputStream()) {
			    os.write(out);
			}
			
			byte[] response;
			try {
				 response = http.getInputStream().readAllBytes();
			}catch (IOException e ) {
				failures++;
//				System.out.println(http.getErrorStream().toString());
				continue;
			}
			
			//System.out.println("received: " + new String(http.getInputStream().readAllBytes()).length() + " ");
			long elapsedTime = System.nanoTime() - startTime;
			
			// convert nanoseconds to seconds
			times.add(elapsedTime/1000000000.0);
			if (i % (count/10) == 0) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			if (i == 0) {
				try {
					hash = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(response));
					System.out.println(filePath + " hash: " + hash);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		System.out.println(failures + " failures");
		times.add(99.0);
		Collections.sort(times);
		return new TestData(times.get(0), times.get(times.size()/10*9), hash);
	}
	
	public static class TestDataWrapper {
		public HashMap<String, TestData> map;
		
		@CompiledJson
		public TestDataWrapper(HashMap<String, TestData> map) {
			this.map = map;
		}
	}
	
	public static class TestData {
		public final double minTime;
		public final double time90th;
		public final String hash;
		
		@CompiledJson
		public TestData(double minTime, double time90th, String hash) {
			this.minTime = minTime;
			this.hash = hash;
			this.time90th = time90th;
		}
	}
}
