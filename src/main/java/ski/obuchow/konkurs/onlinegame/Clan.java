package ski.obuchow.konkurs.onlinegame;

import com.dslplatform.json.CompiledJson;

public class Clan implements Comparable<Clan>{
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

	@Override
	public int compareTo(Clan other) {
		if (other.points != this.points)
			return other.points - this.points;
		return this.numberOfPlayers - other.numberOfPlayers;
	}
}