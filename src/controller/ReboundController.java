package controller;

public class ReboundController extends ObjectController {

  public ReboundController() {
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public void updateObject() {
    if (this.getX() >= this.getPlayground().getSizeX()-20) {
      this.setVX(this.getVX()*-1) ;
    }
    
    if (this.getX() < 20) {
      this.setVX(this.getVX()*-1) ;
    }
    
    if (this.getY() >= this.getPlayground().getSizeY()-20) {
      this.setVY(this.getVY()*-1) ;
    }
    
    if (this.getY() < 20) {
      this.setVY(this.getVY()*-1) ;
    }
    
    
    // unverändert lassen, sogt dafür dass Objekt weiterbewegt wird!
    this.applySpeedVector() ;
  }

}
