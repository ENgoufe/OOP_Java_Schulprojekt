package gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import collider.*;
import controller.ObjectController;
import playground.Playground;
import rendering.*;

/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a circular white point. The controller is left undefined, the collider as well. FallingStar
 * object rarely need a collider so no generateColliders method is implemented.
 *
 */
public class FallingStar extends GameObject {

  private Color color = Color.WHITE;

  protected double rad = -1;


  /**
   * Constructor.
   * 
   * @param id object name
   * @param playground Level instance
   * @param x posx
   * @param y posy
   * @param vx speedx
   * @param vy speedy
   * @param color Star color, see java.awt.Color
   * @param rad radius of the star
   */
  public FallingStar(String id, Playground playground, double x, double y, double vx, double vy,
      Color color, double rad) {
    super(id, playground, x, y, vx, vy);
    this.rad = rad;
    this.color = color;
    LinkedList<Collider> cols = new LinkedList<Collider>();
    CircleCollider cc = new CircleCollider("cc", this, rad);
    cols.add(cc);
    setColliders(cols);


    this.artist = new CircleArtist(this, rad, color);
  }

}
