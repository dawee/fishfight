package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.List;

import playn.core.Key;

public class Combo {

	private static final int KEY_TIME = 150;
	
	private List<Key> comboKeys;
	private List<Key> playerKeys;
	private int index;
	private double startTime;
	private boolean activated;
	
	public Combo(List<Key> comboKeys, List<Key> playerKeys) {
		this.comboKeys = comboKeys;
		this.playerKeys = playerKeys;
		index = 0;
		startTime = 0;
		activated = false;
	}

	public boolean activated() {
		return activated;
	}
	
	private boolean isPlayerKey(Key key) {
		boolean status = false;
		for (Key playerKey : playerKeys) {
			if (playerKey == key) {
				status = true;
				break;
			}
		}
		return status;
	}
	
	public void onKey(Key key) {
		if (index == 0) {
			startTime = currentTime();
		}
		if (key == comboKeys.get(index) && currentTime() - startTime < comboKeys.size() * KEY_TIME) {
			index++;
			if (index == comboKeys.size()) {
				index = 0;
				activated = true;
			}
			
		} else if (isPlayerKey(key)) {
			activated = false;
			index = 0;
		}
	}
	
	public static Combo createDoubleBubbleCombo(Key upKey, Key fightKey, Key downKey, Key backKey) {
		List<Key> playerKeys = new ArrayList<Key>();
		playerKeys.add(upKey);
		playerKeys.add(fightKey);
		playerKeys.add(downKey);
		playerKeys.add(backKey);
		List<Key> comboKeys = new ArrayList<Key>();
		comboKeys.add(backKey);
		comboKeys.add(downKey);
		comboKeys.add(backKey);
		comboKeys.add(backKey);
		comboKeys.add(downKey);
		return new Combo(comboKeys, playerKeys);
	}

	public static Combo createHugeBubbleCombo(Key upKey, Key fightKey, Key downKey, Key backKey) {
		List<Key> playerKeys = new ArrayList<Key>();
		playerKeys.add(upKey);
		playerKeys.add(fightKey);
		playerKeys.add(downKey);
		playerKeys.add(backKey);
		List<Key> comboKeys = new ArrayList<Key>();
		comboKeys.add(backKey);
		comboKeys.add(downKey);
		comboKeys.add(backKey);
		comboKeys.add(downKey);
		comboKeys.add(backKey);
		comboKeys.add(backKey);
		comboKeys.add(downKey);
		comboKeys.add(downKey);
		return new Combo(comboKeys, playerKeys);
	}

}
