package controller;

import pair.Pair;
import direction.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import java.util.List;

public class GameController implements ActionListener, KeyListener {

    private static Logger logger = Logger.getLogger("InfoLogging");


    // Declare parameter that are taken by class when initialised
    private int gameSpeed;
    private int screenWidth;
    private int screenHeight;

    // Declare rest of required variables
    private RenderPanel renderPanel;
    private Timer timer = new Timer(20, this);
    private JFrame jframe = new JFrame("Snake");
    private ArrayList<Point> snakeParts = new ArrayList<Point>();
    private Direction direction = Direction.LEFT;
    private final int scale;
    private long ticks;
    private int tailLength;
    private int score;
    private Point head;
    private Point target;
    private Random random = new Random();
    private boolean gameOver;
    private boolean gamePaused;

    public GameController(int gameSpeedArgument, int screenWidthArgument, int screenHeightArgument) {
        scale = 16;
        gameSpeed = gameSpeedArgument;
        screenWidth = screenWidthArgument * scale;
        screenHeight = screenHeightArgument * scale;
    }

    // Reset game to starting state
    private void reset() {
        gameOver = false;
        gamePaused = false;
        ticks = 0;
        tailLength = 0;
        score = 0;
        snakeParts.clear();
        head = setHeadStartingLocation();
        target = generateRandomTarget();
        timer.start();
    }

    // Create window in which game is played
    public void createWindow() {
        renderPanel = new RenderPanel(this);
        // Set window size
        // If PreferredSize is not set, window might not display on windows machines
        jframe.setPreferredSize(new Dimension(screenWidth, screenHeight));
        jframe.setSize(screenWidth, screenHeight);

        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderPanel);
        jframe.addKeyListener(this);
        reset();
    }

    // Games run inside this method, also responsible for calling repaint to refresh game on screen
    public void actionPerformed(ActionEvent arg0) {
        ticks++;
        if (!gameOver) {
            renderPanel.repaint();
        }
        if (ticks % gameSpeed == 0 && head != null && !gameOver) {
            addNewSnakeSegment();
            updatePosition();
            removeLastSnakeSegment();
            checkTargetCollision();
        }
    }

    private Pair<Point, Boolean> calculatePosition(ArrayList<Point> parts, Point head, Direction dir) {
        if (checkTailCollision(parts, head)) {
            return new Pair(head, true);
        }
        Pair pair = new Pair(new Point(head.x, head.y), false);
        switch (dir) {
            case UP:
                if (head.y > 0) {
                    pair.setLeft(new Point(head.x, head.y - 1));
                } else {
                    pair.setRight(true);
                }
                break;
            case DOWN:
                if (head.y < (screenHeight / scale) - 3) {
                    pair.setLeft(new Point(head.x, head.y + 1));
                } else {
                    pair.setRight(true);
                }
                break;
            case LEFT :
                if (head.x > 0) {
                    pair.setLeft(new Point(head.x - 1, head.y));
                } else {
                    pair.setRight(true);
                }
                break;
            case RIGHT :
                if (head.x < (screenWidth / scale) - 1) {
                    pair.setLeft(new Point(head.x + 1, head.y));
                } else {
                    pair.setRight(true);
                }
                break;
            // Shouldn't ever happen
            default:
        }
        return pair;
    }

    // Control direction of avatars movement, additionally check if collided with wall or itself (to stop game)
    public void updatePosition() {
        Pair result = calculatePosition(snakeParts, head, direction);
        gameOver = (Boolean) result.getRight();
        head = (Point) result.getLeft();
    }

    // Check if avatars 'head' collided with 'target', if yes: remove it, add 'score' & 'tailLength', spawn new 'target'
    private void checkTargetCollision() {
        if (target != null && head.equals(target)) {
            score++;
            tailLength++;
            target = generateRandomTarget();
        }
    }

    // Check if avatars 'head' collided with 'tail'
    public boolean checkTailCollision(List<Point> entries, Point head) {
        ArrayList<Point> parts = new ArrayList<Point>(entries);

        int tailSize = parts.size() - 1;
        // Size needs be more than 1 else it's just head
        if (tailSize < 1) {
            return false;
        }
        ArrayList<Point> withoutHead = new ArrayList<Point>(parts.subList(0, tailSize));
        for (Point point : withoutHead) {
            if (point.equals(head)) {
                return true;
            }
        }
        return false;
    }

    // Stops game timer if game is paused, restarts it when unpaused
    private void checkGameState() {
        if (gamePaused) {
            logger.info("timer stop\n");
            timer.stop();
            // Need to manually paint as no actions are performed when timer stopped.
            if (renderPanel != null) {
                renderPanel.repaint();
            }
        } else {
            logger.info("timer restart\n");
            timer.restart();
        }
    }

    // Set avatars head in middle of screen
    private Point setHeadStartingLocation() {
        return new Point((screenWidth / scale) / 2, (screenHeight / scale) / 2);
    }

    // Return new random location for target
    private Point generateRandomTarget() {
        return new Point(random.nextInt((screenWidth / scale) - 1), random.nextInt((screenHeight / scale) - 2));
    }

    // Add new element (new head position) at the end of 'snakeParts'
    private void addNewSnakeSegment() {
        snakeParts.add(new Point (head.x, head.y));
    }

    // Remove last element of avatar (1st index in 'snakeParts')
    private void removeLastSnakeSegment() {
        if (snakeParts.size() > tailLength) {
            snakeParts.remove(0);
        }
    }

    public Direction getKeyDirection(int button) {
        switch (button) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                return Direction.LEFT;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                return Direction.RIGHT;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                return Direction.UP;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                return Direction.DOWN;
            default:
                return Direction.NONE;
        }
    }

    public Direction getNewDirection(int keycode, Direction currentDir) {
        Direction keyDir = getKeyDirection(keycode);
        Direction newDirection = currentDir;
        if (
            keyDir == Direction.LEFT && currentDir != Direction.RIGHT ||
            keyDir == Direction.RIGHT && currentDir != Direction.LEFT ||
            keyDir == Direction.UP && currentDir != Direction.DOWN ||
            keyDir == Direction.DOWN && currentDir != Direction.UP
        ) {
            newDirection = keyDir;
        }
        return newDirection;
    }

    // Control game with keyboard
    public void keyPressed(KeyEvent e) {
        int button = e.getKeyCode();
        direction = getNewDirection(button, direction);

        // Pause or reset game (if over already) using 'space'
        if (button == KeyEvent.VK_SPACE) {
            if (gameOver) {
                logger.info("Game restart\n");
                reset();
            } else {
                logger.info("Game paused\n");
                gamePaused = !gamePaused;
                checkGameState();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // Unused handler, we only require onKeyPressed.
    }

    public void keyTyped(KeyEvent e) {
        // Unused handler, we only require onKeyPressed.
    }

    // Methods handling variables needed by other classes
    public Point getHead() {
        return head;
    }

    public Point getTarget() {
        return target;
    }

    public List<Point> getSnakeParts() {
        return snakeParts;
    }

    public void setSnakeParts(List<Point> parts) {
        snakeParts = new ArrayList<Point>(parts);
    }

    public void setHead(Point newHead) {
        head = newHead;
    }

    public String getScoreString() {
        return "Score: " + score + "    Time: " + (ticks / (150 / gameSpeed));
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScale() {
        return scale;
    }

    public boolean isGameOverOver() {
        return gameOver;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

}