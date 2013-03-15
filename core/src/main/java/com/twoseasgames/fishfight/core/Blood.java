package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;

import com.twoseasgames.rivet.common.Point;
import com.twoseasgames.rivet.common.Rect;
import com.twoseasgames.rivet.dynamics.Body;
import com.twoseasgames.rivet.dynamics.Body.Listener;
import com.twoseasgames.rivet.dynamics.World;

public class Blood implements Listener {
	
	private ImageLayer layer;
	private Body body;
	private Camera camera;
	private double opacity;
	
	public Blood(Image image, Point pos, World world, Camera camera) {
		this.camera = camera;
		opacity = 1;
		body = world.createBody(pos, Rect.createCenteredHitbox(1000, 1000), false);
		layer = graphics().createImageLayer(image);
		layer.setSize(camera.getScreenUnits(1000), camera.getScreenUnits(1000));
		Point screenPos = camera.getScreenPos(pos);
		graphics().rootLayer().addAt(layer, screenPos.x(), screenPos.y());
		body.setListener(this);
		body.setYVelocity(-500);
	}

	public boolean isEnable() {
		return body.isEnable();
	}
	
	public void paint() {
		if (body.isEnable()) {
			opacity -= 0.003;
			layer.setAlpha((float)opacity);
			if (opacity <= 0) {
				body.disable();
			}
			Point screenPos = camera.getScreenPos(body.pos());
			layer.setTranslation(
				screenPos.x() - layer.width() / 2,
				screenPos.y() - layer.height() / 2
			);
		}		
	}
	
	@Override
	public boolean onCollideTop(Body other) {
		BodyExports exports = BodyExports.export(other);
		if (exports.type().equals("limit")) {
			body.setYVelocity(0);
		}
		return false;
	}

	@Override
	public boolean onCollideRight(Body other) {
		return false;
	}

	@Override
	public boolean onCollideBottom(Body other) {
		return false;
	}

	@Override
	public boolean onCollideLeft(Body other) {
		return false;
	}
	
}
