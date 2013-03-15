package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.List;


import com.twoseasgames.rivet.common.Point;
import com.twoseasgames.rivet.common.Rect;
import com.twoseasgames.rivet.dynamics.Body;
import com.twoseasgames.rivet.dynamics.Body.Listener;
import com.twoseasgames.rivet.dynamics.World;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;

public class Fish implements Listener, com.twoseasgames.fishfight.core.Timer.Listener {

	private ImageLayer layer;
	private Body body;
	private Camera camera;
	private boolean raising;
	private int life;
	private ImageLayer lifeBar;
	private Canvas lifeCanvas;
	private CanvasImage lifeImage;
	private World world;
	private List<Blood> bloods;
	private Image bloodImage;
	
	public Fish(Image image, Image bloodImage, Point pos, World world, Camera camera) {
		this.camera = camera;
		this.world = world;
		this.bloodImage = bloodImage;
		bloods = new ArrayList<Blood>();
		life = 10;
		raising = false;
		body = world.createBody(pos, Rect.createCenteredHitbox(800, 444), true);
		layer = graphics().createImageLayer(image);
		layer.setSize(camera.getScreenUnits(1000), camera.getScreenUnits(444));
		Point screenPos = camera.getScreenPos(pos);
		graphics().rootLayer().addAt(layer, screenPos.x(), screenPos.y());
		lifeImage = graphics().createImage(layer.width(), layer.width() / 12);
		lifeCanvas = lifeImage.canvas();
		lifeBar = graphics().createImageLayer(lifeImage);
		graphics().rootLayer().addAt(lifeBar, layer.tx(), layer.ty() - 2 * lifeBar.height());
		body.setListener(this);
		BodyExports exports = new BodyExports();
		exports.setType("fish");
		exports.setFish(this);
		exports.feedBody(body);
	}
	
	public void hit() {
		life--;
		Timer.createTimer("addBlood", 1, this);
		if (life == 0) {
			layer.setVisible(false);
			lifeBar.setVisible(false);
			body.disable();
		}
	}
	
	public Point pos() {
		return body.pos();
	}
	
	public void paint() {
		List<Blood> toDelete = new ArrayList<Blood>();
		for (Blood blood : bloods) {
			blood.paint();
			if (blood.isEnable() == false) {
				toDelete.add(blood);
			}
		}
		for (Blood blood: toDelete) {
			bloods.remove(blood);
		}
		if (body.isEnable()) {
			lifeCanvas.clear();
			lifeCanvas.setFillColor(0XFFFF0000);
			lifeCanvas.fillRect(0, 0, life * lifeBar.width() / 10, lifeBar.height());
			lifeCanvas.save();
			Point screenPos = camera.getScreenPos(body.pos());
			layer.setTranslation(
				screenPos.x() - layer.width() / 2,
				screenPos.y() - layer.height() / 2
			);
			lifeBar.setTranslation(layer.tx(), layer.ty() - 2 * lifeBar.height());
		}
	}
	
	public void raise() {
		raising = true;
	}
	
	public void stopRaising() {
		raising = false;
		body.setYVelocity(0);
	}
	
	public void update() {
		if (raising == true && body.isEnable()) {
			body.setYVelocity(-1000);
		}
	}

	@Override
	public boolean onCollideTop(Body other) {
		body.setYVelocity(0);
		return false;
	}

	@Override
	public boolean onCollideRight(Body other) {
		return false;
	}

	@Override
	public boolean onCollideBottom(Body other) {
		BodyExports exports = BodyExports.export(other);
		if (exports.type().equals("limit")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onCollideLeft(Body other) {
		return true;
	}

	@Override
	public void onTimeout(String name) {
		bloods.add(new Blood(bloodImage, body.pos(), world, camera));
	}
}
