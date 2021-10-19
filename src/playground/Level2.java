package playground;

import controller.FallingStarController;
import gameobjects.FallingStar;
import gameobjects.GameObject;


/**
 * Another almost-dummy SpaceInvaders level.
 */
public class Level2 extends SpaceInvadersLevel {

  public Level2() {
    super();
  }


  public String getName() {
    return "Level2" ;
  }


  // kleiner Tipp!
  @Override
  protected String getStartupMessage() {
    return "Get ready for level 2!";
  }



}
