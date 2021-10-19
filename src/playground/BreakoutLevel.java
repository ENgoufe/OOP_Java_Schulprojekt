package playground;

import gameobjects.* ;
import java.util.* ;
import rendering.* ;
import java.util.LinkedList ;

import java.awt.Graphics2D;
import java.awt.Color ;
import controller.* ;
import collider.* ;

public abstract class BreakoutLevel extends Playground {
  TextObject counter = null ;
  TextObject lives = null ;

  
  protected GameObject ball = null, ego = null ;
  //ArrayList<GameObject> bricks = new ArrayList<GameObject>() ;

  public BreakoutLevel() {
    super() ;
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
  }   
  
  /** signalisiert der GameEngine des Ende des Spiels. Hier erstmal immer false.
   * @return immer false
   */
  public boolean gameOver() {
    Boolean go = (Boolean)getOrCreateGlobalFlag("gameOver", false) ;
    return go ;
  }


  public String getName() {
    return "Level1" ;
  }

  
  
  /** signalisiert der GameEngine des Ende des Levels. Hier erstmal immer false.
   * @return immer false
   */
  public boolean levelFinished() {
    Boolean go = (Boolean) getOrCreateLevelFlag("levelOver", false) ;
    return go ;
  }
  
  
  /** signalisiert der GameEngine den Restart des Levels. Hier erstmal immer false.
   * @return immer false
   */
  public boolean resetRequested() {
    return false ;
  }
  
  /** Dummy!
   * 
   */
  public void redrawLevel(Graphics2D g) {
    
  }
  
  
  
  /** Signal that the level has a size of 700x700 pixels.
   * @return x size of level  
   */
  @Override
  public int preferredSizeX() {
    return 700;
  }

  /** Signal that the level has a size of 700x700 pixels.
   * @return y size of level  
   */
  @Override
  public int preferredSizeY() {
    return 700;
  }
  
  
  /** Method that gets called by applyGameLogic() of the superclass whenever the ball collides with a brick.
   * @param ball A referene to the urrentz ball object
   * @param brick A reference to the ego object
   */
  void actionIfBallHitsBrick(GameObject ball, GameObject brick) {
    this.deleteObject(brick.getId());
    ball.setVY(ball.getVY()*-1);
  }
  
  
  /** Method that gets called by applyGameLogic() of the superclass whenever the ball collides with the ego object.
   * @param ball A referene to the urrentz ball object
   * @param ego A reference to the ego object
   */  
  void actionIfBallHitsEgo(GameObject ball, GameObject ego) {
    ball.setVY(ball.getVY()*-1);

  }

  /** Models interactions between GameObjects. 
   * notably ball/ego and ball/brick.
   */
  @Override
  public void applyGameLogic() {

    // check for collision ball/brick
    LinkedList<GameObject> bricks = collectObjects("brick", false) ;
    
    for (GameObject brick : bricks) {
      if (this.ball.collisionDetection(brick)) {
        this.actionIfBallHitsBrick(this.ball, brick) ;
      }
    }
    
    // check for collisions ball/ego
    if (ego.collisionDetection(ball)) {
      actionIfBallHitsEgo(this.ball, this.ego) ;
    }    

    // check for gameOver condition
    Integer lives = (Integer)this.getGlobalFlag("lives") ;
    if (lives < 0) {
      setGlobalFlag("gameOver", true) ;
    }
  }
  
  
  /** Controls the width of created bricks in Pixels. Is used by #createBrick.
   * 
   * @return bricks size X in pixels
   */
  public abstract double getBrickSizeX()  ;
  
  /** Controls the height of created bricks in Pixels. Is used by #createBrick.
   * 
   * @return bricks size Y in pixels
   */
  public abstract double getBrickSizeY() ;

  
  /** Controls the x coordinate of the upper left point of the grid of bricks, in Pixels. Is used by #createBrick.
   * 
   * @return bricks start X in pixels
   */
  public abstract double getBrickStartX() ;

  
  /** Controls the y coordinate of the upper left point of the grid of bricks, in Pixels. Is used by #createBrick.
   * 
   * @return bricks start Y in pixels
   */
  public abstract double getBrickStartY() ;
  
  
  /** Controls the nr of bricks in the X direction.  Is used by #createBrick.
   * 
   * @return nr of bricks in X direction
   */
  public abstract int calcNrBricksX() ;

  
  /** Controls the nr of bricks in the Y direction.  Is used by #createBrick.
   * 
   * @return nr of bricks in Y direction
   */
  public abstract int calcNrBricksY() ;
  
  
  /** Creates the ego object and returns it, called by #prepareLevel. Does NOT add the ego object to the playground!
   *  
   * @return The created ego object instance (of class EgoObject)
   */
  public GameObject createEgoObject() {
    System.out.println("prepare");
    RectObject ro = new RectObject("ego", this, 350, 550, 0, 0, 60,10, Color.BLUE) ;
    ro.generateColliders() ;
    Simple4WayController ec = new Simple4WayController() ;
    ro.setController(ec) ;
    return ro ;
  }
  
  /** Creates the ball object and returns it, called by #prepareLevel. Does NOT add the ball object to the playground!
   *  
   * @return The created ball object instance (of class FallingStar)
   */
  public GameObject createBall() {
    GameObject co = new FallingStar("ball1", this, 300,300, 270, 270, Color.RED, 5) ;
    co.setController(new ReboundController())   ;
    return co ;
  }
  
  /** Creates the GameObject (RectObject) instance representing a single brick at a certain grid position. The brick is NOT added here! 
   * @param row row position in the grid, ranges from 0 to calcNrBricksY()-1
   * @param column column position in the grid of bricks, ranges from 0 to calcNrBricksX()-1
   * @return The GameObject instance (really a RectObject) representing the created brick.
   */
  public abstract GameObject createBrick(int row, int column) ;   
    
  /** Prepares a generic Breakout-Type level. This method relies on the methods #createEgo, #createBall and #createBrick, among others, which
   * are meant to be overwritten in subclasses.
   * @param level String passes by the game engine, is in principle obsolete and can be ignored  
   */
  @Override
  public void prepareLevel(String level) {
    
    for (int y = 0; y < this.calcNrBricksY(); y++ ) {
      for (int x = 0; x < this.calcNrBricksX(); x++ ) {
        GameObject tmp = this.createBrick(x,y) ;
        this.addObject(tmp) ;      
      }
    }
    this.ego = this.createEgoObject() ;
    this.ball = this.createBall() ;
    this.addObject(this.ego) ;
    this.addObject(this.ball) ;
    
    // set up global flags
    this.setGlobalFlag("points",Integer.valueOf(0)) ;
    this.setGlobalFlag("lives",Integer.valueOf(3)) ;
    this.counter = new TextObject("counter", this, 600,00,0,0,"0", 20, Color.RED) ;
    this.addObject(this.counter) ;
    this.lives = new TextObject("lives", this, 600,550,0,0,"Lives: 3", 20, Color.RED) ;
    this.addObject(this.lives) ;

    // create and connect LevelController
    LevelController ctrl = new LevelController();
    this.setLevelController(ctrl) ;

  }  
  



}
