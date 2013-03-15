package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;

import com.twoseasgames.rivet.common.Point;
import com.twoseasgames.rivet.common.Rect;
import com.twoseasgames.rivet.dynamics.Body;
import com.twoseasgames.rivet.dynamics.Body.Listener;
import com.twoseasgames.rivet.dynamics.World;

public class Bubble implements Listener {

	private ImageLayer layer;
	private Camera camera;
	private Body body;
	private int size;
	
	public Bubble (Image image, Point pos, int velocity, int size, Camera camera, World world) {
		this.camera = camera;
		this.size = size;
		body = world.createBody(pos, Rect.createCenteredHitbox(size * 200, size * 200), false);
		layer = graphics().createImageLayer(image);
		layer.setSize(camera.getScreenUnits(size * 200), camera.getScreenUnits(size * 200));
		Point screenPos = camera.getScreenPos(pos);
		graphics().rootLayer().addAt(layer, screenPos.x(), screenPos.y());		
		body.setXVelocity(velocity);
		body.setListener(this);
		BodyExports exports = new BodyExports();
		exports.setType("bubble");
		exports.setBubble(this);
		exports.feedBody(body);
	}

	public void paint() {
		if (body.isEnable()) {
			Point screenPos = camera.getScreenPos(body.pos());
			layer.setTranslation(
				screenPos.x() - layer.width() / 2,
				screenPos.y() - layer.height() / 2
			);
		}
	}

	@Override
	public boolean onCollideTop(Body other) {
		return false;
	}

	public void remove() {
		body.disable();
		layer.setVisible(false);
	}
	
	@Override
	public boolean onCollideRight(Body other) {
		BodyExports exports = BodyExports.export(other);
		if (exports.type().equals("bubble")) {
			Bubble bubble = exports.bubble();
			if (bubble.size >= size) {
				remove();
			}
			if (bubble.size <= size) {
				bubble.remove();
			}
		} else if (exports.type().equals("fish")) {
			Fish fish = exports.fish();
			remove();
			fish.hit();
		}
		return false;
	}

	@Override
	public boolean onCollideBottom(Body other) {
		return false;
	}

	@Override
	public boolean onCollideLeft(Body other) {
		BodyExports exports = BodyExports.export(other);
		if (exports.type().equals("fish")) {
			Fish fish = exports.fish();
			remove();
			fish.hit();
		}
		return false;
	}

}
