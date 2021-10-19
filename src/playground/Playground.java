package playground;

import java.awt.Graphics2D;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.awt.event.*;
import collider.Collider;
import controller.ObjectController;
import gameobjects.GameObject;

/**
 * Playground represents a level of the game, focussing on the game LOGIC and the graphical
 * representation of the level itself (not its {@link GameObject} instances). In particular, an
 * instance of Playground
 * <ul>
 * <li>manages the different moving or static objects in a level (e.g., collisions, adding objects,
 * removing objects). This is mainly done by the methods {@link #addObject}, {@link #deleteObject}
 * and {@link #getObject}.
 * <li>processes keyboard inputs provided by GameMain
 * <li>represents the state of a level represented by <b>flags</b>. Each flag has a name (a String)
 * and an arbitrary value of any type. Methods: {@link #setLevelFlag}, {@link #getLevelFlag},
 * {@link #setGlobalFlag}, {@link #getGlobalFlag}. As an example, the current score is a flag
 * usually named "points", with an Integer as a value. This value can be retrieved and manipulated
 * using the above mentioned methods.
 * </ul>
 */
public abstract class Playground {

  public static final int FLAGS_GLOBAL = 1;
  public static final int FLAGS_LEVEL = 2;
  public static final int FLAGS_ALL = 3;
  protected int canvasX = -1;
  protected int canvasY = -1;

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, GameObject> gameObjects = new HashMap<String, GameObject>();

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, Object> flags = new HashMap<String, Object>();

  protected String level = "";
  protected double timeStep = 0;
  protected double gameTime = 0;
  LinkedList<GameObject> addables = new LinkedList<GameObject>();
  LinkedList<String> removables = new LinkedList<String>();
  // HashMap<Integer,Integer> keys ;
  Stack<KeyEvent> keyEvents;
  Stack<MouseEvent> mouseEvents;


  protected boolean pausedFlag = false;

  public Playground() {
    this.canvasX = -1;
    this.canvasY = -1;
  }

  // here, the level communicates its size preference to the GameUI
  // called automatically
  public abstract int preferredSizeX();

  public abstract int preferredSizeY();

  /** 
   * Returns the levels static name, to be redefined.
  */ 
  public abstract String getName() ;

  /**
   * Adds a graphics object to a level.
   * 
   * @param o GameObject The object to be added
   */
  public void addObject(GameObject o) {
    // gameObjects.put(o.getId(), o);
    addables.addLast(o);
  }

  /**
   * Adds a graphics object to a level. NOT recommended!
   * 
   * @param o GameObject The object to be added
   */
  public void addObjectNow(GameObject o) {
    gameObjects.put(o.getId(), o);
  }


  /**
   * Search all game objects for objects with a certain substring in their name. USEFUL!
   * 
   * @param substr String. The string that must be part of the object name if object is to be
   *        considered found.
   * @param filterInactive deprecated, does nothing regardless of value
   * @return a reference to a LinkedList filled with all objects that have <b>substr</b> in thier
   *         name
   */
  public LinkedList<GameObject> collectObjects(String substr, boolean filterInactive) {
    LinkedList<GameObject> l = new LinkedList<GameObject>();
    for (Map.Entry<String, GameObject> entry : this.gameObjects.entrySet()) {
      GameObject obj = entry.getValue();
      if (obj.getId().contains(substr)) {
        if (filterInactive == true) {
          if (obj.isActive()) {
            l.add(obj);
          }
        } else {
          l.add(obj);
        }
      }
    }
    return l;
  };


  /**
   * Removes a graphics object from a level.
   * 
   * @param id String The unique identifier of the object
   */
  public void deleteObject(String id) {
    // gameObjects.remove(id);
    removables.addLast(id);
  }

  /**
   * Removes a graphics object from a level immediately, NOT recommended.
   * 
   * @param id String The unique identifier of the object
   */
  public void deleteObjectNow(String id) {
    gameObjects.remove(id);
  }


  /**
   * Retrieves a graphics object by name.
   * 
   * @param id String Unique id of the object
   * @return reference to the requested game object, or null if not found
   */
  public GameObject getObject(String id) {
    return gameObjects.get(id);
  }

  /**
   * Sets a game-wide permanent flag. This will be prefixed by "/global" but you will never need to
   * put this directly, because the {@link #getGlobalFlag} method also add this automatically.
   * 
   * @param flag String Q unique name in this level. If it exists value is overwritten.
   * @param value Object Any Object can be the value of a flag!
   */
  public static void setGlobalFlag(String flag, Object value) {
    flags.put("/global/" + flag, value);
  }

  /**
   * Sets a level-wide permanent flag.
   * 
   * @param flag String Q unique name in this level. If it exists value is overwritten.
   * @param value Object Any Object can be the value of a flag!
   * @return the value that was just set
   */
  public Object setLevelFlag(String flag, Object value) {
    flags.put("/" + this.level + "/" + flag, value);
    return value;
  }

