import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int GROUND_Y = 540;
    private static final int ROUND_SECONDS = 90;

    private final Player player;
    private final Ore[] ores = new Ore[5];
    private final Rock[] rocks = new Rock[2];

    private final Background background = new Background();

    private final Timer timer;
    private final JButton startButton;
    private final JButton restartButton;

    private boolean aPressed;
    private boolean dPressed;
    private boolean leftArrowPressed;
    private boolean rightArrowPressed;

    private boolean started = false;
    private boolean gameOver = false;

    private int score = 0;
    private int timeLeft = ROUND_SECONDS;

    private long roundStartTime;

    public GamePanel() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(35, 30, 45));
        setLayout(null);

        // ตัวละคร
        player = new Player(380, GROUND_Y - 60);

        // แร่
        for (int i = 0; i < ores.length; i++) {
            int x = randomX(GAME_WIDTH, 25);
            int y = -25 - i * 120;

            ores[i] = new Ore(x, y);
        }

        // หิน
        for (int i = 0; i < rocks.length; i++) {
            int x = randomX(GAME_WIDTH, 30);
            int y = -300 - i * 250;

            rocks[i] = new Rock(x, y);
        }

        setupKeys();

        // วงรอบเกม เริ่มเมื่อคลิก Start Game
        timer = new Timer(16, e -> {
            updateGame();
            repaint();
        });

        // ปุ่มเริ่มเกม
        startButton = new JButton("Start Game");
        startButton.setBounds(
            (GAME_WIDTH - 180) / 2,
            300,
            180,
            50
        );

        startButton.setFont(
            new Font("SansSerif", Font.BOLD, 20)
        );

        startButton.setFocusable(false);

        startButton.addActionListener(e -> {
            restartGame();
        });

        add(startButton);

        // ปุ่มเล่นใหม่
        restartButton = new JButton("Play Again");
        restartButton.setBounds(
            (GAME_WIDTH - 160) / 2,
            350,
            160,
            45
        );

        restartButton.setFont(
            new Font("SansSerif", Font.BOLD, 20)
        );

        restartButton.setFocusable(false);
        restartButton.setVisible(false);

        restartButton.addActionListener(e -> {
            restartGame();
        });

        add(restartButton);
    }

    public void clearKeys() {
        aPressed = false;
        dPressed = false;
        leftArrowPressed = false;
        rightArrowPressed = false;
    }

    private int randomX(int areaWidth, int objectSize) {
        int maxX = Math.max(0, areaWidth - objectSize);

        return (int) (Math.random() * (maxX + 1));
    }

    private void setupKeys() {
        // A
        bindKey("pressed A", "pressA", () -> {
            aPressed = true;
        });

        bindKey("released A", "releaseA", () -> {
            aPressed = false;
        });

        // D
        bindKey("pressed D", "pressD", () -> {
            dPressed = true;
        });

        bindKey("released D", "releaseD", () -> {
            dPressed = false;
        });

        // ลูกศรซ้าย
        bindKey("pressed LEFT", "pressLeft", () -> {
            leftArrowPressed = true;
        });

        bindKey("released LEFT", "releaseLeft", () -> {
            leftArrowPressed = false;
        });

        // ลูกศรขวา
        bindKey("pressed RIGHT", "pressRight", () -> {
            rightArrowPressed = true;
        });

        bindKey("released RIGHT", "releaseRight", () -> {
            rightArrowPressed = false;
        });
    }

    private void bindKey(String key, String name, Runnable action) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(key),
            name
        );

        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void updateGame() {
        if (!started || gameOver) {
            return;
        }

        // คำนวณเวลาที่เหลือ
        long elapsedSeconds =
            (System.nanoTime() - roundStartTime)
            / 1_000_000_000L;

        timeLeft = (int) Math.max(
            0L,
            ROUND_SECONDS - elapsedSeconds
        );

        if (timeLeft == 0) {
            endGame();
            return;
        }

        // การเดิน
        int direction = 0;

        if (aPressed || leftArrowPressed) {
            direction--;
        }

        if (dPressed || rightArrowPressed) {
            direction++;
        }

        player.move(direction, getWidth());

        // อัปเดตแร่และตรวจการเก็บ
        for (int i = 0; i < ores.length; i++) {
            Ore ore = ores[i];

            ore.update();

            if (player.getBounds().intersects(ore.getBounds())) {
                score += ore.getValue();
                resetOre(i);
            } else if (ore.isOffScreen(getHeight())) {
                resetOre(i);
            }
        }

        // อัปเดตหินและตรวจความเสียหาย
        for (int i = 0; i < rocks.length; i++) {
            Rock rock = rocks[i];

            rock.update();

            if (player.getBounds().intersects(rock.getBounds())) {
                resetRock(rock);
                damagePlayer();

                if (gameOver) {
                    return;
                }
            } else if (rock.isOffScreen(getHeight())) {
                resetRock(rock);
            }
        }
    }

    private void resetOre(int index) {
        int x = randomX(getWidth(), 25);
        int y = -25 - (int) (Math.random() * 200);

        ores[index].resetPosition(x, y);
    }

    private void resetRock(Rock rock) {
        int x = randomX(getWidth(), 30);
        int y = -100 - (int) (Math.random() * 400);

        rock.resetPosition(x, y);
    }

    private void damagePlayer() {
        player.takeDamage();

        if (player.isDead()) {
            endGame();
        }
    }

    private void endGame() {
        gameOver = true;
        clearKeys();

        timer.stop();
        restartButton.setVisible(true);
    }

    private void restartGame() {
        timer.stop();

        started = true;
        gameOver = false;

        score = 0;
        timeLeft = ROUND_SECONDS;

        clearKeys();

        player.reset(380, GROUND_Y - 60);

        // รีเซ็ตแร่
        for (int i = 0; i < ores.length; i++) {
            int x = randomX(getWidth(), 25);
            int y = -25 - i * 120;

            ores[i].resetPosition(x, y);
        }

        // รีเซ็ตหิน
        for (int i = 0; i < rocks.length; i++) {
            int x = randomX(getWidth(), 30);
            int y = -300 - i * 250;

            rocks[i].resetPosition(x, y);
        }

        startButton.setVisible(false);
        restartButton.setVisible(false);

        roundStartTime = System.nanoTime();

        timer.start();
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //background
        background.draw(g, getWidth(), getHeight());

        // หน้าเมนู
        if (!started) {
            g.setFont(new Font("SansSerif", Font.BOLD, 48));
            g.setColor(Color.ORANGE);
            drawCenteredText(g, "TAKE IT ORE!", 180);

            g.setFont(new Font("SansSerif", Font.PLAIN, 22));
            g.setColor(Color.WHITE);
            drawCenteredText(g, "Cave Collector", 220);

            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            drawCenteredText(  g, " Move A / D or  ← / →  ", 400
            );
            return;
        }


        // แร่
        for (int i = 0; i < ores.length; i++) {
            ores[i].draw(g);
        }

        // หิน
        for (int i = 0; i < rocks.length; i++) {
            rocks[i].draw(g);
        }

        // ตัวละคร
        player.draw(g);

        // คะแนน เวลา และหัวใจ
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.setColor(Color.WHITE);

        g.drawString("SCORE: " + score, 20, 30);
        drawCenteredText(g, "⏱︎: " + timeLeft, 30);

        g.drawString( "❤︎⁠: " + player.getHearts(),getWidth() - 130,30);

        // หน้าจอจบเกม
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());

            String message;

            if (player.isDead()) {
                message = "GAME OVER";
            } else {
                message = "TIME UP";
            }

            g.setFont(new Font("SansSerif", Font.BOLD, 40));
            g.setColor(Color.RED);
            drawCenteredText(g, message, 270);

            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            drawCenteredText( g,"Final Score: " + score, 315 );
        }
    }

    private void drawCenteredText(Graphics g, String text, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        int x = (getWidth() - textWidth) / 2;

        g.drawString(text, x, y);
    }
}