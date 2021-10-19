package playground;

import java.awt.Color;
import java.util.LinkedList;
import collider.Collider;
import controller.EgoController;
import controller.FallingStarController;
import controller.ObjectController;
import gameobjects.AnimatedGameobject;
import gameobjects.EgoObject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.TextObject;
import collider.*;

/** Fairly advanced SpaceInvaders level.
*/
public class BossLevel extends SpaceInvadersLevel {



  public BossLevel() {
    super();
  }

  public String getName() {
    return "BossLevel" ;
  }



  @Override
  // e : enemyObject
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {
    double gameTime = this.getGameTime();

    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= 10) {
      super.actionIfEnemyIsHit(e, shot);
    } else {
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
    }
    deleteObject(shot.getId());
  }



  @Override
  double calcEnemyShotProb() {
    return 1.5 * this.getTimestep();
  }

  @Override
  protected double calcEnemySpeedX() {
    return ENEMYSPEEDX * 2;
  }

  @Override
  protected double calcEnemySpeedY() {
    return ENEMYSPEEDY * 2;
  }

  @Override
  protected int calcNrEnemies() {
    return (int) 1;
  }

  @Override
  protected GameObject createEnemyShotObject(GameObject parentObject, String name,
      ObjectController limitedTimeController) {
    GameObject ego = this.getObject("ego");

    double deltax = parentObject.getX() - ego.getX();
    double deltay = parentObject.getY() - ego.getY();

    double norm = Math.sqrt(deltax * deltax + deltay * deltay);
    deltax *= -ENEMYSHOTSPEED / norm;
    deltay *= -ENEMYSHOTSPEED / norm;

    GameObject to = new TextObject(name, this, parentObject.getX(), parentObject.getY(), deltax,
        deltay, "*", 20, Color.GREEN).generateColliders().setController(limitedTimeController);
    return to;

  }


  @Override
  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {

    GameObject go = new AnimatedGameobject(name, this, this.canvasX / 2, 10, vx_enemy, 50,
        ENEMYSCALE * 3, this.enemyAnim, this.getGameTime(), "loop").generateColliders()
            .setController(enemyController);

    return go;
  }


  @Override
  protected String getStartupMessage() {
    return "BOSS LEVEL!";
  }



}
