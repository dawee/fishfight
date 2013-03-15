package com.twoseasgames.fishfight.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twoseasgames.rivet.common.Point;
import com.twoseasgames.rivet.common.Rect;
import com.twoseasgames.rivet.dynamics.Body;
import com.twoseasgames.rivet.dynamics.World;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.util.Callback;

public class FishFight implements Game, Callback<Image>, Keyboard.Listener, Timer.Listener {

	private static List<String> imagesToLoad;
	
	static {
		imagesToLoad = new ArrayList<String>();
		imagesToLoad.add("bg");
		imagesToLoad.add("wave");
		imagesToLoad.add("fish1");
		imagesToLoad.add("fish2");
		imagesToLoad.add("bubble");
		imagesToLoad.add("blood");
	}
	
	private ImageLayer bgLayer;
	private ImageLayer seaLayer;
	private ImageLayer waveLayer;
	private int loadedCount;
	private boolean loaded;
	private Map<String, Image> images;
	private World world;
	private Camera camera;
	private Fish fish1;
	private Fish fish2;
	private List<Bubble> bubbles;
	private boolean enableFish1Bubble;
	private boolean enableFish2Bubble;
	private Map<Combo, Integer> fish1Combos;
	private Map<Combo, Integer> fish2Combos;
	private int nextFish1BubbleSize;
	private int nextFish2BubbleSize;
	
	@Override
    public void init() {
		enableFish1Bubble = true;
		enableFish2Bubble = true;
		nextFish1BubbleSize = 1;
		nextFish2BubbleSize = 1;
		fish1Combos = new HashMap<Combo, Integer>();
		fish1Combos.put(Combo.createDoubleBubbleCombo(Key.Z, Key.D, Key.S, Key.Q), 3);
		fish1Combos.put(Combo.createDoubleBubbleCombo(Key.W, Key.D, Key.S, Key.A), 3);
		fish1Combos.put(Combo.createHugeBubbleCombo(Key.Z, Key.D, Key.S, Key.Q), 6);
		fish1Combos.put(Combo.createHugeBubbleCombo(Key.W, Key.D, Key.S, Key.A), 6);
		fish2Combos = new HashMap<Combo, Integer>();
		fish2Combos.put(Combo.createDoubleBubbleCombo(Key.UP, Key.LEFT, Key.DOWN, Key.RIGHT), 3);
		fish2Combos.put(Combo.createHugeBubbleCombo(Key.UP, Key.LEFT, Key.DOWN, Key.RIGHT), 6);
		world = new World(800);
    	camera = new Camera();
    	keyboard().setListener(this);
    	BodyExports limits = new BodyExports();
    	limits.setType("limit");
    	Point groundPos = camera.getWorldPos(new Point(
    		graphics().width() / 2,
    		graphics().height()
    	));
    	groundPos.setY(groundPos.y() + 500);
    	Body ground = world.createBody(
    		groundPos,
    		Rect.createCenteredHitbox(16000, 1000),
    		false
    	);
    	Point wavePos =  camera.getWorldPos(new Point(
    		graphics().width() / 2,
    		graphics().height() * 3 / 8
    	));
    	wavePos.setY(wavePos.y() - 500);
    	Body wave = world.createBody(
    		wavePos,
    		Rect.createCenteredHitbox(16000, 1000),
    		false
    	);
    	limits.feedBody(wave);
    	limits.feedBody(ground);
    	bubbles = new ArrayList<Bubble>();
    	loadedCount = 0;
    	loaded = false;
    	images = new HashMap<String, Image>();
    	for (String name : imagesToLoad) {
    		Image image = assets().getImage("images/" + name + ".png");
    		image.addCallback(this);
    		images.put(name, image);
    	}
    }

