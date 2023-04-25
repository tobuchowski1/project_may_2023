package ski.obuchow.konkurs.onlinegame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.rapidoid.buffer.Buf;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonConverter;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;


public class GameApp {
	private static DslJson<Clans> inputParser = new DslJson<Clans>();
	private static DslJson<List<List<Clan>>> outputSerializer = new DslJson<List<List<Clan>>>();

	
	public static byte[] solve(Buf buf) throws IOException {
		Clans inputClans = parseClans(buf);
		List<ResultGroup> result = new LinkedList<ResultGroup>();
		ArrayList<PriorityQueue<Clan>> clansBySize = new ArrayList<PriorityQueue<Clan>>();
	
		for (int i =1; i <= inputClans.groupCount + 1; i++)
			clansBySize.add(new PriorityQueue<Clan>(Comparator.comparingInt(Clan::getPoints).reversed()));
			
		inputClans.clans.forEach((clan) -> clansBySize.get(clan.numberOfPlayers).add(clan));
		
		ResultGroup currentGroup = new ResultGroup();
		int playersInGroup = 0;
		while (true) {
			Clan candidate = null;
			int currentMax = 0;
			
			// choosing next clan according to priority rules that can fit in the currently constructed group 
			for (int i =1; i <= (inputClans.groupCount - playersInGroup); i++) {
				Clan elem = clansBySize.get(i).peek();
				if (elem != null && elem.points > currentMax) {
					candidate = elem;
					currentMax = elem.points;
				}
			}
			
			if (candidate == null) {
				// there is no possible clan to add to current group
				// adding group to the result list and starting a new one
				
				if (currentGroup.clans.isEmpty())
					// if previous group was empty that means there are no clans left
					break;
					
				result.add(currentGroup);
				currentGroup = new ResultGroup();
				playersInGroup = 0;
			} else {
				// adding clan to the group
				currentGroup.clans.add(candidate);
				clansBySize.get(candidate.numberOfPlayers).remove();
				playersInGroup += candidate.numberOfPlayers;
			}
		}
		
		// do estimation how big should the buffer be
		ByteArrayOutputStream os = new ByteArrayOutputStream(10000);
		outputSerializer.serialize(result, os);
		return os.toByteArray();
	}
	
	
	private static Clans parseClans(Buf buf) throws IOException {
		int size = buf.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(buf.size());
		buf.writeTo(byteBuffer);
		return inputParser.deserialize(Clans.class, byteBuffer.array(), size);
	}
	
	// dsl-json couldn't handle nested lists without custom converter :( 
	@JsonConverter(target = ResultGroup.class)
	public static abstract class ResultGroupConverter {
		public static ResultGroup read(JsonReader reader) throws IOException {
			throw new IOException("not implemented"); 
		}
		public static void write(JsonWriter writer, ResultGroup value) {
			if (value == null) {
				writer.writeNull();
			} else {
				writer.serializeObject(value.clans);
			}
		}
	}
}

