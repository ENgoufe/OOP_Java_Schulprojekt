package collider;

import java.awt.Graphics2D;
import java.util.LinkedList;
import gameobjects.GameObject;
import playground.Playground;
import controller.ObjectController;

/**
 * Abstract base class for all collider types. Colliders detect when one GameObject
 * intersects/collides with another. For this purpose, a collider must be fitte to the shape of an
 * object. Each GameObject can have several colliders, since complex ahapes need to be approximated
 * by basic shapes like circle, and rectangle. These basic shapes are represented by
 * {@link CircleCollider} and {@link RectCollider}. <br>
 * New colliders can be added representing more complex ways that objects can collide. For this, you
 * subclass {@link Collider} and overwrite the method {@link #collidesWith}. To ensire consistency,
 * each new collider must be able to handle all old collider classes, but the old ones do not need
 * to be changed. To handle an old collider class X, in {@link #collidesWith} you cast the argument
 * to class X. If this is not possible, you raise an exception. If it is possible, return true or
 * false depending on whether there is a collision. See {@link CircleCollider} and
 * {@link RectCollider} for exa�ples.
 * 
 * @author geppe
 *
 */
public abstract class Collider {


  public String id = null;

  protected GameObject gameobject = null;

  protected double dx = 0.;
  double dy = 0.;

  /**
   * Constructor.
   * 
   * @param id unique name of collider instance
   * @param o parent object, {@link GameObject} or subclass
   */
  public Collider(String id, GameObject o) {
    this.gameobject = o;

    this.id = id;

  }


  public Collider setOffsets(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
    return this;
  }

  public String toString() {
    return "baseColl";
  }

  public double getX() {
    return this.gameobject.getX() + this.dx;
  }


  public double getY() {
    return this.gameobject.getY() + this.dy;
  }


  public String getId() {
    return id;
  }


  public void setObject(GameObject gameObject) {
    this.gameobject = gameObject;
  }


  /**
   * Collision detection with other Collider.
   * 
   * @param other Subclass of {@link Collider}
   * @return true or false, depending on whether there is an exception
   */
  abstract public boolean collidesWith(Collider other);


}
