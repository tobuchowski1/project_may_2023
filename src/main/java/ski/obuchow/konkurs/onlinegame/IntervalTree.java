package ski.obuchow.konkurs.onlinegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;


// this is a binary tree allowing to search for a clan group with highest amount of points with the
// number of players lower or equal to a given number. Adding and removing clan operations are done in logM time
// where M is the max size of the player group

public class IntervalTree {
	private final int base; 
	private int[] tree;
	private int size;
	private ArrayList<PriorityQueue<Clan>> pointsBySize;
	
	public int size() {
		return size;
	}
	
	public IntervalTree(int maxPos) {
		int power = 2;
		while (power <= maxPos)
			power *= 2;
		base = power;
		tree = new int[base*2];
		pointsBySize = new ArrayList<PriorityQueue<Clan>>();
		for (int i=0; i<base; i++) {
			pointsBySize.add(new PriorityQueue<Clan>());
		}
	}
	
	public void addClan(Clan clan) {
		size += 1;
		pointsBySize.get(clan.numberOfPlayers).add(clan);
		updateTree(clan.numberOfPlayers);
	}
	
	public Clan popClan(int maxCount) {
		Clan clan = popClan(maxCount, 0, base-1, 1);
		
		if (clan == null)
			return null;
		
		size -= 1;
		Clan result = pointsBySize.get(clan.numberOfPlayers).remove();
		updateTree(clan.numberOfPlayers);
		return result;
	}
	
	private void updateTree(int count) {
		int node = count + base;
		Clan clan = pointsBySize.get(count).peek();
		int points = 0;
		if (clan != null)
			points = clan.points;
		if (points != tree[node]) {
			tree[node] = points;
			node /=2;
			while (node > 0) {
				tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
				node /= 2;
			}
		}
	}
	
	private Clan popClan(int maxCount, int start, int end, int node) {
		if (node >= base) {
			return pointsBySize.get(node - base).peek();
		}
		
		if (tree[node] == 0) {
			return null;
		}
		
		int mid = (start+end) / 2;
		if (mid >= maxCount || (tree[node * 2] >= tree[node * 2 + 1])) {
			return popClan(maxCount, start, mid, node * 2);
		} else if (end <= maxCount || tree[node * 2] == 0) {
			return popClan(maxCount, mid + 1, end, node * 2 + 1);
		}
		
		Clan left = popClan(maxCount, start, mid, node * 2);
		Clan right = popClan(maxCount, mid + 1, end, node * 2 + 1);
		
		if (right == null)
			return left;
		
		if (left.points >= right.points) {
			return left;
		}
		return right;
	}
}
