import javax.swing.JFrame;

public class GameFrame extends JFrame{
  public GameFrame(){
    setTitle("Take It Ore: Cave Collector");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    add(new GamePanel());

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
