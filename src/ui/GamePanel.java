package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.JPanel;
import playground.Playground;
import java.awt.event.*;


/**
 * Models the area where the game is actually drawn, as a part the main window of the application.
 * Contains all of the Swing logic to draw the game, and to listen and register keyboard and mouse
 * inputs which are passed to the game logic.
 */
class GamePanel extends JPanel implements KeyListener, MouseListener {
  private static final long serialVersionUID = 1L;
  protected volatile boolean painting = false;
  private volatile Integer currentKey = null;
  private volatile Boolean releasedFlag = null;
  private Playground playground = null;
  private HashMap<Integer, Integer> keys = new HashMap<Integer, Integer>();
  private int sizeX, sizeY;
  Stack<KeyEvent> keyEvents = new Stack<KeyEvent>();
  Stack<MouseEvent> mouseEvents = new Stack<MouseEvent>();

  GamePanel(int sizeX, int sizeY) {
    super();
    setPreferredSize(new Dimension(sizeX, sizeY));
    addKeyListener(this);
    addMouseListener(this);
  }

  public void setSize(int sizeX, int sizeY) {
    setPreferredSize(new Dimension(sizeX, sizeY));
    this.sizeX = sizeX;
    this.sizeY = sizeY;
  }

  @Override
  public void repaint() {
    super.repaint();
  }

  public void setPainting() {
    painting = true;
  }


  public void waitWhilePainting() {
    while (painting) {
    }
  }


  Stack<KeyEvent> getKeyEvents() {
    return keyEvents;
  }


  Stack<MouseEvent> getMouseEvents() {
    return mouseEvents;
  }


  public void setPlayground(Playground pg) {
    this.playground = pg;
  }

  public boolean stillPainting() {
    return painting;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // System.out.println(playground) ;
    if (playground != null) {
      playground.redraw((Graphics2D) g);
    }
    painting = false;

  }


  public HashMap<Integer, Integer> getCurrentKey() {
    return keys;
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    /*
     * this.keys.put(e.getKeyCode(), 2); currentKey = e.getKeyCode();
     * //System.out.println("keyCode " + e.getKeyCode());
     */
    //System.out.println(e.paramString()) ;
    this.keyEvents.push(e);
  }

  @Override
  public void keyReleased(KeyEvent e) { /*
                                         * this.keys.put(e.getKeyCode(), 1); currentKey =
                                         * e.getKeyCode(); releasedFlag = true;
                                         */
    this.keyEvents.push(e);
    // System.out.println(e.paramString()) ;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.mouseEvents.push(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }


  @Override
  public void mousePressed(MouseEvent e) {
    this.mouseEvents.push(e) ;
  }



  @Override
  public void mouseEntered(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }
}
