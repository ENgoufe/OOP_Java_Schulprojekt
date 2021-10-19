package playground;

// import utilities.* ;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import javax.imageio.ImageIO;
import collider.CircleCollider;
import collider.Collider;
import collider.RectCollider;
import controller.EnemyController;
import controller.GhostController;
import controller.FallingStarController;
import controller.LimitedTimeController;
import controller.MineController;
import controller.ObjectController;
import controller.SimpleShotController;
import controller.PacmanController;
import controller.Simple4WayController;
import controller.LevelController;
import controller.CollisionAwareEgoController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;
import gameobjects.EgoObject;
import gameobjects.TextObject;
import gameobjects.PacmanObject;
import java.util.Scanner;



/**
 * Class that realizes all the game logic of a SpaceInvaders-type level. This is the class that you
 * inerit from if you want to create your own SpaceInvaders-tpe levels. Functions performed by this
 * class are:
 * <ul>
 * <li>initially set up the level, spawn all object etc., in method {@link #prepareLevel}
 * <li>define a game state machine, replay initial message and detect collisions in method
 * {@link #prepareLevel}
 * <li>create a lot of utility functions to redefine if you want to change a certain aspect of the
 * level
 * </ul>
 */
public class PacmanLevel extends Playground {
  public static final int POINTSX = 10 ;
  public static final int POINTSY = 10 ;
  public static final int STARTX = 50 ;
  public static final int STARTY = 50 ;
  public static final int SPACE = 20 ;
  public static final int BLOCKSIZE = 10 ;
  public static final int POINTSIZE = 4 ;
  public static final double PACSIZE = SPACE/2. - BLOCKSIZE/2. + SPACE/2. -1 ;
  public static final double GHOST_EAT_DURATION = 10 ;


  public static final double ENEMYSPEEDX = 80;
  public static final double ENEMYSPEEDY = 120;
  public static final double ENEMYSCALE = 0.3;
  public static final double STARTPERIOD = 5.;
  public static final double DYING_INTERVAL = 2.0;
  public static final int CANVASX = 700;
  public static final int CANVASY = 700;
  public static final double SHOTSPEED = 350;
  public static final double EGOSPEED = 200;
  public static final double EGORAD = 15;
  public static final double LEVEL_INIT_TIME = 1.0;
  public static final String EGO = "Julian" ;


  protected boolean lost = false;
  protected boolean doneLevel = false;
  protected double startzeit = 0;
  public File smash = null;
  public static File laser = null;
  int wallCount = 0 ;
  protected int points = 0 ;


  protected int animCycle = 0 ;
  protected Animation [] enemyAnims = {null, null, null, null };
  String [] animNames = {"./video/mkrake.txt", "./video/mkrake.txt", "./video/mkrake.txt" , "./video/mkrake.txt"} ; 
  GameObject ego = null ;

  /** 1 = wall, 2= pill, 0 = point, 3=empty */
  public static int [][] levelMap = 
  {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
   {1,2,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,2,1}, 
   {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1}, 
   {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1}, 
   {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1}, 
   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
   {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,0,1}, 
   {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,0,1}, 
   {1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1}, 
   {1,1,1,1,1,0,1,1,1,3,1,3,1,1,1,0,1,1,1,1,1}, 
   {3,3,3,3,1,0,1,3,3,3,3,3,3,3,1,0,1,3,3,3,3}, 
   {3,3,3,3,1,0,1,3,1,3,3,3,1,3,1,0,1,3,3,3,0}, 
   {1,1,1,1,1,0,1,3,1,3,3,3,1,3,1,0,1,1,1,1,1}, 
   {3,5,3,3,3,0,0,3,1,3,3,3,1,3,0,0,3,3,3,6,3}, 
   {1,1,1,1,1,0,1,3,1,1,1,1,1,3,1,0,1,1,1,1,1}, 
   {3,3,3,3,1,0,1,3,3,3,3,3,3,3,1,0,1,3,3,3,3}, 
   {3,3,3,3,1,0,1,3,1,1,1,1,1,3,1,0,1,3,3,3,3}, 
   {1,1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1,1}, 
   {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1}, 
   {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1}, 
   {1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1}, 
   {1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,1,1}, 
   {1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,1,1}, 
   {1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1}, 
   {1,0,1,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1}, 
   {1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1}, 
   {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}} ; 


  public PacmanLevel() {
    super();
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
  }

  public String getName() {
    return "PacmanLevel"; 
  }

  public int preferredSizeX() {
    return CANVASX;
  }

  public int preferredSizeY() {
    return CANVASY;
  }


  public boolean resetRequested() {
    return false;
  }


  protected double calcEnemySpeedX() {
    return ENEMYSPEEDX;
  }


  protected double calcEnemySpeedY() {
    return ENEMYSPEEDY;
  }


  ObjectController createEnemyController() {
    return new EnemyController();
  }


  protected String getStartupMessage() {
    String levelName = (String) (getGlobalFlag("user_choice") ) ;
    return levelName+"!!!" ;
  }


