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
import controller.*;
import controller.CollisionAwareEgoController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;
import gameobjects.EgoObject;
import gameobjects.TextObject;
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
public abstract class SpaceInvadersLevel extends Playground {

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
  protected int nrEnemies = 0 ;



  protected boolean lost = false;
  protected boolean doneLevel = false;
  protected double startzeit = 0;
  public File smash = null;
  public static File laser = null;
  double lastShot = -1 ;

  public BufferedImage[] alienImage = null;
  public double[] alienshowTime = null;

  public BufferedImage[] heartImage = null;
  public double[] heartshowTime = null;

  protected Animation enemyAnim = null;
  protected Animation heartAnim = null;


  public SpaceInvadersLevel() {
    super();
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
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


  protected GameObject createEnemyShotObject(GameObject parentObject, String name,
      ObjectController limitedTimeController) {
    GameObject to =
        new TextObject(name, this, parentObject.getX(), parentObject.getY(), 0, ENEMYSHOTSPEED, "I",
            20, Color.YELLOW).generateColliders().setController(limitedTimeController);
    /*
     * // auch denkbar! GameObject to = new RectObject(name, this, parentObject.getX(),
     * parentObject.getY(), 0, ENEMYSHOTSPEED, 4, 20,
     * Color.YELLOW).addController(limitedTimeController) ; to.generateColliders();
     */
    return to;
  }


  protected void createEnemyShot(GameObject e) {
    double gameTime = this.getGameTime();

    double PROB = calcEnemyShotProb();
    double diceThrow = Math.random();
    Integer nrEnemyShots = (Integer) (getLevelFlag("enemyShotCounter"));
    if (diceThrow < PROB) {
      setLevelFlag("enemyShotCounter", Integer.valueOf(++nrEnemyShots));

      LimitedTimeController limitedTimeController = new LimitedTimeController(gameTime, 5.);

      GameObject textObject =
          createEnemyShotObject(e, "enmyShot" + nrEnemyShots, limitedTimeController);

      addObject(textObject);
    }
  }

  double calcEnemyShotProb() {
    return 0.1 * this.getTimestep();
  }

  protected double calcEnemySpeedX() {
    return ENEMYSPEEDX;
  }


  protected double calcEnemySpeedY() {
    return ENEMYSPEEDY;
  }

  protected int calcNrEnemies() {
    return NR_ENEMIES;
  }

  protected int calcNrCollect() {
    return NR_COLLECT;
  }


  ObjectController createEnemyController() {
    return new EnemyController();
  }


  protected String getStartupMessage() {
    return "SpaceInvadersLevel!!!";
  }


  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {

    GameObject tmp =
        new AnimatedGameobject(name, this, x_enemy, y_enemy, vx_enemy, vy_enemy, ENEMYSCALE,
            this.enemyAnim, gameTime, "loop").setController(enemyController).generateColliders();
    return tmp;
  }

  protected GameObject createSingleCollect(String name) {
    double gameTime = this.getGameTime();
    double cspeedy = 20.;
    double x_collect = Math.random() * this.canvasX;
    double y_collect = Math.random() * this.canvasY / 3;
    double vx_collect = 2 * (Math.random() - 0.5) * 0;
    double vy_collect = Math.random() * cspeedy;

    GameObject tmp = new AnimatedGameobject(name, this, x_collect, y_collect, vx_collect,
        vy_collect, 0.3, this.heartAnim, gameTime, "loop").generateColliders()
            .setController(new EnemyController());
    return tmp;
  }


  protected void createEnemies() {
    // create enemies
    double gameTime = this.getGameTime();
    double speedx = this.calcEnemySpeedX();
    double speedy = this.calcEnemySpeedY();
    for (int i = 0; i < this.calcNrEnemies(); i++) {
      double x_enemy = Math.random() * this.canvasX;
      double y_enemy = Math.random() * this.canvasY / 3;
      double vx_enemy = 2 * (Math.random() - 0.5) * speedx;
      double vy_enemy = Math.random() * speedy;

      ObjectController enemyController = createEnemyController();
      GameObject enemy = createSingleEnemy("enemy" + i, x_enemy, y_enemy, vx_enemy, vy_enemy,
          enemyController, gameTime);
      addObject(enemy);
    }
    this.nrEnemies = this.calcNrEnemies() ;
  }

  protected void createCollectables() {
    double gameTime = this.getGameTime();
    // create collectables
    for (int i = 0; i < this.calcNrCollect(); i++) {


      GameObject collect = createSingleCollect("collect" + i);

      addObject(collect);
    }
  }


  protected void createEgoObject() {
    // add ego to playground at lower bottom
    //EgoController egoController = new CollisionAwareEgoController(this.EGORAD);
    Simple4WayController egoController = new Simple4WayController();
    

    GameObject ego = new EgoObject("ego", this, 50, canvasY - 60, 0, 0, EGORAD)
        .setController(egoController).generateColliders();

    addObject(ego);
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


  void createExplosion(double gameTime, GameObject e, String basename, double interval,
      Color color) {
    // spawn a cloud of exploded shards
    for (int i = 0; i < NRSHARDS; i++) {
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVY();
      addObject(
          new FallingStar("shard" + gameTime + "/" + i, this, e.getX(), e.getY(), vx, vy, color, 2)
              .setController(new LimitedTimeController(gameTime, interval)));
    }
  }


  void actionIfEnemyIsHit(GameObject e, GameObject shot) {

    double gameTime = this.getGameTime();
    createExplosion(gameTime, e, "shard", DYING_INTERVAL, Color.RED);

    Music.music(smash);

    // delete enemy
    deleteObject(e.getId());

    // delete shot
    deleteObject(shot.getId());

    // add to points counter
    Integer pts = (Integer) getGlobalFlag("points");
    setGlobalFlag("points", pts + 200);

    // reduce global enemies counter
    this.nrEnemies-- ;

  }

  void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
    double gameTime = this.getGameTime();

    if (this.getObject("bonustext") == null) {

      // spawn a bonus points object
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVY();


      LimitedTimeController bonusTextController =
          new LimitedTimeController(gameTime, SpaceInvadersLevel.EXPL_DURATION);
      GameObject bonusText = new TextObject("bonustext", this, collect.getX(), collect.getY(), vx,
          vy, "Extra life!!", 20, Color.YELLOW).setController(bonusTextController);

      addObject(bonusText);

      // delete collect
      deleteObject(collect.getId());

      // add to points counter
      Integer lives = (Integer) getGlobalFlag("egoLives");
      lives++;
      setGlobalFlag("egoLives", lives);
    }

  }

  void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {

    /**
     * set temporary text over ego. This object lives only for a short time. While it lives, further
     * collisions are ignored. Otherwise a single collision would result in a lot of deducted
     * points...
     */

    double gameTime = this.getGameTime();
    GameObject auaObj = this.getObject("AUA-EGO");

    if (auaObj == null) {
      addObject(new TextObject("AUA-EGO", this, ego.getX(), ego.getY() - 20, ego.getVX(),
          ego.getVY(), "AUAA!!", 10, Color.RED)
              .setController(new LimitedTimeController(gameTime, BONUS_DURATION)));
      // deduct points
      Integer pts = (Integer) getGlobalFlag("points");
      setGlobalFlag("points", pts - 500);
    }

  }

  protected void actionIfEgoObjectIsHit(GameObject eshot, GameObject ego) {
    // System.out.println("collision of " + eshot.getId() + " and " + ego.getId());

    double gameTime = this.getGameTime();
    this.deleteObject(eshot.getId());

    Integer lives = (Integer) getGlobalFlag("egoLives");
    lives--;
    setGlobalFlag("egoLives", lives);

    if (lives <= 0) {
      lost = true;
      HighscoreManager hsm = new HighscoreManager();
      hsm.writeHSToFile((Integer) this.getGlobalFlag("points"),
          (Integer) this.getGlobalFlag("highscore"));

    }

    LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
    for (GameObject _eshot : eshots) {
      deleteObject(_eshot.getId());
    }
    setLevelFlag("detailedStatus", "dying");
    setLevelFlag("t0", gameTime);
    ego.setActive(false);
    createExplosion(gameTime, ego, "egoexp", DYING_INTERVAL, Color.WHITE);
    LinkedList<GameObject> enemies = collectObjects("enemy", false);
    for (GameObject enemy : enemies) {
      enemy.setY(0);
      enemy.setActive(false);
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
    setLevelFlag("delete", new LinkedList<String>());
    setLevelFlag("replace", new LinkedList<String>());
    getOrCreateGlobalFlag("points", Integer.valueOf(0));
    setLevelFlag("enemyShotCounter", Integer.valueOf(0));
    setLevelFlag("gameStatus", "start");
    setLevelFlag("detailedStatus", "std");
    getOrCreateGlobalFlag("egoLives", Integer.valueOf(5));
    setLevelFlag("dying", Double.valueOf(-1));

    // Zeitmessung starten
    this.startzeit = this.getGameTime();

    // Musik laden
    if (this.smash == null) {
      this.smash = new File("./audio/smash.wav");
    }
    if (this.laser == null) {
      this.laser = new File("./audio/laser.wav");
    }

    // ----- Alien
    if (this.enemyAnim == null) {
      String dateiName = "./video/sweetAlien.txt";
      this.enemyAnim = new Animation(dateiName);
    }

    // -----Heart
    if (this.heartAnim == null) {
      String heartName = "./video/heart.txt";
      this.heartAnim = new Animation(heartName);
    }

    // JW: Highscore aus Datei laden und aktualisieren
    HighscoreManager dh = new HighscoreManager();
    int alltimeHighscore = dh.readHSFromFile();
    setGlobalFlag("highscore", alltimeHighscore);
    System.out.println("HIGHSCORE" + alltimeHighscore);

    // create and connect LevelController
    LevelController ctrl = new LevelController();
    this.setLevelController(ctrl) ;

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
    g2.setColor(Color.BLACK);
    g2.fill(bg);

    // draw score in upper left part of playground
    Integer pts = (Integer) getGlobalFlag("points");
    Font drawFont = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as = new AttributedString("Points: " + pts);
    as.addAttribute(TextAttribute.FONT, drawFont);
    as.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as.getIterator(), 10, 20);

    // draw lives counter in upper left part of playground
    Integer lives = (Integer) getGlobalFlag("egoLives");
    Font drawFont2 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as2 = new AttributedString("Lives: " + lives);
    as2.addAttribute(TextAttribute.FONT, drawFont2);
    as2.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as2.getIterator(), canvasX - 100, 20);

    // ergaenzt durch JW, draw highscore in left part of playground under score
    Integer highscore = (Integer) getGlobalFlag("highscore");
    Font drawFont3 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as3 = new AttributedString("Highscore: " + highscore);
    as3.addAttribute(TextAttribute.FONT, drawFont3);
    as3.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as3.getIterator(), 10, 40);

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
    setLevelFlag("gameStatus", "starting");

