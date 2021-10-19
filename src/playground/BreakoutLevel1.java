package playground;

import java.awt.Color;
import controller.EgoController;
import controller.*;
import controller.ReboundController;
import collider.RectCollider;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;

/* colored bricks
 * rebound depends on position where ball hits ego object
 * rebound depends on where ball hits brick
 * spwan bonus object per Level, randomly with a certain provbability
 * 
 */

public class BreakoutLevel1 extends BreakoutLevel {
  

  public String getName() {
    return "BreakoutLevel2" ;
  }

  
  
  @Override
  void actionIfBallHitsBrick(GameObject ball, GameObject brick) {
    
    double ballX = ball.getX() ;
    double ballY = ball.getY() ;
    double brickX = brick.getX() ;
    double brickY = brick.getY() ;

    ball.setVY(ball.getVY()*-1);      
    
    this.deleteObject(brick.getId());
    for (int i = 0 ; i < 20; i++) {
      this.addObject(new FallingStar("gt"+this.getGameTime()+"_redexpl"+i, this, brickX, brickY, Math.random()*180, 
                     Math.random()*100., Color.RED, 3 ).setController(new LimitedTimeController(this.getGameTime(), 3.0))); 
      this.addObject(new FallingStar("gt"+this.getGameTime()+"_blueexpl"+i, this, brickX, brickY, Math.random()*180, 
          Math.random()*100., Color.BLUE, 3 ).setController(new LimitedTimeController(this.getGameTime(), 3.0))); 
    }
    
  } 
  
  @Override
  void actionIfBallHitsEgo(GameObject ball, GameObject ego) {
    double ballX = ball.getX() ;
    double ballY = ball.getY() ;
    double egoX = ego.getX() ;
    double egoY = ego.getY() ;            
    
    if (((ballX - egoX) < -0.9 * 60./2) && (ball.getVX() > 0)) {
      ball.setVX(ball.getVX()*-1);
      
    }
    
    if (((ballX - egoX) > 0.9 * 60./2) && (ball.getVX() < 0)) {
      ball.setVX(ball.getVX()*-1);
      
    }    
    
    ball.setVY(ball.getVY()*-1);
    
  }
  
  
  public  double getBrickStartX() {
    return 20 ;
  }
  
  
  public  double getBrickStartY() {
    return 20 ;
  }
  
  
  public  double getBrickSizeX() {
    return 60 ;
  }
  
  
  public  double getBrickSizeY() {
    return 30 ;
  }
  
  
  

  public int calcNrBricksX() {
    return 6 ;
  }

  
  public int calcNrBricksY() {
    return 5 ;
  }  
 
  @Override 
  public GameObject createBrick(int row, int column) {
    double xSize = getBrickSizeX() ;
    double ySize = getBrickSizeY() ;
    double xStart = getBrickStartX() ;
    double yStart = getBrickStartY() ;
    
    double random = Math.random() ;
    Color c = null ;
    if (random < 0.25) {
      c = Color.green ;
    } else if (random < 0.5) {
      c = Color.red; 
    } else if (random < 0.75) {
      c = Color.BLUE ;
    } else {
      c = Color.YELLOW;
    }    
    
    RectObject ro = new RectObject("brick"+row+"/"+column, this, xStart + 2*column * xSize, 
        yStart + 2* row * ySize, 0, 0, xSize-4, ySize-4, c) ;
    RectCollider rc = new RectCollider("egal", ro, xSize-4, ySize-4) ;
    ro.addCollider(rc) ;
    
    return ro ;
  }
  

  @Override
  public GameObject createEgoObject() {
    return super.createEgoObject().setController((new BreakoutController())) ;
  }
  
  
  
  

}




