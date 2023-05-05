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

import java.lang.reflect.Field;

import org.apache.commons.codec.binary.Hex;

public class SpeedTest {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
	private static final int port = 8080;
	private static final String host = "localhost";
	private static DslJson<TimeData> serializer = new DslJson<TimeData>();
	
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		String dir = args[0];
		String inputDir = dir + "/in";
		double transactionUniqueSmall = singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-unique-10.json", 10000);
		double transactionUnique = singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-unique-100000.json", 100);
		double transactionRepeated = singleThreadedTimeComparison(getUrl("/transactions/report"), inputDir + "/transaction-repeated-100000.json", 100);
		double clanRandom = singleThreadedTimeComparison(getUrl("/onlinegame/calculate"), inputDir + "/clan-random-20k.json", 100);
		double clanSquare = singleThreadedTimeComparison(getUrl("/onlinegame/calculate"), inputDir + "/clan-square-20k.json", 100);
		double atmRandom = singleThreadedTimeComparison(getUrl("/atms/calculateOrder"), inputDir + "/atm-random-1M.json", 100);
		double atmDup = singleThreadedTimeComparison(getUrl("/atms/calculateOrder"), inputDir + "/atm-random-dup-1M.json", 100);
		
		
		TimeData times = new TimeData(transactionUniqueSmall, transactionUnique, transactionRepeated, clanRandom, clanSquare, atmRandom, atmDup);
		FileOutputStream timeFile = new FileOutputStream(dir + "/lastTimes.json");
		serializer.serialize(times, timeFile);
		timeFile.close();
		
		FileInputStream baselineFile = new FileInputStream(dir + "/baseline.json");
		TimeData baseline = serializer.deserialize(TimeData.class, baselineFile);
		baselineFile.close();
		
		Field[] fields = baseline.getClass().getDeclaredFields();
		for (Field field : fields) {
			double baseVal = (double)field.get(baseline);
			double val = (double)field.get(times);
			double diff = (baseVal - val)/baseVal;
			String color = ANSI_GREEN;
			
			if (diff > 0.1) {
				color = ANSI_RED;
			}
			
			System.out.println(color + field.getName() + ": " + new BigDecimal(diff).setScale(3, RoundingMode.HALF_UP) + ANSI_RESET);
		}
		
	}
	
	private static String getUrl(String endpoint) {
		return "http://" + host + ":" + port + endpoint; 
	}
	
	private static double singleThreadedTimeComparison(String URL, String filePath, int count) throws IOException {
		Path path = Paths.get(filePath);
		byte[] out = Files.readAllBytes(path);
		ArrayList<Double> times = new ArrayList<Double>();
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
			
			byte[] response = http.getInputStream().readAllBytes();
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
					System.out.println(filePath + " hash: " + Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(response)));
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return Collections.min(times);
	}

	public static class TimeData {
		public final double transactionUnique;
		public final double transactionUniqueSmall;
		public final double transactionRepeated;
		public final double clanRandom;
		public final double clanSquare;
		public final double atmRandom;
		public final double atmDup;
		

		@CompiledJson
		public TimeData(
				double transactionUniqueSmall, 
				double transactionUnique, 
				double transactionRepeated, 
				double clanRandom,
				double clanSquare,
				double atmRandom,
				double atmDup
				) {
			this.transactionUniqueSmall = transactionUniqueSmall;
			this.transactionUnique = transactionUnique;
			this.transactionRepeated = transactionRepeated;
			this.clanRandom = clanRandom;
			this.clanSquare = clanSquare;
			this.atmRandom = atmRandom;
			this.atmDup = atmDup;
		}
	}
	
}
