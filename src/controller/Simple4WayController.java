package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import playground.*;
import controller.*;
import gameobjects.*;
import java.util.*;
import java.awt.event.*;


/** Simple controller for object movable in 4 directions.
  * Key strokes are not read directly but taken from level flags
  */
public class Simple4WayController extends ObjectController {
  double rad = 0;

  GameObject ego = null ;
  int heading = -1 ; // index into pressedEvts
  String [] pressedEvts = {"right","left","up","down" } ;
  int [] dxs = {1,-1,0,0} ;
  int [] dys = {0,0,-1,1} ;



  public Simple4WayController() {

  }

  public String[] allowedEvents() {
    return this.pressedEvts ;
  }


  public int[] getDXs() {
    return this.dxs ;
  }


  public int[] getDYs() {
    return this.dys ;
  }


  /** Computes the state thge player wants based on key presses.
 * @return: -1 (stop), 0(right), ...
 */ 
  public int  computeRequestedState() {
    String[] pe = this.allowedEvents() ;
    for (int i = 0; i < pe.length; i ++) {
      Boolean pr = (Boolean) getPlayground().getOrCreateLevelFlag(pe[i]+"_pressed", false) ;
      Boolean rel = (Boolean) getPlayground().getOrCreateLevelFlag(pe[i]+"_released", false) ;
      if ((this.heading != i) && (pr))  {
        return i ;
      }
      if ((this.heading == i) && (rel)) {
        return -1 ;
      }
        
    }
    return this.heading ;  
  }


  // meant 2 be overwritten
  double getMovementSpeed() {
    return 250.0 ;
  }

  public void executeMovement(int reqState) {
    if (reqState == -1) {
      this.setVX(0) ;
      this.setVY(0) ;
      this.heading = -1 ;
    } else {
      int[]_dxs = this.getDXs() ;
      int[] _dys = this.getDYs() ;
      double vx = _dxs[reqState] * this.getMovementSpeed();
      double vy = _dys[reqState] * this.getMovementSpeed();
      this.setVX(vx) ;
      this.setVY(vy) ;
    } 
  }


  public void updateObject() {

    int requestedState = this.computeRequestedState() ;
    //System.out.println("mmmm");
    this.executeMovement(requestedState) ;

    // updateSpeed and position
    applySpeedVector();

  }


}
