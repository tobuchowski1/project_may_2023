package ski.obuchow.konkurs.onlinegame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonConverter;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;


public class GameApp {
	private static DslJson<Clans> inputParser = new DslJson<Clans>();
	private static DslJson<List<List<Clan>>> outputSerializer = new DslJson<List<List<Clan>>>();
	
	public static void solve(InputStream is, OutputStream os) throws IOException {
		Clans inputClans = inputParser.deserialize(Clans.class, is);
		List<ResultGroup> result = solutionIntervalTree(inputClans);
		outputSerializer.serialize(result, os);
	}
	
    public static List<ResultGroup> solutionA(Clans inputClans) {
		List<ResultGroup> result = new LinkedList<ResultGroup>();
		ArrayList<PriorityQueue<Clan>> clansBySize = new ArrayList<PriorityQueue<Clan>>();
	
		for (int i = 1; i <= inputClans.groupCount + 1; i++)
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
		
		
		return result;
	}
	
    public static List<ResultGroup> solutionIntervalTree(Clans inputClans) {
    	List<ResultGroup> result = new LinkedList<ResultGroup>();
    	IntervalTree tree = new IntervalTree(inputClans.groupCount);
    	
    	for (Clan clan : inputClans.clans) {
    		tree.addClan(clan);
    	}
    	
    	ResultGroup currentGroup = new ResultGroup();
		int playersInGroup = 0;
    	
    	while (tree.size() > 0) {
    		Clan clan = tree.popClan(inputClans.groupCount - playersInGroup);
    		if (clan == null) {
    			result.add(currentGroup);
    			playersInGroup = 0;
    			currentGroup = new ResultGroup();
    		} else {
    			currentGroup.clans.add(clan);
    			playersInGroup += clan.numberOfPlayers;
    		}
    	}
    	result.add(currentGroup);
    	
    	return result;
    }
	
    public static List<ResultGroup> solutionSquare(Clans inputClans) {
    	LinkedList<Clan> clans = new LinkedList<Clan>(); 
    	clans.addAll(inputClans.clans);
    	Collections.sort(clans);
    	List<ResultGroup> result = new LinkedList<ResultGroup>();
    	
    	
    	while (!clans.isEmpty()) {
    		ResultGroup currentGroup = new ResultGroup();
    		int playersInGroup = 0;
    		Iterator<Clan> it = clans.iterator();
    		while (it.hasNext()) {
    			Clan clan = it.next();
    			if (clan.numberOfPlayers + playersInGroup <= inputClans.groupCount) {
    				playersInGroup += clan.numberOfPlayers;
    				currentGroup.clans.add(clan);
    				it.remove();
    			}
    		}
    		result.add(currentGroup);
    	}
    	
    	
    	return result;
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

