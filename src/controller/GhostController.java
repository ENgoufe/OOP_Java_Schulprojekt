package controller;

import playground.PacmanLevel;

/**
 * This class controls the ghosts
 */
public class GhostController extends ObjectController {

  protected String ghostName ;
  protected int heading = 2; // -1 = start, 0 1 2 3 = left right up down

  public GhostController(String ghostName) {
    super() ;
    this.ghostName = ghostName ;
  }

  double getGridPosX(double x) {
    return (x-PacmanLevel.STARTX) / PacmanLevel.SPACE ;
  }

  double getGridPosY(double y) {
    return (y-PacmanLevel.STARTY) / PacmanLevel.SPACE ;
  }



  @Override
  public void updateObject() {
    int dx = (int)(this.getVX() / Math.abs(this.getVX())) ;
    int dy = (int)(this.getVY() / Math.abs(this.getVY())) ;
    double curposX = getGridPosX(getX()) ;
    double curposY = getGridPosY(getY()) ;
    int intposX =  (int)(curposX+0.5) ;
    int intposY =  (int)(curposY+0.5) ;

    double fracposX = curposX - intposX ;
    double fracposY = curposY - intposY ;

    int mapEntry = PacmanLevel.levelMap[intposY+dy][intposX+dx] ;

    if ((mapEntry == 1)&&(Math.abs(fracposX) < 0.2) && (Math.abs(fracposY)<0.2)) {
      this.setX(PacmanLevel.STARTX + intposX*PacmanLevel.SPACE) ;
      this.setY(PacmanLevel.STARTY + intposY*PacmanLevel.SPACE) ;
      this.heading = (int)(Math.random() * 4.) ;
      int [] dxs = {1,-1,0,0} ;
      int [] dys = {0,0,-1,1} ;
      this.setVX(PacmanLevel.EGOSPEED/2 * dxs[heading]) ;
      this.setVY(PacmanLevel.EGOSPEED/2 * dys[heading]) ;

    }

    

    
    

    applySpeedVector();
  }
}