    this.createStars();

    // set up ego object
    this.createEgoObject();

    // add text object to playground
    ObjectController ctrl = new LimitedTimeController(gameTime, 3.);
    GameObject readyText = new TextObject("ready?", this, getSizeX() / 2, 0, 0, 100,
        this.getStartupMessage(), 50, Color.RED).setController(ctrl);
    addObject(readyText);

  }

  /** produce a shot if space pressed */
  public void shoot(GameObject ego) {
    Object flag = getLevelFlag("shoot_pressed") ;
    if (flag != null) {
  if ((Boolean)flag == true) {
    if (this.getGameTime() - this.lastShot > 0.1) {
    Integer nextShot =
        (Integer) getOrCreateLevelFlag("nextShot", Integer.valueOf(0));
    String shotName = "simpleShot" + nextShot++;
    setLevelFlag("nextShot", nextShot);

    SimpleShotController simpleshot = new SimpleShotController();
    GameObject ss = new RectObject(shotName, this, ego.getX(), ego.getY(), 0,
        -1. * SpaceInvadersLevel.SHOTSPEED, 4, 12, Color.CYAN).setController(simpleshot);
    ss.generateColliders();
    addObject(ss);
    this.lastShot = this.getGameTime() ;
    }
    }
    }
  }


  public void checkLevelCompletion() {
    if (this.nrEnemies == 0) {
      HighscoreManager hsm = new HighscoreManager();
      hsm.writeHSToFile((Integer) this.getGlobalFlag("points"),
          (Integer) this.getGlobalFlag("highscore"));
      this.doneLevel = true;
    }
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

    if (gameStatus.equals("start") == true) {
      setupInitialState();

    } else if (gameStatus.equals("starting") == true) {

      if ((gameTime - startzeit) > LEVEL_INIT_TIME) {
        setLevelFlag("gameStatus", "init");
      }

    } else if (gameStatus.equals("init") == true) {

      this.createEnemies();
      this.createCollectables();

      setLevelFlag("gameStatus", "playing");

    } else if (gameStatus.equals("playing") == true) {
      GameObject s = this.getObject("ego");
      // System.out.println("Loop");

      if (subStatus.equals("std")) {

        shoot(s) ;

        // check for collisions of enemy and shots, reuse shots list from before..
        LinkedList<GameObject> enemies = collectObjects("enemy", false);

        // check whether all enemies have been destroyed or escaped
        checkLevelCompletion() ;

        // loop over enemies to check for collisions or suchlike ...
        LinkedList<GameObject> shots = collectObjects("simpleShot", true);
        for (GameObject e : enemies) {
          // if ego collides with enemy..
          if (s.collisionDetection(e)) {
            actionIfEgoCollidesWithEnemy(e, s);
          }

          // if shot collides with enemy
          for (GameObject shot : shots) {
            if (e.collisionDetection(shot)) {
              actionIfEnemyIsHit(e, shot);
            }
          }
        }

        // Wenn Herzen einsammeln
        LinkedList<GameObject> collects = collectObjects("collect", false);
        for (GameObject c : collects) {
          if (s.collisionDetection(c)) {
            actionIfEgoCollidesWithCollect(c, s);
          }
        }

        // loop over enemies and, with a certain probability, launch an enemy shot for each one
        for (GameObject e : enemies) {
          createEnemyShot(e);
        }

        // check for collisions between ego object and enemy shots
        LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
        for (GameObject eshot : eshots) {

          if (s.collisionDetection(eshot)) {
            // System.out.println("COLLISION"+ eshot.scol.get(0)+"/"+ s.scol.get(0));
            actionIfEgoObjectIsHit(eshot, s);
          }
        }
      } // if substatus..
      else if (subStatus.equals("dying")) {
        Double t0 = (Double) getLevelFlag("t0");
        if (gameTime - t0 > DYING_INTERVAL) {
          LinkedList<GameObject> enemies = collectObjects("enemy", false);
          setLevelFlag("detailedStatus", "std");
          s.setActive(true);

          for (GameObject e : enemies) {
            // System.out.println("activating"+e.getId());
            e.setActive(true);
          }
        }
      }

    } // if (gameStatus == "playing")


  }

  public boolean gameOver() {
    return lost;
  }

  public boolean levelFinished() {
    return doneLevel;
  }



}
