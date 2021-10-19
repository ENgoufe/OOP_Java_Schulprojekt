package collider;

import java.awt.Color;
import gameobjects.*;
import java.awt.Graphics2D;
import java.util.LinkedList;
import controller.ObjectController;
import playground.Playground;

/**
 * Collider for a circular object.
 * 
 * @author geppe
 *
 */
public class CircleCollider extends Collider {

  double x;
  double y;
  double vx;
  double vy;

  double r;

  /**
   * Constructor.
   * 
   * @param id Collider name
   * @param o parent object
   * @param radius radius of circle that triggers collisions
   */
  public CircleCollider(String id, GameObject o, double radius) {
    super(id, o);
    this.r = radius;
  }


  public String toString() {
    return "circ:" + x + " " + y + "/" + r + " ";
  }


  public boolean checkCollisionCircCirc(Collider _c2) throws Exception {
    CircleCollider c2 = (CircleCollider) _c2;
    CircleCollider c1 = this;
    // System.out.println(c1.x + " " + c1.y + " " + c1.r + " " + c2.x + " " + c2.y+ " " + c2.r);
    int kathete1 = (int) (Math.abs(c2.gameobject.getX() - c1.gameobject.getX()));
    int kathete2 = (int) (Math.abs(c2.gameobject.getX() - c1.gameobject.getY()));
    int hypothenuse = (int) (c1.r + c2.r);

    // System.out.println(kathete1 + " " + kathete2 + " " + hypothenuse + " ");

    if (((kathete1 ^ 2) + (kathete2 ^ 2)) <= (hypothenuse ^ 2)) {
      // System.out.println("Kollision");
      return true;
    }
    return false;
  }


  /**
   * example of how to implement own colliders. This one check for collisions with instances of
   * itself. For this, the code tries to cast the parameter other to CircleCollider. If it is
   * possible, collision is evaluated. If not, an exception is raised to signal that thos collider
   * type cannot be handled.
   * 
   * @param other subclass of {@link Collider}
   * @return false or true
   * @throws RuntimeException if other cannot be handled
   * 
   */
  @Override
  public boolean collidesWith(Collider other) {

    // circ circ
    try {
      return checkCollisionCircCirc(other);
    } catch (Exception e) {
    }

    try {
      return other.collidesWith(this);
    } catch (Exception e) {
    }

    throw new RuntimeException("Collider type not implemented!");
  }

  private Color color = Color.WHITE;



}
