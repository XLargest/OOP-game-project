import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private int x;
    private int y;

    private final int width = 80;
    private final int height = 120;
    private final int speed = 5;

    private int hearts = 3;

    private BufferedImage image;

    // Constructor: กำหนดตำแหน่งและโหลดภาพครั้งเดียว
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(
                    Player.class.getResource("/images/Miner_Idle1.png"));
        } catch (IOException e) {
            throw new IllegalStateException("loading /images/Miner_idle1.png fail", e);
        }
    }

    public void move(int direction, int areaWidth) {
        x += direction * speed;

        if (x < 0) {
            x = 0;
        }

        if (x + width > areaWidth) {
            x = areaWidth - width;
        }
    }

    public int getHearts() {
        return hearts;
    }

    public void takeDamage() {
        if (hearts > 0) {
            hearts--;
        }
    }

    public boolean isDead() {
        return hearts == 0;
    }

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;

        hearts = 3;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
