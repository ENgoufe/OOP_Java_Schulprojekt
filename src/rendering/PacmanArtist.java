package rendering;

import gameobjects.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

/**
 * Rendering an object as a filled circle of a specified color and size.
 */
public class PacmanArtist extends Artist {

  protected double egoRad;
  protected Color color;
  protected boolean mouthOpen = true ;
  protected int direction = 0 ; // 0, 90, 180, 270

  public PacmanArtist(GameObject go) {
    super(go);
    this.egoRad = 15.0;
    // TODO Auto-generated constructor stub
  }

  public PacmanArtist(GameObject go, double egoRad, Color color) {
    super(go);
    this.egoRad = egoRad;
    this.color = color;
  }


  public void setMouthState(boolean mouthOpen) {
    this.mouthOpen = mouthOpen ;
  }


  public void setDirection(int direction) {
    this.direction = direction ;
  }

  /**
   * Draw the circle.
   * 
   * @param g The Swing graphics context.
   */
  @Override
  public void draw(Graphics2D g) {
    g.setColor(this.color);
    double x = this.getX();
    double y = this.getY();
    int posX = (int) (x - egoRad);
    int posY = (int) (y - egoRad);
    int rad = (int) (2 * egoRad);
    if (this.mouthOpen == false) {
      g.fillOval(posX, posY, rad, rad);
    } else
    {
      g.setPaint(this.color);
      g.fill(new Arc2D.Double(posX, posY, 2*egoRad, 2*egoRad, this.direction+23, 
                                 320, Arc2D.PIE));
    }
  }

}
