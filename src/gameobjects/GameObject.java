package gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.lang.reflect.*;
import rendering.*;
import collider.Collider;
import controller.ObjectController;
import playground.Playground;

/**
 * The class {@link GameObject} represents an object appearing in a level of the
 * game described by an instance of the class {@link playground.Playground}. An instance of {@link GameObject} 
 * has the following properties:
 * <ul>
 * <li>a name that is unique within a certain {@link Playground}
 * <li>2D screen position x and y in pixels
 * <li>2D speed x and y in pixels/s
 * <li>a reference to the {@link playground.Playground} object it belongs to
 * </ul>
 * <br>
 * In order to allow a flexible composition of object properties, an instance of {@link GameObject} is an
 * aggregation of up to three instances of other classes. To put it another way, each instance has 
 * attributes that are references to:
 * <ul>
 * <li>a subclass of {@link controller.ObjectController} that handles the movement/input logic
 * of the object<br>
 * <li>a subclass of {@link collider.Collider} that handles the collision detection of the
 * object<br>
 * <li>a subclass of {@link rendering.Artist} that draws the object<br>
 * </ul>
 * When a GameObject is constructed using <strong>new</strong>, all of these references are set to <strong>null</strong>.
 * In this state, the object does nothing, collides with nothing and is not visible because it is not drawn.  
 * You can use calls to the methods {@link #setColliders}, {@link #addArtist}, {@link #addCollider} and
 * {@link #setController} to set these references later, thus giving to an object a behavior, the ability to collide with others, 
 * and a visual appearance.
 * <br>
 * Normally, it is not necessary to create subclasses of GameObject since the composition of
 * different behavior, collision and appearance properties is governed by subclassing the classes
 * {@link controller.ObjectController}, {@link collider.Collider} and {@link rendering.Artist} and then adding them to the
 * GameObject instance. <br>
 * However, some subclasses of GameObject define convenience constructors where you can directly
 * provide these references to have more readable code. Examples are: {@link AnimatedGameobject},
 * {@link TextObject}, {@link RectObject} and {@link EgoObject}.
 */
public class GameObject {

  public static final int RADIUS = 0;
  public static final int RECTANGLE = 1;
  public static final int MASK = 2;

  protected Artist artist = null;

  public String id = null;
  protected double x = 0;
  protected double vx = 0;
  protected double y = 0;
  protected double vy = 0;
  protected BufferedImage mask = null; // TODO!!
  protected boolean active = true;
  // public int collisionMode = GameObject.RADIUS;
  protected Playground playground = null;

  private ObjectController controller = null;
  public LinkedList<Collider> scol;
  
  public String getName() {
    return this.id ;
  }


  /**
   * convenience constructor.
   * 
   * @param id Object name, must be unique
   * @param playground Level this object should be part of
   * @param controller Can be null if no controller is needed
   * @param x X position
   * @param y Y poition
   * @param vx X speed
   * @param vy Y speed
   */
  public GameObject(String id, Playground playground, ObjectController controller, double x,
      double y, double vx, double vy) {
    setX(x);
    setY(y);
    setVX(vx);
    setVY(vy);
    this.id = id;
    this.controller = controller;
    this.controller.setObject(this);
    this.controller.setPlayground(playground);
    this.setPlayground(playground);
  }


  /**
   * primary constructor. Intializes al aggregated instances to null, need to be set afterwards.
   * 
   * @param id Object name, must be unique
   * @param playground Level this object should be part of
   * @param x X position
   * @param y Y poition
   * @param vx X speed
   * @param vy Y speed
   */
  public GameObject(String id, Playground playground, double x, double y, double vx, double vy) {
    setX(x);
    setY(y);
    setVX(vx);
    setVY(vy);
    this.id = id;
    this.controller = null;
    this.scol = new LinkedList<Collider>();
    this.setPlayground(playground);
  }


  /**
   * Can be overwritten in subclasses to generate specific colliders automatically. For example, a
   * {@link TextObject} knows its own shape so it can generate the necessary rectangular collider
   * automatically.
   * 
   * @return The instance of GameObject that is modified (i.e., this)
   */
  public GameObject generateColliders() {
    return this;
  }

  /**
   * sets colliders.
   * 
   * @param l LinkedList of Colliders
   */
  public void setColliders(LinkedList<Collider> l) {
    this.scol = l;
  }


  public GameObject setController(ObjectController c) {
    this.controller = c;
    this.controller.setObject(this);
    this.controller.setPlayground(playground);
    return this;
  }

  /**
   * Sets a new object controller. Can be done anytime at all.
   * 
   * @param controller An instance of {@link ObjectController} or one of its subclasses.
   */
  public void setObjectController(ObjectController controller) {
    this.controller = controller;
  }

  /**
   * Returns ref 2 object controller.
   * 
   * @return Reference to ObjectController instance.
   */
  public ObjectController getObjectController() {
    return this.controller;
  }


  /**
   * set the Artist reference of this GmeObject.
   * 
   * @param a This subclass of {@link Artist}
   * @return this
   */
  public GameObject addArtist(Artist a) {
    this.artist = a;
    return this;
  }

