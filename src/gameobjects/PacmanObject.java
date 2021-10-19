package gameobjects;


import java.awt.Color;
import collider.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import collider.Collider;
import controller.ObjectController;
import playground.Playground;
import rendering.*;

/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a circular white object. The controller is left undefined, the collider as well. However, a
 * single call to the overwritten method {@link #generateColliders} will in fact generate a circle
 * collider of just the right size.
 *
 */
public class PacmanObject extends GameObject {

  double egoRad = 0;

  /**
   * Constructor.
   * 
   * @param id object name
   * @param pg containing {@link Playground} instance
   * @param x positionx
   * @param y positiony
   * @param vx speedx
   * @param vy speedy
   * @param egoRad circle radius
   */

  public PacmanObject(String id, Playground pg, double x, double y, double vx, double vy,
      double egoRad) {
    super(id, pg, x, y, vx, vy);
    this.egoRad = egoRad;
    this.addArtist(new PacmanArtist(this, egoRad, Color.WHITE));
  }


  public void setDirection(int direction) {
    PacmanArtist pa = (PacmanArtist)(this.artist) ;
    pa.setDirection(direction) ;
  }


  public void setMouthState(boolean mouthOpen) {
    PacmanArtist pa = (PacmanArtist)(this.artist) ;
    pa.setMouthState(mouthOpen) ;
  }

  public GameObject generateColliders() {
    CircleCollider coll = new CircleCollider("coll", this, this.egoRad);
    this.addCollider(coll);
    return this;
  }

}
