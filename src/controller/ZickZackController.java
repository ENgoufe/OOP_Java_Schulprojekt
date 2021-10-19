package controller;

public class ZickZackController extends EnemyController {
  protected double g0;
  protected double dt;
  protected double lastMod = -1;

  public ZickZackController(double gameTime, double dt) {
    super();
    this.dt = dt;
    this.g0 = gameTime;
  }

  public void updateObject() {
    double gameTime = this.getPlayground().getGameTime();
    // System.out.println("toright!"+gameObject.getX());
    double mod = (gameTime - this.g0) % this.dt;
    if (mod < lastMod) {
      gameObject.setVX(-1.0 * gameObject.getVX());
    }
    lastMod = mod;

    super.updateObject();

  }
}
