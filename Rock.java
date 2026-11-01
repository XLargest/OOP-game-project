import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Rock extends FallingObject {

    private BufferedImage image;

    public Rock(int x, int y) {
        super(x, y, 60, 4);
        try {
            image = ImageIO.read(
                    Player.class.getResource("/images/Rock.png"));
        } catch (IOException e) {
            throw new IllegalStateException("loading /images/Rock.png fail", e);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}
