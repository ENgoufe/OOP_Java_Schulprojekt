package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import playground.*;
import controller.*;
import collider.*;
import gameobjects.*;
import java.util.*;
import java.awt.event.*;

/** Ein InputController erhält alle Inputs (Keyboard, Maus) und setzt sie in
 *  Level-Flags um. Diese Level-Flags können dann von anderen Controllern gelesen werden.
*   So muss ein Objekt-Controller nicht jedesmal eine potentiell komplizierte Steuerung
*   implementieren.
*/
public class LevelController extends ObjectController {
  double t0 = 0;

  Integer pressedKey = null;
  Integer lastPressedKey = null;

  GameObject clickObject = null ;
  RectCollider clickCollider = null ;

  HashMap<Integer,Boolean> memory = new HashMap<Integer,Boolean>() ;
  HashMap<Integer,String> keyTable = new HashMap<Integer,String>() ;


  public LevelController() {
    // click object dient dazu festzustellen mit welchem Objekt des Playground des Mausklick kollidiert
    this.clickObject = new GameObject("__clickObject", null,  -1,-1,0,0) ;
    this.clickCollider = new RectCollider("__clickCollider", this.clickObject, 5.,5.) ;
    this.clickObject.addCollider(this.clickCollider) ;

    this.keyTable.put(KeyEvent.VK_RIGHT,"right") ;
    this.keyTable.put(KeyEvent.VK_LEFT,"left") ;
    this.keyTable.put(KeyEvent.VK_DOWN,"down") ;
    this.keyTable.put(KeyEvent.VK_UP,"up") ;
    this.keyTable.put(KeyEvent.VK_SPACE,"shoot") ;

  }

  public void updateObject() {

    Stack<KeyEvent> keyEvents = getPlayground().getKeyEvents();
    Stack<MouseEvent> mouseEvents = getPlayground().getMouseEvents();
    int pgSizeX = getPlayground().getSizeX();
    int pgSizeY = getPlayground().getSizeY();
    double ts = getPlayground().getTimestep();
    //System.out.println("Playground inst is"+this.getPlayground().getKeyEvents()) ;

    // wir loopen über alle neuen Maus-Events und publizieren sie
    while (mouseEvents.isEmpty()==false) {
      MouseEvent e = mouseEvents.pop();
      getPlayground().setLevelFlag("mouseX", e.getX()) ;
      getPlayground().setLevelFlag("mouseX", e.getY()) ;
      getPlayground().setLevelFlag("mousebutton", e.getButton()) ;

      // extra-Funktionalität, kann auch mal in eine Unterklasse abgeladen werden. 
      // bei Links-Klicks wird berstimmt auf welches GameObject geklickt wurde.
      if (e.getButton() == MouseEvent.BUTTON1) {
        LinkedList<GameObject> options = this.getPlayground().collectObjects("option_", true);
        this.clickObject.setX(e.getX()) ;
        this.clickObject.setY(e.getY()) ;
        for (GameObject option : options ) {
          if (this.clickObject.collisionDetection(option)) {
            getPlayground().setGlobalFlag("user_choice",option.getId()) ;
          }
        }
        
      }
    } // while


    // selbes Spuiel mit Keyboard-Events
    // auslesen und als Level-Flags publizieren!
    int i=-1 ;
    memory.clear() ;
    while (!keyEvents.isEmpty()) {
      //System.out.println("Iteration  is"+ (++i)) ;
      KeyEvent e = keyEvents.pop();
      boolean pressed = false;
      boolean released = true;
      int kc = e.getKeyCode();
      //System.out.println("KEY is"+e.paramString()) ;

      if (e.paramString().indexOf("PRESSED") >= 0) {
        pressed = true;
        released = false;
      }
      // ok komplziert: wenn in einem update-Zyklus eine Taste sowohl geddrückt als auch losgelassen wird
      // (gibt es)  --> dann gilt sie als losgelassen, dh der Drück-Event wird ignoriert
      Boolean memAccess =  memory.get(kc) ;
      if (memAccess == null) ;
      else if (memAccess.equals(false)) continue ;
      memory.put(kc,pressed) ;

      String rel = (pressed?"pressed":"released") ;
      String inv = (pressed?"released":"pressed") ;

      for (HashMap.Entry<Integer,String> entry : keyTable.entrySet()) {
        Integer key = entry.getKey();
        String keyName = entry.getValue();

        if (kc == key) {
          getPlayground().setLevelFlag(keyName+'_'+rel, true) ;
          getPlayground().setLevelFlag(keyName+'_'+inv, false) ;
        }  
      }
    } 



  }


}
