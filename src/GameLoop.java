import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.awt.event.*;
import gameobjects.GameObject;
import playground.Level1;
import playground.Level2;
import playground.Level3;
import playground.Level4;
import playground.BossLevel;
import playground.HitTwiceLevel;
import playground.Playground;
import playground.SpaceInvadersLevel;
import playground.HighscoreManager;
import ui.GameUI;
import ui.Menu;

/**
 * Main class starting any game, contains main(). Apart from that, this class manages all
 * non-logical functionalities which should be hidden from a game designer like:
 * <ul>
 * <li>Setting up windows, panels, buttons, action callbacks, ...
 * <li>Reading keyboard inputs
 * <li>Redrawing game window if necessary
 * <li>managing the game time and calling the appropriate {@link Playground} methods periodically,
 * at every time step of the game.
 * </ul>
 *
 * If you want to create a new game, inherit from this class and overwrite the {@link defineLevels}
 * method, storing instances to all levels of your game there.
 */

public abstract class GameLoop {

  public static int SIZEX = 200;
  public static int SIZEY = 200;
  // protected ArrayList<Playground> levels = new ArrayList<Playground>();

  /** contains the levels of the game. Set by {@link defineLevels} */
  protected Playground[] levels = null;

  /**
   * defines the levels of the game, overwrite if you want to define own levels. THese must be
   * subclasses of {@link Playground}.
   */
  //public abstract void defineLevels() ;  

  public abstract Playground nextLevel(Playground currentLevel) ;

  /**
   * runs the game. Normally never overwritten.
   * 
   * @param args The argument list from the main() git@gitlab.informatik.hs-fulda.de:fdai0114/javashooterws19.git method
   */
  public void runGame(String[] args) {

    GameUI gameUI = new GameUI(SIZEX, SIZEY);

    double gameTime = -1;
    Playground currentPlayground = null;

    //this.defineLevels();

    // loop over different levels
    int levelIndex = 0;
    //while (levelIndex < levels.length) {
    while (true) {

      gameTime = 0;

      // loop over single level
      while (true) {

        int act = gameUI.getNewAction();

        // if GameUI.newAction is not declared as volatile --> wprintln is required
        // System.out.println(act);

        // Query GameUI for high-level user commands; new game/reset/etc...
        if (act == GameUI.ACTION_RESET) {
          // Spiel neu starten
          System.out.println("RESET");
          //currentPlayground = this.levels[levelIndex];
          currentPlayground.prepareLevel("level");
          gameUI.resetAction();
        }

        if (act == GameUI.ACTION_NEW) {
          // new game
          levelIndex = 0;
          //currentPlayground = levels[levelIndex];
          currentPlayground = this.nextLevel(null) ;
          currentPlayground.prepareLevel("level");
          gameUI.setPlayground(currentPlayground);
          gameUI.setSize(currentPlayground.preferredSizeX(), currentPlayground.preferredSizeY());
          gameUI.resetAction();
          break;
        }

        if (act == GameUI.ACTION_BUTTON) {
          // Event wenn Button gedrückt wurde --> pausieren!
          if (currentPlayground != null) {
            boolean p = currentPlayground.isPaused();
            p = !p;
            currentPlayground.setPaused(p);
          }
          gameUI.resetAction();
        }

        if (act == GameUI.ACTION_SAVE) {
          // speichere Spielzustand
          gameUI.resetAction();
        }

        if (act == GameUI.ACTION_LOAD) {
          // lade Spielzustand
          gameUI.resetAction();
        }

        // if game has been created: execute a single iteration of the game loop
        if (currentPlayground != null) {

          if (currentPlayground.levelFinished() || currentPlayground.gameOver() == true) {
            break; // leave level
          }

          // paint current state of level and start time measurement
          long strt = System.nanoTime();
          gameUI.waitWhilePainting();
          // calc time that was used for painting the game, in ms
          long nd = System.nanoTime();
          double realTS = ((double) (nd - strt) / 1000000000.);

          gameUI.grabFocus();

          // communicate inputs to level
          //System.out.println(gameUI.getKeyEvents()) ;
          currentPlayground.processKeyEvents(gameUI.getKeyEvents());
          currentPlayground.processMouseEvents(gameUI.getMouseEvents());
          //System.out.println(gameUI.getKeyEvents().size());

          if (currentPlayground.isPaused() == false) {
            // update game time
            gameTime += realTS;

            // communicate gameTime and timestep to level
            currentPlayground.setTimestep(realTS);
            currentPlayground.setGameTime(gameTime);
            Playground.setGlobalFlag("gameTime", Double.valueOf(realTS));

            // update objects and level
            currentPlayground.updateObjects();
            currentPlayground.applyGameLogic();
          } // if
        } // if

      } // inner while loop within level

      // after level is done: leave outer loop if game over
      if (currentPlayground.gameOver() == true) {
        break;
      }

      // after level is done: reset level and go to next, if there is one
      if (currentPlayground.levelFinished() == true) {

        // if no next level --> exit
        currentPlayground = this.nextLevel(currentPlayground) ;
        if (currentPlayground == null) {
          break ;
        }
        currentPlayground.prepareLevel("level" + levelIndex);
      }


    } // outer loop over levels
    System.exit(0);
  } // main()


}
