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
import controller.FallingStarController;
import controller.LimitedTimeController;
import controller.MineController;
import controller.ObjectController;
import controller.SimpleShotController;
import controller.LevelController;
import controller.CollisionAwareEgoController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;
import gameobjects.EgoObject;
import gameobjects.TextObject;
import gameobjects.AnimatedGameobject;
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
public class EldradorSelectionLevel extends Playground {

  public static final int LEVEL2STARS = 80;
  public static final double BONUS_DURATION = 1.;
  public static final double ENEMYSPEEDX = 120;
  public static final double ENEMYSPEEDY = 80;
  public static final double ENEMYSCALE = 1.0;
  public static final double ENEMYSHOTSPEED = 150;
  public static final int NRSHARDS = 50;
  public static final double EXPL_DURATION = 1.;
  public static final int NR_ENEMIES = 30;
  public static final int NR_COLLECT = 5;
  public static final Color EXPL_COLOR = Color.RED;
  public static final double SHARDSPEED = 400;
  public static final double STARSPEED = 200;
  public static final double STARTTEXTSPEED = 150;
  public static final double STARTPERIOD = 5.;
  public static final double DYING_INTERVAL = 2.0;
  public static final int CANVASX = 700;
  public static final int CANVASY = 700;
  public static final double SHOTSPEED = 350;
  public static final double EGOSPEED = 400;
  public static final double EGORAD = 15;
  public static final double LEVEL_INIT_TIME = 1.0;

  protected int nextShot = 0;



  protected boolean lost = false;
  protected boolean doneLevel = false;
  protected double startzeit = 0;
  public File smash = null;
  public static File laser = null;

  public BufferedImage[] alienImage = null;
  public double[] alienshowTime = null;

  public BufferedImage[] heartImage = null;
  public double[] heartshowTime = null;

  protected Animation pfmonster = null;
  protected Animation kroko = null;
  protected Animation eisgreif = null;
  protected Animation stier = null;
  protected Animation jaguar = null;
  protected Animation mkrake = null;
  protected Animation lskorpion = null;


  public EldradorSelectionLevel() {
    super();
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
  }

  @Override
  public String getName() {
    return "EldradorSelectionLevel" ;
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


  protected void createStars() {
    // add stars to playground
    for (int i = 1; i <= LEVEL2STARS; i++) {
      FallingStarController fsc = new FallingStarController();

      GameObject star = new FallingStar("star" + i, this, Math.random() * canvasX,
          Math.random() * 15, 0.0, Math.random() * STARSPEED, Color.WHITE, 1.).setController(fsc);

      addObject(star);
    }
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

    // set up flags that some objects rely on
    getOrCreateGlobalFlag("user_choice", "");
    setLevelFlag("gameStatus", "initialized");
    setLevelFlag("detailedStatus", "std");

    // Zeitmessung starten
    this.startzeit = this.getGameTime();

    // ----- Alien
    if (this.pfmonster == null) {
      String dateiName = "./video/pfmonster.txt";
      this.pfmonster = new Animation(dateiName);
    }
    // ----- Alien
    if (this.kroko == null) {
      String dateiName = "./video/kroko.txt";
      this.kroko = new Animation(dateiName);
    }
    // ----- Alien
    if (this.stier == null) {
      String dateiName = "./video/stier.txt";
      this.stier = new Animation(dateiName);
    }
    // ----- Alien
    if (this.jaguar == null) {
      String dateiName = "./video/jaguar.txt";
      this.jaguar = new Animation(dateiName);
    }
    // ----- Alien
    if (this.eisgreif == null) {
      String dateiName = "./video/eisgreif.txt";
      this.eisgreif = new Animation(dateiName);
    }

    if (this.lskorpion == null) {
      String dateiName = "./video/lskorpion.txt";
      this.lskorpion = new Animation(dateiName);
    }

    if (this.mkrake == null) {
      String dateiName = "./video/mkrake.txt";
      this.mkrake = new Animation(dateiName);
    }
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

    // fill background with black
    int[] x = {0, canvasX, canvasX, 0};
    int[] y = {0, 0, canvasY, canvasY};
    Polygon bg = new Polygon(x, y, 4);
    g2.setColor(Color.BLUE);
    g2.fill(bg);

    if (isPaused()) {
      Font drawFont4 = new Font("SansSerif", Font.PLAIN, 50);
      AttributedString as4 = new AttributedString("Das Spiel wurde pausiert.");
      as4.addAttribute(TextAttribute.FONT, drawFont4);
      as4.addAttribute(TextAttribute.FOREGROUND, Color.red);
      g2.drawString(as4.getIterator(), 30, 400);
    }

  }

  /** Adds ego object and stars and displays startup message. Is called from applyGameLogic */
  protected void setupInitialState() {
    double gameTime = this.getGameTime();
    setLevelFlag("gameStatus", "startText");

    this.createStars();

  }


  protected void createChoices() {

    // add text object to playground
    String[] welten = {"Lava","Stein","Dschungel","Eis","Ozean"} ;
    for (int i = 0; i < welten.length; i++ ) {
      ObjectController ctrlWorld = new LimitedTimeController(gameTime, 3000.);
      GameObject weltText = new TextObject("option_"+welten[i], this, getSizeX()/2, (i) * getSizeY() / 5, 0, 0,
        welten[i], 50, Color.RED).setController(ctrlWorld).generateColliders();
      addObject(weltText);
    }

    GameObject ago = new AnimatedGameobject("croc", this, 150, 150, 0, 0, 0.6,
            this.kroko, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(ago) ;
    GameObject pfm = new AnimatedGameobject("pfm", this, 250, 350, 0, 0, 0.6,
            this.pfmonster, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(pfm) ;
    GameObject eg = new AnimatedGameobject("eisgreif", this, 250, 450, 0, 0, 0.6,
            this.eisgreif, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(eg) ;
    GameObject stier = new AnimatedGameobject("stier", this, 550, 550, 0, 0, 0.6,
            this.stier, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(stier) ;
    GameObject jag = new AnimatedGameobject("jaguar", this, 400, 600, 0, 0, 0.6,
            this.jaguar, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(jag) ;
    GameObject lskorpion = new AnimatedGameobject("lskorpion", this, 100, 600, 0, 0, 0.6,
            this.lskorpion, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(lskorpion) ;
    GameObject mkrake = new AnimatedGameobject("mkrake", this, 500, 200, 0, 0, 0.6,
            this.mkrake, gameTime, "loop").setController(new LimitedTimeController(gameTime, 30000)).generateColliders();
    addObject(mkrake) ;


    // add level controller
    LevelController ctrl = new LevelController();
      GameObject dummyObject = new TextObject("dummyControlObj", this, 0,0, 0, 0,
        ".", 50, Color.RED).setController(ctrl);
      addObject(dummyObject) ;
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

    if (gameStatus.equals("initialized") == true) {
      setupInitialState();

    } else if (gameStatus.equals("startText") == true) {

      if ((gameTime - startzeit) > LEVEL_INIT_TIME) {
        setLevelFlag("gameStatus", "initChoices");
      }

    } else if (gameStatus.equals("initChoices") == true) {
      createChoices() ;
      setLevelFlag("gameStatus", "playing");

    } else if (gameStatus.equals("playing") == true) {
      String uc = (String)getGlobalFlag("user_choice") ;
      if (!uc.equals("")) {
        this.doneLevel = true ;
      }
    } 


  }

  public boolean gameOver() {
    return lost;
  }

  public boolean levelFinished() {
    return doneLevel;
  }



}
