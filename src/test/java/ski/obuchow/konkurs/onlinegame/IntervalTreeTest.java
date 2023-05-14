package ski.obuchow.konkurs.onlinegame;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class IntervalTreeTest {
    
    @Test
    public void addAndPop()
    {
        IntervalTree tree = new IntervalTree(2);
        tree.addClan(new Clan(2, 10));
        assertTrue(tree.size() == 1);
        tree.addClan(new Clan(2, 8));
        tree.addClan(new Clan(2, 9));
        assertTrue(tree.size() == 3);
        assertTrue(tree.popClan(1) == null);
        assertTrue(tree.popClan(0) == null);
        assertTrue(tree.popClan(2).points == 10);
        assertTrue(tree.popClan(2).points == 9);
        assertTrue(tree.size() == 1);
        assertTrue(tree.popClan(2).points == 8);
        assertTrue(tree.popClan(2) == null);
        assertTrue(tree.size() == 0);
    }
}
