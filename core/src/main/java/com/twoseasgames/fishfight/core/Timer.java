package com.twoseasgames.fishfight.core;

import java.util.ArrayList;
import java.util.List;

public class Timer {

	private static List<Timer> timers = new ArrayList<Timer>();
	private static List<Timer> timersToCreate = new ArrayList<Timer>();
	private static int count = 0;
	
	public interface Listener {
		
		public void onTimeout(String name);
		
	}
	
	private String name;
	private Listener listener;
	private int id;
	private float deltaSum;
	private boolean active;
	private float delay;
	
	private Timer(String name, float delay, Listener listener) {
		id = count;
		deltaSum = 0;
		active = true;
		this.delay = delay;
		this.name = name;
		this.listener = listener;
	}
	
	public static void createTimer(String name, float delta, Listener listener) {
		timersToCreate.add(new Timer(name, delta, listener));
		count++;
	}

	public static void cancel(String name) {
		for(Timer timer : timers) {
			if (timer.name.equals(name)) {
				timer.active = false;
			}
		}
	}

	public static boolean has(String name) {
		boolean result = false;
		for(Timer timer : timers) {
			if (timer.name.equals(name)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private void update(float delta) {
		deltaSum += delta;
		if (deltaSum >= delay) {
			listener.onTimeout(name);
			active = false;
		}
	}
	
	static void updateAll(float delta) {
		List<Timer> toDelete = new ArrayList<Timer>();
		for(Timer timer : timers) {
			if (timer.active) {
				timer.update(delta);
			} else {
				toDelete.add(timer);
			}
		}
		for(Timer timer : toDelete) {
			timers.remove(timer);
		}
		addPendingTimers();
	}
	
	static private void addPendingTimers() {
		for(Timer timer : timersToCreate) {
			timers.add(timer);
		}
		timersToCreate.clear();
	}
	
	public boolean equals(Object object) {
		boolean result = false;
		Timer other = (Timer) object;
		if (other.id == id) {
			result = true;
		}
		return result;
	}
	
	public int hashCode() {
		return id;
	}
}
