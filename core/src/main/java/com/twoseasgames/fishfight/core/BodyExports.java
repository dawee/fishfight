package com.twoseasgames.fishfight.core;

import com.twoseasgames.rivet.dynamics.Body;


public class BodyExports {

	private String type = "";
	private Fish fish = null;
	private Bubble bubble = null;
	
	public BodyExports() {}
	
	public void feedBody(Body body) {
		body.setUserData((Object) this);
	}

	public static BodyExports export(Body body) {
		BodyExports bodyExports = null;
		if (body.userData() == Body.NO_DATA) {
			bodyExports = new BodyExports();
		} else {
			bodyExports = (BodyExports) body.userData();
		}
		return bodyExports;
	}

	public void setFish(Fish fish) {
		this.fish = fish;
	}
	
	public Fish fish() {
		return fish;
	}
	
	public void setBubble(Bubble bubble) {
		this.bubble = bubble;
	}

	public Bubble bubble() {
		return bubble;
	}
	
	public String type() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
