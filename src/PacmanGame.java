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
class PacmanGame extends GameLoop {

  @Override
  public Playground nextLevel(Playground currentLevel) {
    if (currentLevel == null) {
      return new PacmanLevel() ;
    } else
    if(currentLevel.getName().equals("PacmanLevel")) {
     
      return null ;
    } 

    return null ;
  }

  public static void main(String[] args) {
    PacmanGame p2g = new PacmanGame();
    p2g.runGame(args);
  }
}