  public void enemy(String name, double x, double y) {
    x  = STARTX + x*SPACE ;
    y  = STARTY + y*SPACE ;
    RectObject ro = null ;
    if (name.equals("Binky")) {
      ro = new RectObject("enemy_"+name, this, x,y,0,0, 15,15, Color.RED ) ;
    } else
    if (name.equals("Pinky")) {
      ro = new RectObject("enemy_"+name, this, x,y,0,0, 15,15, Color.BLUE ) ;
    } else
    if (name.equals("Clyde")) {
      ro = new RectObject("enemy_"+name, this, x,y,0,0, 15,15, Color.YELLOW ) ;
    } else
    if (name.equals("Inky")) {
      ro = new RectObject("enemy_"+name, this, x,y,0,0, 15,15, Color.GREEN ) ;
    } 

    ObjectController oc = new GhostController(name) ;
    ro.setController(oc).generateColliders() ;
    ro.setVX(0) ;
    ro.setVY(-EGOSPEED/2) ;
    addObject(ro) ;
    

  }



  protected void createEnemies() {
    // create enemies
    enemy("Binky", 10,13) ;
    enemy("Pinky", 11,13) ;
    enemy("Clyde", 10,13) ;
    enemy("Inky", 11, 13) ;
  }


  protected void  point(int x,int y) {
        GameObject onePoint = new RectObject("point"+x+"/"+y, this, STARTX+x*SPACE,STARTY+y*SPACE,0,0,5,5, Color.WHITE).generateColliders() ;
        addObject(onePoint) ;
        this.points ++ ;
  }

  protected void  pill(int x,int y) {
        GameObject onePill = new RectObject("pill"+x+"/"+y, this, STARTX+x*SPACE,STARTY+y*SPACE,0,0,7,7, Color.CYAN).generateColliders() ;
        addObject(onePill) ;
  }



  protected void block(int x,int y) {
    GameObject leftW = new RectObject("obstacle"+ (++this.wallCount), this, STARTX+SPACE*x, STARTY+SPACE*y, 0, 0, BLOCKSIZE, BLOCKSIZE, Color.WHITE).generateColliders() ;
    addObject(leftW);
  }


  // 0 to 27 included vertical
  protected void createLabyrinth() {

    for (int y = 0; y < this.levelMap.length; y++) {
      for (int x = 0; x < this.levelMap[0].length; x++) {
        if (this.levelMap[y][x] == 1) {
          block(x,y) ;
        } else 
        if (this.levelMap[y][x] == 0) {
          point(x,y) ;
        } else 
        if (this.levelMap[y][x] == 2) {
          pill(x,y) ;
        } 

      }
    }
  }


  protected void createEgoObject() {
    // add ego to playground at lower bottom
    PacmanController egoController = new PacmanController();
    System.out.println("PACSIZE="+PACSIZE) ;

    this.ego = new PacmanObject("ego", this, STARTX+2*SPACE, STARTY+13*SPACE, 0,0, PACSIZE )
        .setController(egoController).generateColliders();
	

    //GameObject ego = new EgoObject("ego", this, 50, canvasY - 60, 0, 0, EGORAD)
    //    .setController(egoController).generateColliders();

    addObject(ego);
  }


  void actionIfEgoEatsPoint(GameObject point) {
    deleteObject(point.getId()) ;
      // add to points counter
      Integer pts = (Integer) getGlobalFlag("points");
      pts++;
      setGlobalFlag("points", pts);
  }



