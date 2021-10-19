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
import playground.*;
import ui.GameUI;
import ui.Menu;


/**
 * Klasse die ein Beispiel für ein komplettes Spiel darstellt. Erbt von GameLoop und überschreibt
 * die Methode defineLevels.
 */
class SpaceInvadersGame extends GameLoop {

  Playground currentLevel=null ;

  public Playground nextLevel(Playground currentLevel) {
    if (currentLevel == null) {
      return new Level2() ;
    } else
    if(currentLevel.getName().equals("Level2")) {     
      return new Level4()  ;
    } else
    if(currentLevel.getName().equals("Level4"))  {
      return null ;
    }
    return null ;
  }

  public static void main(String[] args) {
    SpaceInvadersGame game = new SpaceInvadersGame();
    game.runGame(args);
  }
}
