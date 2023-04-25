package ski.obuchow.konkurs.onlinegame;

import java.util.List;

import com.dslplatform.json.CompiledJson;

public class Clans {
	public final int groupCount;
	public final List<Clan> clans; 

	@CompiledJson
	public Clans(int groupCount, List<Clan> clans) {
		this.groupCount = groupCount;
		this.clans = clans;
	}
}
