package playground;

import java.util.LinkedList;
import collider.Collider;
import controller.FallingStarController;
import controller.ObjectController;
import controller.ZickZackController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import controller.LimitedTimeController;
import gameobjects.TextObject;
import java.awt.Color;
import java.awt.Font;



/**
 * Simple spaceinvaders derivative.
 * <ul>
 * <li>Hit aliens twice to kill them
 * <li>they say AUA when not destroyed
 * </ul>
 */
public class Level4 extends SpaceInvadersLevel {
  public static final int MAX_HITS = 2;

  public Level4() {
    super();
  }

  public String getName() {
    return "Level4" ;
  }


  // kleiner Tipp!
  @Override
  protected String getStartupMessage() {
    return "Jetzt gibts Saures!";
  }

  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {
    double gameTime = this.getGameTime();



    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= MAX_HITS) {
      super.actionIfEnemyIsHit(e, shot);
    } else {
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
      // spawn a bonus points object
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVY();

      LimitedTimeController bonusTextController =
          new LimitedTimeController(gameTime, SpaceInvadersLevel.EXPL_DURATION);
      GameObject bonusText = new TextObject("bonus" + e.getId(), this, e.getX(), e.getY(), vx, vy,
          "Aua", 20, Color.YELLOW).setController(bonusTextController);

      this.addObject(bonusText);
    }
    deleteObject(shot.getId());

  }



}
