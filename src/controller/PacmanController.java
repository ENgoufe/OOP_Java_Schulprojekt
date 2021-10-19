package controller;





import java.awt.Color;
import java.awt.event.KeyEvent;
import playground.*;
import controller.*;
import gameobjects.*;
import java.util.*;
import java.awt.event.*;


public class PacmanController extends Simple4WayController  {
  int direction = 0 ;
  boolean mouthOpen = true ;
  double lastStep = 0.0 ;
  double savex, savey, savevx, savevy ;
  String state = "stop" ;

  public PacmanController() {
    super();

  }


  void clipEgoToGrid(int x, int y) {
    this.setX(PacmanLevel.STARTX + x*PacmanLevel.SPACE) ;
    this.setY(PacmanLevel.STARTY + y*PacmanLevel.SPACE) ;
  }


  double getEgoGridPosX(double x) {
    return (x-PacmanLevel.STARTX) / PacmanLevel.SPACE ;
  }

  double getEgoGridPosY(double y) {
    return (y-PacmanLevel.STARTY) / PacmanLevel.SPACE ;
  }


  int getCurrentTileType() {
    int x = (int)(getEgoGridPosX(this.getX())) ;
    int y = (int)(getEgoGridPosY(this.getY())) ;
    return PacmanLevel.levelMap[y][x] ;
  }


  public boolean movementPossible(int dx, int dy) {
    double nextposX = getEgoGridPosX(getX()+ dx*this.getTimestep() * PacmanLevel.EGOSPEED) ;
    double nextposY = getEgoGridPosY(getY()+ dy*this.getTimestep() * PacmanLevel.EGOSPEED) ;
    int intposX = (int) ( nextposX+ 0.5) ; // rounded
    int intposY = (int) ( nextposY + 0.5) ; // rounded
    double fracposX = nextposX - intposX ;
    double fracposY = nextposY - intposY ;
    double fracPos1 = 0; double fracPos2 = 0; double delta=0 ;
    if (dx != 0) { fracPos1 = fracposX ; fracPos2 = fracposY; delta = dx ; }
    if (dy != 0) { fracPos1 = fracposY ; fracPos2 = fracposX; delta = dy ; }

    int mapEntry = PacmanLevel.levelMap[intposY+dy][intposX+dx] ;

    if (mapEntry == 1) {
      //System.out.println(fracPos2);
      return (fracPos1*delta <= 0 ) && (Math.abs(fracPos2) < 0.1) ;
    } 
    return  true  ;
  }


  @Override
  public void executeMovement(int reqState) {
    this.ego = this.gameObject ;

    if (reqState == -1) {
      ego.setVY(0.0);
      ego.setVX(0.0);
      this.heading = -1 ;
      return ;
    }

    if (this.movementPossible(dxs[reqState], dys[reqState])) {
    
      ego.setVX(dxs[reqState]*PacmanLevel.EGOSPEED);
      ego.setVY(dys[reqState]*PacmanLevel.EGOSPEED);

      // if we change direction, clip pacman to integer grid! necessary because movementPossible allows a movement 
      // if pacman is within 10% of a cell width of the center of the cell
      if (heading != reqState) {
        clip2GridFraction(1) ;
      }
    
      this.heading = reqState ;
    } else {
      if (this.heading == reqState) {
        ego.setVY(0.0);
        ego.setVX(0.0);
        this.heading = -1 ;
      }
    }
  }


  void clip2GridFraction(double CLIP) {
    this.ego = this.gameObject ;
    double posX = Math.floor (getEgoGridPosX(ego.getX())*CLIP+0.5)/CLIP ;
    double posY = Math.floor(getEgoGridPosY(ego.getY())*CLIP+0.5)/CLIP ;
    ego.setX(PacmanLevel.STARTX + PacmanLevel.SPACE*posX) ;
    ego.setY(PacmanLevel.STARTY + PacmanLevel.SPACE*posY) ;
    
  }


  public void updateObject() {
    PacmanObject po = (PacmanObject)(this.gameObject) ;
    double gt = this.getPlayground().getGameTime(); 
    if ((gt - this.lastStep) > 0.1) {
      this.lastStep = gt ;
      this.mouthOpen = !this.mouthOpen ;
      po.setMouthState(mouthOpen) ;
    }
    super.updateObject() ;

    // set heading dir of Pacman
    if (this.heading  != -1) {
      int[] headings = {0,180,90,270 } ;
      po.setDirection(headings[this.heading]) ;
    }

    // transport Pacman when he leaves on the left/right
    if (this.getCurrentTileType() == 5) {  // left transporter
      this.clipEgoToGrid(19,13)  ;
    } else 
    if (this.getCurrentTileType() == 6) {  // left transporter
      this.clipEgoToGrid(2,13)  ;
    } 


  }

}
