package ski.obuchow.konkurs.onlinegame;

import com.dslplatform.json.CompiledJson;

public class Clan {
	public final int numberOfPlayers;
	public final int points;

	public int getPoints() {
		return this.points;
	}
	
	@CompiledJson
	public Clan(int numberOfPlayers, int points) {
		this.numberOfPlayers = numberOfPlayers;
		this.points = points;
	}
}