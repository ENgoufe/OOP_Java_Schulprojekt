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
class EldradorGame extends GameLoop {

 
  Playground currentLevel=null ;

  public Playground nextLevel(Playground currentLevel) {
    if (currentLevel == null) {
      return new EldradorSelectionLevel() ;
    } else
    if(currentLevel.getName().equals("EldradorSelectionLevel")) {
      System.out.println("NEW: "+currentLevel.getGlobalFlag("user_choice")) ;
     
      return new EldradorGenericLevel() ;
    } else
    if(currentLevel.getName().equals("EldradorGenericLevel"))  {
      return null ;
    }
    return null ;
  }

  public static void main(String[] args) {
    EldradorGame p2g = new EldradorGame();
    p2g.runGame(args);
  }
}
