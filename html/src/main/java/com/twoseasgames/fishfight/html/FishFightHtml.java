package com.twoseasgames.fishfight.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.twoseasgames.fishfight.core.FishFight;

public class FishFightHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("fishfight/");
    PlayN.run(new FishFight());
  }
}
