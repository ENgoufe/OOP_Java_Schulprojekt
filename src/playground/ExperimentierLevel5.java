package playground;
import java.awt.Color;
import gameobjects.* ;
import controller.* ;
import rendering.* ;

public class ExperimentierLevel5 extends SpaceInvadersLevel {
  
  @Override
  protected String getStartupMessage() {
    return "ExperimentierLevel5!!!";
  }

  public String getName() {
    return "ExpLevel5" ;
  }

  
  @Override
  public void prepareLevel(String id) {
    super.prepareLevel(id);
    
    ObjectController ctrl1 = new ReboundController() ;    
    GameObject newObj1 = new RectObject("flyaround1", this, 300,300, 70,170, 20,10, Color.GREEN) ;
    newObj1.setController(ctrl1) ;
    this.addObject(newObj1) ;
    
    ObjectController ctrl2 = new ReboundController() ;
    GameObject newObj2 = new RectObject("flyaround2", this, 200,200, 170, 30, 30, 30, Color.BLUE) ;
    newObj2.setController(ctrl2) ;
    this.addObject(newObj2) ;    
  }
      

}