  /**
   * Add a collider to the existing list of colliders. If it is null, create it and add the collider
   * anyway.
   * 
   * @param c A subclass of {@link Collider}
   * @return this
   */
  public GameObject addCollider(Collider c) {
    if (this.scol == null) {
      this.scol = new LinkedList<Collider>();
    }
    this.scol.add(c);
    return this;
  }

  // -------------

  public Playground getPlayground() {
    return playground;
  }

  public void setPlayground(Playground playground) {
    this.playground = playground;
  }

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name so it does not interfere with same flags in other objects.
   * 
   * @param flag Flag name
   * @param value Flag value
   */
  public void setObjectFlag(String flag, Object value) {
    this.playground.setLevelFlag(this.id + "/" + flag, value);
  }

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name.
   * 
   * @param flag Flag name
   * @return flag value, null if flag does not exist
   */
  public Object getObjectFlag(String flag) {
    return this.playground.getLevelFlag(this.id + "/" + flag);
  }

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name.
   * 
   * @param flag Flag name
   * @param createValue If flag does not exist, create it with this value
   * @return The flag's value, is of type Object so it must be cast
   */
  public Object getOrCreateObjectFlag(String flag, Object createValue) {
    return this.playground.getOrCreateLevelFlag(this.id + "/" + flag, createValue);
  }


  /**
   * Called by the game engine, DO NOT CALL YOURSELF. Is used to detect collision of this with
   * another GameObject instance other. The task is delegated to the list of aggregated colliders.
   * If there is no aggregated collider, no collisions are reported.
   * 
   * @param other, a GameObject procided by the game engine
   * @return true or false, depending on whether this an other collide, as determined by both their
   *         colliders
   */
  public boolean collisionDetection(GameObject other) {
    if (this.scol == null) {
      return false;
    }
    for (Collider c : this.scol) {
      // System.out.println(other.id);
      for (Collider o : other.scol) {
        if (c.collidesWith(o)) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Is called by the game engine once per time step, DO NOT CALL YOURSELF. Gives an object the
   * chance to react to the current game state, change its position and speed a.s.o, react to user
   * inputs, ... All that is done here is to delegate that task to the aggregated subclass of
   * {@link ObjectController}. If that ref is null, nothing happens.
   */
  public void updateObject() {
    if (this.controller != null) {
      controller.updateObject();
    }
  }

  public boolean isActive() {
    return active;
  }

  public GameObject setActive(boolean flag) {
    this.active = flag;
    return this;
  }

  /**
   * gets the screen X position.
   * 
   * @return double screen x position
   */
  public double getX() {
    return x;
  }

  /**
   * gets the screen Y position.
   * 
   * @return double screen Y position
   */
  public double getY() {
    return y;
  }

  /**
   * gets the screen X speed in pixels per frame.
   * 
   * @return double screen x speed
   */
  public double getVX() {
    return vx;
  }

  /**
   * gets the screen Y speed in pixels per frame.
   * 
   * @return double screen y speed
   */
  public double getVY() {
    return vy;
  }

  /**
   * set screen x position.
   * 
   * @param x double new position
   */
  public void setX(double x) {
    if (this.active == true) {
      this.x = x;
    }
  }

  /**
   * set screen y position.
   * 
   * @param y double new position
   */
  public void setY(double y) {
    if (this.active == true) {
      this.y = y;
    }
  }

  /**
   * set screen x speed.
   * 
   * @param vx double new x position
   */
  public void setVX(double vx) {
    if (this.active == true) {
      this.vx = vx;
    }
  }

  /**
   * set screen y speed in pix per frame.
   * 
   * @param vy double new y speed.
   */
  public void setVY(double vy) {
    if (this.active == true) {
      this.vy = vy;
    }
  }

  /**
   * return the unique object ID.
   * 
   * @return String unique object ID
   */
  public String getId() {
    return id;
  }


  /**
   * Delegate gameTime computation to the Playground that a GameObject is part of.
   * 
   * @return The game time in seconds
   */
  public double getGameTime() {
    return this.playground.getGameTime();
  }

  /**
   * Draws the object in its current state. Is called by the game engine, should NOT be called by
   * the user. All it does it to delegate the task to the aggregated subclass of {@link Artist}. If
   * this referene is null, nothing happens.
   * 
   * @param g Graphics2D object that has all the necessary drawing functionalities
   */
  public void draw(Graphics2D g) {
    if (this.artist != null) {
      this.artist.draw(g);
    }
  }


  /**
   * Experimental, use at your own risk. Uses reflection to call a particular setter method in one
   * of the aggregated instances. Assuming that is exists of course. Duh.
   * 
   * @param comp The aggregated instance to use. For now, only "controller" is supported.
   * @param property The property to be set. Assumes that an appropriate setter exists.
   * @param value Value to be set.
   */
  public void setComponentProperty(String comp, String property, Object value) {
    if (comp.equals("controller")) {
      Class<? extends Object> clO = this.controller.getClass();
      for (Method m : clO.getMethods()) {
        if (m.getName().indexOf(property) != -1) {
          System.out.println("Method " + property + " found!!");
          try {
            m.invoke(this.getObjectController(), value);
          } catch (Exception e) {
          }
        }
      }

    }
  }
}