    public void onLoadEnd() {
        bgLayer = graphics().createImageLayer(images.get("bg"));
        bgLayer.setWidth(graphics().width());
        bgLayer.setHeight(graphics().height());
        graphics().rootLayer().add(bgLayer);
        CanvasImage sea = graphics().createImage(graphics().width(), (int)(graphics().height() * 4.0 / 4));
        Canvas canvas = sea.canvas();
        canvas.setFillColor(0xFF000080);
        canvas.fillRect(0, 0, sea.width(), sea.height());
        canvas.save();
        seaLayer = graphics().createImageLayer(sea);
        graphics().rootLayer().addAt(seaLayer, 0, graphics().height() / 4);
        waveLayer = graphics().createImageLayer(images.get("wave"));
        waveLayer.setSize((int)(graphics().width() * 1.2), (int)(graphics().width() * 1.2 / 8.4));
        graphics().rootLayer().addAt(waveLayer, 0, (graphics().height() / 4) - waveLayer.height() / 2);
    	fish1 = new Fish(images.get("fish1"), images.get("blood"), new Point(-3000, 0), world, camera);
    	fish2 = new Fish(images.get("fish2"), images.get("blood"), new Point(3000, 0), world, camera);
    	loaded = true;
    }
    
    @Override
    public void paint(float alpha) {
    	if (loaded == true) {
        	fish1.paint();
        	fish2.paint();
        	for (Bubble bubble : bubbles) {
        		bubble.paint();
        	}
    	}
    }

    @Override
    public void update(float delta) {
    	Timer.updateAll(delta);
    	world.step(delta / 1000);
    	if (loaded == true) {
        	fish1.update();
        	fish2.update();
    	}
    }

    @Override
    public int updateRate() {
        return 25;
    }

	@Override
	public void onSuccess(Image result) {
		loadedCount++;
		if (loadedCount == imagesToLoad.size()) {
			onLoadEnd();
		}
		
	}

	@Override
	public void onFailure(Throwable cause) {}

	@Override
	public void onKeyDown(Event event) {
		switch(event.key()) {
			case UP:
				fish2.raise();
				break;
			case LEFT:
				if (enableFish2Bubble) {
					enableFish2Bubble = false;
					bubbles.add(
						new Bubble(
							images.get("bubble"),
							new Point(
								fish2.pos().x() - 300,
								fish2.pos().y()
							),
							-800,
							nextFish2BubbleSize,
							camera,
							world
						)
					);
					Timer.createTimer("enableFish2Bubble", 200, this);
				}
				break;
			case Z:
			case W:
				fish1.raise();
				break;
			case D:
				if (enableFish1Bubble) {
					enableFish1Bubble = false;
					bubbles.add(
						new Bubble(
							images.get("bubble"),
							new Point(
								fish1.pos().x() + 1000,
								fish1.pos().y()
							),
							800,
							nextFish1BubbleSize,
							camera,
							world
						)
					);
					Timer.createTimer("enableFish1Bubble", 200, this);
				}
				break;
			default:
				break;
		}
		nextFish1BubbleSize = 1;
		nextFish2BubbleSize = 1;
		for (Combo combo : fish1Combos.keySet()) {
			combo.onKey(event.key());
			if (combo.activated()) {
				nextFish1BubbleSize = fish1Combos.get(combo);
			}
		}
		for (Combo combo : fish2Combos.keySet()) {
			combo.onKey(event.key());
			if (combo.activated()) {
				nextFish2BubbleSize = fish2Combos.get(combo);
			}
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
	}

	@Override
	public void onKeyUp(Event event) {
		switch(event.key()) {
			case Z:
			case W:
				fish1.stopRaising();
				break;
			case UP:
				fish2.stopRaising();
				break;
			default:
				break;
	}
	}

	@Override
	public void onTimeout(String name) {
		if (name.equals("enableFish1Bubble")) {
			enableFish1Bubble = true;
		} else if (name.equals("enableFish2Bubble")) {
			enableFish2Bubble = true;
		}
		
	}
}
