package playground;

import controller.FallingStarController;
import controller.LimitedTimeController;
import controller.ObjectController;
import controller.ZickZackController;
import controller.MineController;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.TextObject;
import gameobjects.RectObject;
import java.awt.Color;
import java.util.LinkedList;
import collider.Collider;

/**
 * SpaceInvaders Level. You have to hit aliens 3 times to destroy them.
 */
public class HitTwiceLevel extends SpaceInvadersLevel {

  public static final int MAX_HITS = 3;

  public HitTwiceLevel() {
    super();
    this.level = "hitTwice";
  }


  public String getName() {
    return "HitTwiceLevel" ;
  }


  // kleiner Tipp!
  @Override
  protected String getStartupMessage() {
    return "3 shots / alien required!!!";
  }

  protected int calcNrEnemies() {
    return 10;
  }


  public void setupInitialState() {
    super.setupInitialState();

    GameObject ro = new RectObject("obstacleRect", this, 600, 300, 0, 0, 20, 100, Color.RED)
        .generateColliders();
    this.addObject(ro);

  }


  @Override
  // e : enemyObject
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {
    double gameTime = this.getGameTime();

    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= MAX_HITS) {
      super.actionIfEnemyIsHit(e, shot);
    } else {
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
    }
    deleteObject(shot.getId());
  }



}
