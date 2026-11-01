import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.io.File;

public class Background {
    private final Image imageBg;

    public Background() {
        File file = new File("images/background.png");
        imageBg = new ImageIcon(file.getAbsolutePath()).getImage();
    }

    public void draw(Graphics g, int width, int height) {
        g.drawImage(imageBg, 0, 0, width, height, null);
    }
}