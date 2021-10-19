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
class BreakoutGame extends GameLoop {

  Playground currentLevel=null ;

  public Playground nextLevel(Playground currentLevel) {
    if (currentLevel == null) {
      return new BreakoutLevel1() ;
    } else
    if(currentLevel.getName().equals("Level1")) {     
      return new BreakoutLevel2()  ;
    } else
    if(currentLevel.getName().equals("Level2"))  {
      return null ;
    }
    return null ;
  }

  public static void main(String[] args) {
    BreakoutGame game = new BreakoutGame();
    game.runGame(args);
  }
}
