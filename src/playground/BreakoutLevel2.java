package playground;

import java.awt.Color;
import controller.EgoController;
import controller.*;
import controller.ReboundController;
import collider.RectCollider;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;
import gameobjects.TextObject;

public class BreakoutLevel2 extends BreakoutLevel1 {
  
  
  
  
  
  @Override 
  public void actionIfBallHitsBrick(GameObject ball, GameObject brick) {
    super.actionIfBallHitsBrick(ball, brick) ;
    this.setGlobalFlag("points", (Integer)this.getGlobalFlag("points")+10 ) ;
    this.counter.setText(this.getGlobalFlag("points").toString()) ;
  }
  
  public void applyGameLogic() {
    
    
    if (this.ball.getY() > this.ego.getY()+10) {
      this.ball.setX(this.ego.getX()) ;
      this.ball.setY(this.ego.getY()-20) ;
      Integer lives = (Integer)this.getGlobalFlag("lives") ;
      this.setGlobalFlag("lives", lives-1) ;
      this.lives.setText("Lives: "+this.getGlobalFlag("lives").toString()) ;
    }
    super.applyGameLogic() ;
  }
  
  
  
  
  
  
  

}


