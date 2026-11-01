import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class FallingObject {
  protected int x;
  protected int y;
  protected int size;
  protected int speed;

  private final int value = 10;

  
  public FallingObject(int x, int y, int size, int speed) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.speed = speed;
  }

  public void update() {
    y += speed;
  }

  public boolean isOffScreen(int screenHeight) {
    return y > screenHeight;
  }

  public void resetPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Rectangle getBounds() {
    return new Rectangle(x, y, size, size);
  }

  public int getValue() {
    return value;
  }


  public abstract void draw(Graphics g);
}