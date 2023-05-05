package ski.obuchow.konkurs.onlinegame;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

public class GameAppTest {
	private Clans getInput1() {
		List<Clan> clans = new LinkedList<Clan>(); 
		clans.add(new Clan(4, 50));
		clans.add(new Clan(2, 70));
		clans.add(new Clan(6, 60));
		clans.add(new Clan(1, 15));
		clans.add(new Clan(5, 40));
		clans.add(new Clan(3, 45));
		clans.add(new Clan(1, 12));
		clans.add(new Clan(4, 40));
		return new Clans(6, clans);
	}
	
	private List<ResultGroup> getOutput1() {
		List<ResultGroup> result = new LinkedList<ResultGroup>();
		ResultGroup rg1 = new ResultGroup();
		rg1.clans.add(new Clan(2, 70));
		rg1.clans.add(new Clan(4, 50));
		result.add(rg1);
		ResultGroup rg2 = new ResultGroup();
		rg2.clans.add(new Clan(6, 60));
		result.add(rg2);
		ResultGroup rg3 = new ResultGroup();
		rg3.clans.add(new Clan(3, 45));
		rg3.clans.add(new Clan(1, 15));
		rg3.clans.add(new Clan(1, 12));
		result.add(rg3);
		ResultGroup rg4 = new ResultGroup();
		rg4.clans.add(new Clan(4, 40));
		result.add(rg4);
		ResultGroup rg5 = new ResultGroup();
		rg5.clans.add(new Clan(5, 40));
		result.add(rg5);
		return result;
	}
	
	@Test
    public void solutionA()
    {
		List<ResultGroup> result = GameApp.solutionA(getInput1());
		List<ResultGroup> expected = getOutput1();
		compareOutputs(expected, result);
    }

	@Test
    public void solutionB()
    {
		List<ResultGroup> result = GameApp.solutionIntervalTree(getInput1());
		List<ResultGroup> expected = getOutput1();
		compareOutputs(expected, result);
    }
	
	@Test
    public void solutionC()
    {
		List<ResultGroup> result = GameApp.solutionSquare(getInput1());
		List<ResultGroup> expected = getOutput1();
		compareOutputs(expected, result);
    }
	
	private void compareOutputs(List<ResultGroup> expected, List<ResultGroup> result) {
		assertEquals(expected.size(), result.size());
		for (int i=0; i<result.size(); i++) {
			assertEquals(expected.get(i).clans.size(), result.get(i).clans.size());
			for (int j=0; j<result.get(i).clans.size(); j++) {
				assertTrue (result.get(i).clans.get(j).compareTo(expected.get(i).clans.get(j)) == 0 );
			}
		}
	}

}