  void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
    double gameTime = this.getGameTime();
    this.setLevelFlag("eatGhostMode", true) ;
    this.setLevelFlag("eatGhostMode_t0", gameTime) ;
    deleteObject(collect.getId()) ;
  }

  void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {

    double gameTime = this.getGameTime();
    boolean eatGhostMode = (Boolean) getOrCreateLevelFlag("eatGhostMode", false) ;
    
    if (eatGhostMode == false) {
      Integer lives = (Integer) getGlobalFlag("lives");
      lives--;
      this.ego.setX(STARTX + 1*SPACE); 
      this.ego.setY(STARTY+25*SPACE) ;
      setGlobalFlag("lives", lives);
      if (lives  == 0) {
        this.lost = true;
      }
    } else {
      Integer points = (Integer) getGlobalFlag("points");
      points+=10;
      setGlobalFlag("points", points) ;
      enemy.setX(STARTX + 10*SPACE) ;
      enemy.setY(STARTY+13*SPACE) ;
      enemy.setVX(0) ;
      enemy.setVY(-1*EGOSPEED/2.) ;    
    }

  }


  public GameObject getEgo() {
    return this.ego ;
  }


  /**
   * initially sets up the level. Not called by user, but called every time a layer is restarted
   * from scratch. So make sure that this is possible. Here, resources are loaded only once even if
   * method is called several times.
   * 
   * @param id String identifies level.
   */
  @Override
  public void prepareLevel(String id) {
    System.out.println("PREPARE");
    reset();
    resetFlags(FLAGS_LEVEL);

    // create and connect LevelController
    LevelController ctrl = new LevelController();
    this.setLevelController(ctrl) ;

    // set up flags that some objects rely on
    getOrCreateGlobalFlag("points", Integer.valueOf(0));
    setLevelFlag("gameStatus", "start");
    setLevelFlag("detailedStatus", "std");
    getOrCreateGlobalFlag("lives", Integer.valueOf(3));
    setLevelFlag("dying", Double.valueOf(-1));

    // Zeitmessung starten
    this.startzeit = this.getGameTime();

  }


  /**
   * (re)draws the level but NOT the objects, they draw themselves. Called by the main game loop.
   * This method mainly draws the background and the scoreboard.
   * 
   * @param g2 Graphics2D object that can, and should be, draw on.
   */
  @Override
  public void redrawLevel(Graphics2D g2) {
    // Set anti-alias!
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    // Set anti-alias for text
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

    boolean eatGhostMode = (Boolean) getOrCreateLevelFlag("eatGhostMode", false) ;

    // fill background with black
    int[] x = {0, canvasX, canvasX, 0};
    int[] y = {0, 0, canvasY, canvasY};
    Polygon bg = new Polygon(x, y, 4);
    if (eatGhostMode == false) {
      g2.setColor(Color.BLACK);
    } else {
      g2.setColor(Color.BLUE);
    }
    g2.fill(bg);

    // draw score in upper left part of playground
    Integer pts = (Integer) getGlobalFlag("points");
    Font drawFont = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as = new AttributedString("Points: " + pts);
    as.addAttribute(TextAttribute.FONT, drawFont);
    as.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as.getIterator(), 10, 20);

    // draw lives counter in upper left part of playground
    Integer lives = (Integer) getGlobalFlag("lives");
    Font drawFont2 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as2 = new AttributedString("Lives: " + lives);
    as2.addAttribute(TextAttribute.FONT, drawFont2);
    as2.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as2.getIterator(), canvasX - 100, 20);


    if (isPaused()) {
      Font drawFont4 = new Font("SansSerif", Font.PLAIN, 50);
      AttributedString as4 = new AttributedString("Das Spiel wurde pausiert.");
      as4.addAttribute(TextAttribute.FONT, drawFont4);
      as4.addAttribute(TextAttribute.FOREGROUND, Color.red);
      g2.drawString(as4.getIterator(), 30, 400);
    }

  }


  /** Adds ego object, labyrinth and points and displays startup message. Is called from applyGameLogic */
  protected void setupInitialState() {
    double gameTime = this.getGameTime();
    setLevelFlag("gameStatus", "starting");

    // set up ego object
    this.createEgoObject();
    this.createLabyrinth() ;
    this.createEnemies() ;

    // add text object to playground
    ObjectController ctrl = new LimitedTimeController(gameTime, 2.);
    GameObject readyText = new TextObject("ready?", this, getSizeX() / 2, getSizeY()/2, 0, 0,
        "Get Ready!", 50, Color.RED).setController(ctrl);
    addObject(readyText);

  }


  /**
   * applies the logic of the level: For now, this is just about deleting shots that are leaving the
   * screen
   */
  @Override
  public void applyGameLogic() {
    double gameTime = this.getGameTime();
    String gameStatus = (String) getLevelFlag("gameStatus");
    String subStatus = (String) getLevelFlag("detailedStatus");

    boolean eatGhostMode = (Boolean)this.getOrCreateLevelFlag("eatGhostMode", true) ;
    double t0 = (Double) getOrCreateLevelFlag("eatGhostMode_t0", -100.) ;
    if (eatGhostMode == true) {
      if ((gameTime-t0) > GHOST_EAT_DURATION) {
        setLevelFlag("eatGhostMode", false) ;
      }
    }
    


    if (gameStatus.equals("start") == true) {
      setupInitialState();

    } else if (gameStatus.equals("starting") == true) {

      if ((gameTime - startzeit) > 0) {
        setLevelFlag("gameStatus", "init");
      }

    } else if (gameStatus.equals("init") == true) {

      //this.createEnemies();
      //this.createCollectables();

      setLevelFlag("gameStatus", "playing");

    } else if (gameStatus.equals("playing") == true) {

      if (subStatus.equals("std")) {

        // check for collisions of enemy and shots, reuse shots list from before..
        LinkedList<GameObject> enemies = collectObjects("enemy", false);
        LinkedList<GameObject> points = collectObjects("point", false);
        LinkedList<GameObject> pills = collectObjects("pill", false);


        // loop over enemies to check for collisions or suchlike ...
        for (GameObject p : points) {
          // if ego collides with enemy..
          if (ego.collisionDetection(p)) {
            actionIfEgoEatsPoint(p);
          }

        }

        for (GameObject e : enemies) {
          // if ego collides with enemy..
          if (ego.collisionDetection(e)) {
            actionIfEgoCollidesWithEnemy(e,ego);
          }

        }

        for (GameObject p : pills) {
          // if ego collides with enemy..
          if (ego.collisionDetection(p)) {
            actionIfEgoCollidesWithCollect(p,ego);
          }

        }



      } // if substatus..

    } // if (gameStatus == "playing")


  }

  public boolean gameOver() {
    return lost;
  }

  public boolean levelFinished() {
    String gameStatus = (String) getLevelFlag("gameStatus");
    return ((Integer)(getGlobalFlag("points")) >= 102)  ;
  }



}