  /**
   * Deletes all flags.
   * 
   * @param mode Can be: FLAGS_ALL (all), FLAGS_GLOBAL(global), FLAGs_LEVEL(level)
   */
  public void resetFlags(int mode) {
    LinkedList<String> delKeys = new LinkedList<String>();
    for (Map.Entry<String, Object> entry : this.flags.entrySet()) {
      System.out.println(entry.getKey() + " IndexofGlobal = " + entry.getKey().indexOf("/global/"));
      if ((mode == FLAGS_GLOBAL) && (entry.getKey().indexOf("/global/") != -1)) {
        System.out.println("GLOABAL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      } else if ((mode == FLAGS_LEVEL) && (entry.getKey().indexOf("/global/") == -1)) {
        System.out.println("LEVEL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      } else if (mode == FLAGS_ALL) {
        System.out.println("ALL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      }
    }

    for (String str : delKeys) {
      System.out.println("removing key " + str);
      flags.remove(str);
    }
  }


  /**
   * Retrieves a game-wide flag by name.
   * 
   * @param flag String Unique flag id
   * @return the value associated with <b>flag</b>, or <b>null</b> if the flag does not exist.
   */
  public static Object getGlobalFlag(String flag) {
    return flags.get("/global/" + flag);
  }

  /**
   * Retrieves a game-wide flag by name. If it does not exist, it is created.
   * 
   * @param flag String Unique flag id
   * @param value Default value for the flag to be vreated if it does not exist.
   * @return the value associated with <b>flag</b>
   */
  public static Object getOrCreateGlobalFlag(String flag, Object value) {
    Object tmp = getGlobalFlag(flag);
    if (tmp == null) {
      setGlobalFlag(flag, value);
      return value;
    } else {
      return tmp;
    }
  }

  /**
   * Retrieves a level-wide flag by name. If it does not exist, retuns null
   * 
   * @param flag String Unique flag id
   * @return the value associated with <b>flag</b>, or null.
   */
  public Object getLevelFlag(String flag) {
    return flags.get("/" + this.level + "/" + flag);
  }

  /**
   * Retrieves a level-wide flag by name. If it does not exist, it is created.
   * 
   * @param flag String Unique flag id
   * @param createValue Default value for the flag to be vreated if it does not exist.
   * @return the value associated with <b>flag</b>
   */
  public Object getOrCreateLevelFlag(String flag, Object createValue) {
    Object tmp = getLevelFlag(flag);
    if (tmp == null) {
      return setLevelFlag(flag, createValue);
    } else {
      return tmp;
    }
  }


  /**
   * Reinitializes the level.
   */
  public void reset() {
    gameObjects.clear();
    // flags.clear();
  }


  public boolean isPaused() {
    return this.pausedFlag;
  }

  public void setPaused(boolean p) {
    this.pausedFlag = p;
  }

  public void togglePause() {
    pausedFlag = !pausedFlag;

  }


  /**
   * Internal, NEVER call directly.
   * 
   * @param keyEvents Set of all keyEvents received by the UI in this timestep.
   */
  public void processKeyEvents(Stack<KeyEvent> keyEvents) {
    //System.out.println("KEYS="+keyEvents.size()) ;
    this.keyEvents = keyEvents;
    this.setGlobalFlag("inputs", keyEvents);
  }


  /**
   * Internal, NEVER call directly.
   * 
   * @param mouseEvents Set of all MouseEvents received by the UI in this timestep.
   */
  public void processMouseEvents(Stack<MouseEvent> mouseEvents) {
    this.mouseEvents = mouseEvents;
    this.setGlobalFlag("inputs", mouseEvents);
  }


  /**
   * Can be called by, e.g., {@link controller.ObjectController} instances to react to user inputs.
   * 
   * @return list of current unhandled key events
   */
  public Stack<KeyEvent> getKeyEvents() {
    return this.keyEvents;
  }

  /**
   * Can be called by, e.g., {@link controller.ObjectController} instances to react to user inputs.
   * 
   * @return list of current unhandled mouse events
   */
  public Stack<MouseEvent> getMouseEvents() {
    return this.mouseEvents;
  }


  /**
   * Method meant to be filled with own code, handles the entire game logic (collision checks, timed
   * events, ...).
   */
  public abstract void applyGameLogic();

  public abstract boolean gameOver();

  public abstract boolean levelFinished();

  public abstract boolean resetRequested();

  public int getSizeX() {
    return canvasX;
  }

  public int getSizeY() {
    return canvasY;
  }

  /**
   * Calls all object update methods in level. Internal, never call directly.
   * 
   */
  public void updateObjects() {
    for (GameObject gameObject : gameObjects.values()) {
      if (gameObject.isActive() == true) {
        gameObject.updateObject();
        // System.out.println(gameObject.scol);
      }
    }

    for (GameObject o : addables) {
      addObjectNow(o);
    }

    for (String s : removables) {
      deleteObjectNow(s);
    }
    removables.clear();
    addables.clear();
  }

  public void setTimestep(double s) {
    timeStep = s;
  }

  public double getTimestep() {
    return timeStep;
  }

  public void setGameTime(double s) {
    this.gameTime = s;
  }

  public double getGameTime() {
    return this.gameTime;
  }


  /**
   * Installs a level controller, i.e., a normal ObjectController instance
   * attached to a dummy object that is invisible and has not collider.
   * Since every controller has access to the Playground instance of its 
   * associated object, it can control the Playground instance. For example, 
   * applyGameLogic() could be replaced by a dedicated LevelController.
   */
  public void setLevelController(ObjectController ctrl) {
    GameObject dummyObject = new GameObject("dummyControlObj", this, ctrl, 0,0, 0,0) ;
    addObject(dummyObject) ;
  }


  /**
   * To be redefined!! Draws mainly h level background and global information like points etc.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public abstract void redrawLevel(Graphics2D g2);

  /**
   * Internal, do not call directly.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public void redraw(Graphics2D g2) {
    redrawLevel(g2);
    for (GameObject gameObject : gameObjects.values()) {
      if (gameObject.isActive()) {
        gameObject.draw(g2);
      }
    }
  }

  /**
   * Sets up a single level. Prepares all objects etc. Is called ONCE at the start of each level by
   * the game engine.
   * 
   * @param level String a string identifying the level number etc
   */
  public abstract void prepareLevel(String level);


}
