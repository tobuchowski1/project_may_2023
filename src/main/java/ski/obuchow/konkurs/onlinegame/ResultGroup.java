package ski.obuchow.konkurs.onlinegame;

import java.util.List;
import java.util.LinkedList;

import com.dslplatform.json.CompiledJson;


public class ResultGroup {
	public List<Clan> clans;

	@CompiledJson
	public ResultGroup()
	{
		this.clans = new LinkedList<Clan>();
	}
}
