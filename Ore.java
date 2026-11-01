import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Ore extends FallingObject {

    private final int value = 10;
    private BufferedImage image;

    public Ore(int x, int y) {
        super(x, y, 60, 4);
        try {
            image = ImageIO.read(
                    Player.class.getResource("/images/Ore.png"));
        } catch (IOException e) {
            throw new IllegalStateException("loading /images/Ore.png fail", e);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}