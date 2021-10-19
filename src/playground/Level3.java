package playground;

import java.util.LinkedList;
import collider.Collider;
import controller.FallingStarController;
import controller.ObjectController;
import controller.ZickZackController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import collider.*;


/** SpaceInvaders Level, Aliens move in zigzag! */
public class Level3 extends SpaceInvadersLevel {

  public Level3() {
    super();
  }


  public String getName() {
    return "Level3" ;
  }


  // kleiner Tipp!
  @Override
  protected String getStartupMessage() {
    return "Get ready for level 3!!!";
  }


  @Override
  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {
    ObjectController zzController = new ZickZackController(gameTime, 0.5);
    GameObject go = new AnimatedGameobject(name, this, x_enemy, y_enemy, vx_enemy, vy_enemy,
        ENEMYSCALE, this.enemyAnim, this.getGameTime(), "loop").setController(zzController)
            .generateColliders();

    return go.generateColliders();
  }



}
