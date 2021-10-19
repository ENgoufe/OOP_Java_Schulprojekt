package playground;

import gameobjects.* ;
import controller.* ;
import collider.* ;
import java.awt.Color ;



public class ExperimentierLevel6 extends SpaceInvadersLevel {

  public ExperimentierLevel6() {
    // TODO Auto-generated constructor stub
  }

  public String getName() {
    return "ExpLevel6" ;
  }

  
  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {
    if (!(e.getName().equals("fly_enemy1") || e.getName().equals("fly_enemy2"))) {
      super.actionIfEnemyIsHit(e, shot) ;
    }    
  }
  
  
  
  @Override
  void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {
    if (enemy.getName().equals("fly_enemy1") || enemy.getName().equals("fly_enemy2")) {
      System.out.println("XXXXXXXXXXXXXXx") ;
      this.setGlobalFlag("egoLives", Integer.valueOf(0)) ;
      
    } else {
      super.actionIfEgoCollidesWithEnemy(enemy, ego) ;
    }
    
  }
  
  @Override
  public void prepareLevel(String id) {
    super.prepareLevel(id) ;
    
    RectObject ro1 = new RectObject("fly_enemy1", this, 300,300,130,100,30,10,Color.YELLOW) ;
    RectObject ro2 = new RectObject("fly_enemy2", this, 300,300,-80,70,30,10,Color.GREEN) ;
    
    ReboundController rbc1 = new ReboundController() ;
    ReboundController rbc2 = new ReboundController() ;
    
    ro1.setController(rbc1) ;
    ro2.setController(rbc2) ;       
    
    this.addObject(ro1) ;
    this.addObject(ro2) ;
    
    ro1.addCollider(new RectCollider("coll1", ro1, 30,10)) ;
    ro2.addCollider(new RectCollider("coll2", ro2, 30,10)) ;
    
  }

}
