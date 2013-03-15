package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.*;

import com.twoseasgames.fishfight.core.Settings;
import com.twoseasgames.rivet.common.Point;
import com.twoseasgames.rivet.common.Size;

public class Camera {

	private Point target;
	
	public Camera() {
		target = new Point(
			0, 0
		);
	}
	
	public Point target() {
		return target;
	}
	
	public void setTargetX(int x) {
		target.setX(x);
	}
	
	public int getScreenUnits(int worldUnits) {
		return worldUnits * graphics().width() / Settings.SCREEN_WORLD_WIDTH;
	}
	
	public Size getScreenSize(Size worldSize) {
		return new Size(
			getScreenUnits(worldSize.width()),
			getScreenUnits(worldSize.height())
		);
	}
	
	public Point getScreenPos(Point worldPoint) {
		Size worldDistance = new Size(
			worldPoint.x() - target.x(),
			worldPoint.y() - target.y()
		);
		Size screenDistance = getScreenSize(worldDistance);
		Point screenPos = new Point(
			(graphics().width() / 2) + screenDistance.width(),
			(graphics().height() / 2) + screenDistance.height()
		);
		return screenPos;
	}
	
	public int getWorldUnits(int screenUnits) {
		return screenUnits * Settings.SCREEN_WORLD_WIDTH / graphics().width();
	}

	public Size getWorldSize(Size screenSize) {
		return new Size(
			getWorldUnits(screenSize.width()),
			getWorldUnits(screenSize.height())
		);
	}

	public Point getWorldPos(Point screenPos) {
		Size screenDistance = new Size(
			screenPos.x() - (graphics().width() / 2),
			screenPos.y() - (graphics().height() / 2)
		);
		Size worldDistance = getWorldSize(screenDistance);
		Point worldPos = new Point(
			target.x() + worldDistance.width(),
			target.y() + worldDistance.height()
		);
		return worldPos;
	}
	
	
}

