package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import playground.*;
import controller.*;
import gameobjects.*;
import java.util.*;
import java.awt.event.*;


public class EgoController extends ObjectController {
  double rad = 0;

  Integer pressedKey = null;
  Integer lastPressedKey = null;

  public EgoController(double egoRad) {
    this.rad = egoRad;
  }


  public void onUp(KeyEvent kc, GameObject ego) {
    ego.setVX(0.0);
    ego.setVY(-SpaceInvadersLevel.EGOSPEED);
  }

  public void onDown(KeyEvent kc, GameObject ego) {
    ego.setVX(0.0);
    ego.setVY(SpaceInvadersLevel.EGOSPEED);
  }

  public void onLeft(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(-SpaceInvadersLevel.EGOSPEED);
  }

  public void onRight(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(SpaceInvadersLevel.EGOSPEED);
  }

  public void onStop(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(0.0);
    ego.setComponentProperty("controller", "setDummy", "NEW");
    ego.setComponentProperty("controller", "setDummy2", "XXX");
  }


  public boolean stopObject() {
    // check whether ego object is at level boundaries
    int pgSizeX = this.getPlayground().getSizeX();
    int pgSizeY = this.getPlayground().getSizeY();
    double ts = this.getTimestep();
    //System.out.println("TS "+ts+" ..... XY="+pgSizeX+"/"+pgSizeY) ;
    if (this.getX() + rad + this.getVX() * ts >= pgSizeX
        || this.getX() - rad + this.getVX() * ts < 0) {
      return true;
    }
    if (this.getY() + rad + this.getVY() * ts >= pgSizeY
        || this.getY() - rad + this.getVY() * ts < 0) {
      return true;
    }
    return false;

  }


  public void onSpace(KeyEvent e, GameObject ego) {
    pressedKey = lastPressedKey;
    lastPressedKey = null;

    // double gt = ego.getGameTime() ;

    // eindeutigen Namen für das neue Objekt erzeugen
    // dafür wird das Flag nextShot gelesen (falls es nicht eistiert)
    // und danach um 1 erhöht und zurückgeschrieben
    Integer nextShot =
        (Integer) this.getPlayground().getOrCreateLevelFlag("nextShot", Integer.valueOf(0));
    String shotName = "simpleShot" + nextShot++;
    this.getPlayground().setLevelFlag("nextShot", nextShot);

    SimpleShotController simpleshot = new SimpleShotController();
    GameObject ss = new RectObject(shotName, this.getPlayground(), ego.getX(), ego.getY(), 0,
        -1. * SpaceInvadersLevel.SHOTSPEED, 4, 12, Color.CYAN).setController(simpleshot);
    ss.generateColliders();
    this.getPlayground().addObject(ss);
  }


  public void updateObject() {

    // System.out.println("Playground inst is"+this.getPlayground()) ;
    Stack<KeyEvent> keyEvents = this.getPlayground().getKeyEvents();
    Stack<MouseEvent> mouseEvents = this.getPlayground().getMouseEvents();
    int pgSizeX = this.getPlayground().getSizeX();
    int pgSizeY = this.getPlayground().getSizeY();
    double ts = getPlayground().getTimestep();

    GameObject ego = this.gameObject;

    while (!keyEvents.isEmpty()) {

      KeyEvent e = keyEvents.pop();
      boolean pressed = false;
      boolean released = true;
      int kc = e.getKeyCode();

      if (e.paramString().indexOf("PRESSED") >= 0) {
        pressed = true;
        released = false;
      }

      /**
       * Generelle Idee: Wenn eine Taste gedrückt wird wird sie gespeichert. wenn die zuvor
       * gespeicherte Taste wieder losgelassen wird stoppt das Ego-Objekt. Falls vor dem Loslassen
       * eine andere Taste gedrückt wird, wird diese gespeichert und die alte vergessen. Dh das
       * loslassen der alten Taste stoppt das Objekt nicht. Spezialfall: space, das loslassen von
       * space stoppt das Objekt nicht!
       */

      if (pressed == true) {
        lastPressedKey = pressedKey;
        pressedKey = kc;
      }

      /**
       * Nur eine losgelassene Taste die auch vorher gedrückt wurde stoppt das Objekt. Eine
       * losgelassene Taste die nicht vorher gedrückt wurde bzw vergessen wurde stoppt das Objekt
       * nicht
       */
      if (released == true) {
        if (pressedKey != null) {
          if (pressedKey.equals(kc)) {
            ego.setVX(0);
            ego.setVY(0);
            pressedKey = null;
          }
        }
        continue;
      }

      if (kc == KeyEvent.VK_LEFT) {
        this.onLeft(e, ego);
      }

      if (kc == KeyEvent.VK_RIGHT) {
        this.onRight(e, ego);
      }

      if (kc == KeyEvent.VK_UP) {
        this.onUp(e, ego);
      }

      if (kc == KeyEvent.VK_DOWN) {
        this.onDown(e, ego);
      }

      // stop
      if (kc == KeyEvent.VK_Z) {
        this.onStop(e, ego);
      }


      // schiessen!
      if (kc == KeyEvent.VK_SPACE) {
        // space is not reg//istered! Releasing space does not stop the egoobject
        this.onSpace(e, ego);
      }
    }


    boolean stop = this.stopObject();

    if (stop) {
      this.setVX(0);
      this.setVY(0);
    }

    // updateSpeed and position
    applySpeedVector();

  }


}
