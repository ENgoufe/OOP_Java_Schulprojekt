package controller;

import java.awt.event.KeyEvent;
import gameobjects.GameObject;
import playground.SpaceInvadersLevel;

public class BreakoutController extends Simple4WayController {

  public BreakoutController() {
    super();
  }
  
  
  public String[] allowedEvents() {
    return new String[] {"left","right"} ;
  }

  public int[] getDXs() {
    return new int[] {-1, 1} ;
  }

  public int[] getDYs() {
    return new int[] {0, 0} ;
  }

  
  
  

}
