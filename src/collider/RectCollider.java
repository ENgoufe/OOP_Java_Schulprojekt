package collider;

import java.awt.Color;
import gameobjects.*;
import java.awt.Graphics2D;
import java.util.LinkedList;
import controller.ObjectController;
import playground.Playground;

/**
 * Collider for a rectangular object.
 * 
 * @author geppe
 *
 */

public class RectCollider extends Collider {

  // double x;
  // double y;
  // double vx;
  // double vy;
  double w, h;

  private Color color = Color.WHITE;

  public RectCollider(String id, GameObject o, double w, double h) {
    super(id, o);
    this.w = w;
    this.h = h;
  }

  public String toString() {
    return " " + w + " " + h + " ";
  }

  public boolean checkCollisionRectRect(Collider other) {
    RectCollider r1 = this;
    RectCollider r2 = (RectCollider) other;

    if ((((r1.getX() + r1.w / 2.) >= (r2.getX() - r2.w / 2.))
        && ((r1.getX() + r1.w / 2.) <= (r2.getX() + r2.w / 2.)))
        || (((r2.getX() + r2.w / 2.) >= (r1.getX() - r1.w / 2.))
            && ((r2.getX() + r2.w / 2.) <= (r1.getX() + r1.w / 2.)))) {
      if ((((r1.getY() + r1.h / 2.) >= (r2.getY() - r2.h / 2.))
          && ((r1.getY() + r1.h / 2.) <= (r2.getY() + r2.h / 2.)))
          || (((r2.getY() + r2.h / 2.) >= (r1.getY() - r1.h / 2.))
              && ((r2.getY() + r2.h / 2.) <= (r1.getY() + r1.h / 2.)))) {
        return true;
      }
    }
    return false;
  }

  public boolean checkCollisionRectCirc(Collider other) {
    RectCollider r = this;
    CircleCollider c = (CircleCollider) (other);
    double circleDistX = Math.abs(c.getX() - (r.getX()));
    double circleDistY = Math.abs(c.getY() - (r.getY()));

    // System.out.println("c.x:"+c.x+" "+"c.y:"+c.y+" "+"c.r:"+c.r+" "+"r.x:"+r.x+" "+"r.y:"+r.y+"
    // "+"r.w:"+r.w+" "+"r.h:"+r.h+" "+"circleDistX:"+circleDistX+" "+"circleDistY:"+circleDistY);

    if (circleDistX > (r.w / 2 + c.r))
      return false;
    if (circleDistY > (r.h / 2 + c.r))
      return false;

    if (circleDistX <= (r.w / 2)) {
      // System.out.println("Kollision Rechteck mit Kreis");
      return true;
    }
    if (circleDistY <= (r.h / 2)) {
      // System.out.println("Kollision Rechteck mit Kreis2");
      return true;
    }

    double cornerDistSqr = Math.pow(circleDistX - r.w / 2, 2) + Math.pow(circleDistY - r.h / 2, 2); // Satz
                                                                                                    // des
                                                                                                    // Pythagoras
    return (cornerDistSqr <= c.r * c.r); // falls true zurueckgegeben: Kollision
  }


  /**
   * example of how to implement own colliders. This one check for collisions with instances of
   * itself, and of {@link CircleCollider}. For this, the code tries to cast the parameter other to
   * both classes. If it is possible, collision is evaluated. If not, an exception is raised to
   * signal that the collider type cannot be handled.
   * 
   * @param other subclass of {@link Collider}
   * @return false or true
   * @throws RuntimeException if other cannot be handled
   * 
   */
  @Override
  public boolean collidesWith(Collider other) {

    // rect circ
    try {
      return checkCollisionRectCirc(other);
    } catch (Exception e) {
      // do nothing
    }

    // rect rect
    try {
      return checkCollisionRectRect(other);
    } catch (Exception e) {
      // do nothing
    }

    try {
      return other.collidesWith(this);
    } catch (Exception e) {
      // do nothing
    }

    throw new RuntimeException("Collider type not implemented!");
  }

}
