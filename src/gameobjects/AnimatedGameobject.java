package gameobjects;

import java.awt.Graphics2D;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import controller.ObjectController;
import playground.Playground;
import playground.Animation;
import collider.Collider;
import collider.RectCollider;
import collider.CircleCollider;
import rendering.*;


/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a bitmap animation described by the {@link Animation} class. The controller is left
 * undefined, the collider as well. A cll to gneerateCalliders will put in place a correct
 * rectangular collider.
 *
 */

public class AnimatedGameobject extends GameObject {

  protected AnimationArtist animArtist;

  /**
   * auto-generates collider according to box width/height
   * 
   * @return this
   */
  public GameObject generateColliders() {
    LinkedList<Collider> cols = new LinkedList<Collider>();
    // System.out.println("Created enelmy "+ this.animArtist.getW()+" "+this.animArtist.getH()) ;
    double w = this.animArtist.getW();
    double h = this.animArtist.getH();
    this.addCollider(new RectCollider("RectColl_" + this.id, this, w, h));

    System.out.println("ANIMGO-COLL ID=" + this.getId() + " WH= " + w + "/" + h);
    return this;
  }


  /**
   * Constructor.
   * 
   * @param id object name
   * @param pg Playground instance decribing level
   * @param x posx
   * @param y posy
   * @param vx speedx
   * @param vy speedy
   * @param scale scale the animation by this factor
   * @param anim Animation instance into which avalid animation has been loaded.
   * @param t0 game time at construction of the object. Needed to control replay.
   * @param abspielmodus forward|backwards|loop are options here
   */
  public AnimatedGameobject(String id, Playground pg, double x, double y, double vx, double vy,
      double scale, Animation anim, double t0, String abspielmodus) {
    super(id, pg, x, y, vx, vy); // Konstruktor-Aufruf GameObject

    this.artist = new AnimationArtist(this, anim, t0, abspielmodus, scale);
    this.animArtist = (AnimationArtist) (this.artist);

  }


}
