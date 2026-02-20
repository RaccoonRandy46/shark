package me.REDSTONER85.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.REDSTONER85.screens.SharkGui;
import me.REDSTONER85.utils.settings.Settings;

public class LevelChecker {

	private SharkGui gameState;
	private boolean possible;
	private List<int[]> path = null;
	private boolean calculated = false;
	
	private int[][] board = new int[9][5];
	
	private int true_label = TRUE;
	
	private static final int CHECKED = 1;
	private static final int TRUE = 2;
	
	public LevelChecker(SharkGui gameStateIn) {
		
		this.gameState = gameStateIn;
		
	}
	
	public void updatePossibility() {
		this.calculated = true;
		this.board = new int[9][5];
		this.possible = this.checkPossible(this.gameState.getPrevSharkX(), this.gameState.getPrevSharkY());
		this.calculatePath();
	}
	
	public boolean isPossible() {
		if (this.calculated) return this.possible;
		this.updatePossibility();
		return this.possible;
	}
	
	public List<int[]> getPath() {
		if (this.calculated) return this.path;
		this.updatePossibility();
		return this.path;
	}
	
	private void calculatePath() {
		
		if (!this.isPossible()) return;
		
		HashMap<Integer, int[]> pathMap = new HashMap<>();
		int maxStep = 0;
		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 5; y++) {
				
				int step = this.board[x][y];
				if (step >= TRUE) pathMap.put(step, new int[] {x, y});
				if (step > maxStep) maxStep = step;
				
			}
		}
		
		List<int[]> path = new ArrayList<>();
		for (int i = TRUE; i <= maxStep; i++) {
			if (!pathMap.containsKey(i)) continue;
			path.add(pathMap.get(i));
		}
		
		this.path = path;
		
	}
	
	private boolean checkPossible(int x, int y) {
		
		if (x < 0 || y < 0 || x > 8 || y > 4) return false;
		
		int tile = this.gameState.getTile(x, y);
		
		if (tile == SharkGui.FLAG) return true;
		if (tile == SharkGui.WALL) return false;
		if (this.board[x][y] == CHECKED) return false;
		
		this.board[x][y] = CHECKED;
		
		boolean ret;
		
		if (tile >= 3 && tile <= 6) {
			
			switch (tile) {
				case SharkGui.ARROW_UP:
					ret = checkPossible(x, y - 1);
					if (ret) this.board[x][y] = TRUE();
					return ret;
				case SharkGui.ARROW_DOWN:
					ret = checkPossible(x, y + 1);
					if (ret) this.board[x][y] = TRUE();
					return ret;
				case SharkGui.ARROW_LEFT:
					ret = checkPossible(x - 1, y);
					if (ret) this.board[x][y] = TRUE();
					return ret;
				case SharkGui.ARROW_RIGHT:
					ret = checkPossible(x + 1, y);
					if (ret) this.board[x][y] = TRUE();
					return ret;
			}
			
		}
		
		HashMap<Integer, List<int[]>> moveMap = this.getMoveMap(x, y);
		int maxDist = moveMap.get(-1).get(0)[0];
		
		for (int i = 0; i <= maxDist; i++) {
			if (!moveMap.containsKey(i)) continue;
			List<int[]> moves = moveMap.get(i);
			for (int[] move : moves) {
				if (checkPossible(move[0], move[1])) {
					this.board[x][y] = TRUE();
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	private HashMap<Integer, List<int[]>> getMoveMap(int x, int y) {
		
		HashMap<Integer, List<int[]>> map = new HashMap<>();
		
		int[][] moves = {{x - 1, y}, {x - 2, y}, {x + 1, y}, {x + 2, y}, {x, y - 1}, {x, y - 2}, {x, y + 1}, {x, y + 2}};
		if (!Settings.getNPSetting(Settings.NP_ALLOW_DASHES)) moves = new int[][] {{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}};
		int maxDist = -1;
		
		for (int[] move : moves) {
			
			int dist = Math.abs(move[0] - this.gameState.getFlagX()) + Math.abs(move[1] - this.gameState.getFlagY());
			if (dist > maxDist) maxDist = dist;
			
			if (map.containsKey(dist)) {
				map.get(dist).add(move);
				continue;
			}
			
			List<int[]> list = new ArrayList<>();
			list.add(move);
			map.put(dist, list);
			
		}
		
		List<int[]> retMaxDist = new ArrayList<>();
		retMaxDist.add(new int[] {maxDist});
		map.put(-1, retMaxDist);
		return map;
		
	}
	
	private int TRUE() {
		return this.true_label++;
	}
	
}
