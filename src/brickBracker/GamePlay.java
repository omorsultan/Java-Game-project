package brickBracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePlay extends JPanel implements KeyListener , ActionListener {

    private enum GameState { START, PLAYING, GAME_OVER, LEVEL_COMPLETE, WIN }
    
    // Constants to eliminate magic numbers - DRY Principle
    private static final int GAME_WIDTH = 700;
    private static final int GAME_HEIGHT = 600;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_HEIGHT = 8;
    private static final int PADDLE_Y = 550;
    private static final int INITIAL_LIVES = 3;
    private static final int TIMER_DELAY = 8;
    private static final int PADDLE_MOVE_SPEED = 40;
    private static final int INITIAL_BALL_X = 120;
    private static final int INITIAL_BALL_Y = 350;
    private static final int INITIAL_PADDLE_WIDTH = 150;
    private static final int INITIAL_PADDLE_X = 320;
    private static final int INITIAL_SPEED = 30;
    private static final int SPEED_INCREMENT = 7;
    private static final int SCORE_PER_BRICK = 5;
    private static final int MAX_LEVEL = 3;
    private static final int MAP_ROWS = 3;
    private static final int MAP_COLS = 7;
    private static final int BALL_RESET_DELAY = 1000;

    // Value objects to replace primitive obsession - Single Responsibility Principle
    private static class Position {
        public int x, y;
        public Position(int x, int y) { this.x = x; this.y = y; }
    }
    
    private static class Velocity {
        public float x, y;
        public Velocity(float x, float y) { this.x = x; this.y = y; }
    }
    
    private GameState gameState = GameState.START;
    private boolean play = false;
    private int score = 0;
    private int lives = INITIAL_LIVES;
    private int currentLevel = 1;
    private int scoreMultiplier = 1;

    private Timer timer;
    
    // Encapsulated game objects
    private Position ballPosition;
    private Velocity ballVelocity;
    private float speed = INITIAL_SPEED;
    private int paddleWidth = INITIAL_PADDLE_WIDTH;
    private int paddlePosX = INITIAL_PADDLE_X;

    private MapGenerator map;
    public GamePlay()
    {
        initializeGame(); // Extract initialization method - SRP
        setupUI();
    }
    
    // Extract initialization method - Single Responsibility Principle
    private void initializeGame() {
        ballPosition = new Position(INITIAL_BALL_X, INITIAL_BALL_Y);
        ballVelocity = new Velocity(0, 0);
        map = new MapGenerator(MAP_ROWS, MAP_COLS, currentLevel);
    }
    
    // Separate UI setup - Single Responsibility Principle  
    private void setupUI() {
        setOpaque(false);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

        switch (gameState) {
            case START:
                drawStartScreen(g);
                break;
            case PLAYING:
                drawPlayingScreen(g);
                break;
            case GAME_OVER:
                drawGameOverScreen(g);
                break;
            case LEVEL_COMPLETE:
                drawLevelCompleteScreen(g);
                break;
            case WIN:
                drawWinScreen(g);
                break;
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
    private void drawStartScreen(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("BREAKOUT", 220, 200);

        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Press SPACE to Start", 190, 300);
        g.drawString("Use LEFT and RIGHT arrows", 160, 350);
    }

    private void drawPlayingScreen(Graphics g) {
        map.draw((Graphics2D) g);

        drawGameStats(g); // Extract method - SRP
        drawPaddle(g);    // Extract method - SRP  
        drawBall(g);      // Extract method - SRP
    }
    
    // Extracted methods for better organization - Single Responsibility Principle
    private void drawGameStats(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 500, 30);
        g.drawString("Lives: " + lives, 100, 30);
        g.drawString("Level: " + currentLevel, 300, 30);
    }
    
    private void drawPaddle(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(paddlePosX, PADDLE_Y, paddleWidth, PADDLE_HEIGHT);
    }
    
    private void drawBall(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(ballPosition.x, ballPosition.y, BALL_SIZE, BALL_SIZE);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("GAME OVER", 180, 200);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Final Score: " + score, 230, 280);
        g.drawString("Press SPACE to Restart", 180, 350);
    }

    private void drawLevelCompleteScreen(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("LEVEL " + currentLevel + " COMPLETE!", 80, 200);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Score: " + score, 270, 280);
        g.drawString("Press SPACE for Next Level", 150, 350);
    }

    private void drawWinScreen(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("YOU WIN!", 230, 200);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Final Score: " + score, 230, 280);
        g.drawString("Press SPACE to Play Again", 170, 350);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.PLAYING && play) {
            updateGameState(); // Extract game logic - SRP
        }
        repaint();
    }
    
    // Extracted game update logic - Single Responsibility Principle
    private void updateGameState() {
        handlePaddleCollision(); // SRP - Handle paddle collision
        handleBrickCollisions(); // SRP - Handle brick collisions  
        updateBallPosition();    // SRP - Update ball movement
        handleWallCollisions();  // SRP - Handle boundary collisions
        checkGameConditions();   // SRP - Check win/lose conditions
    }
    
    // Handle paddle collision - Single Responsibility Principle
    private void handlePaddleCollision() {
        Rectangle ballRect = new Rectangle(ballPosition.x, ballPosition.y, BALL_SIZE, BALL_SIZE);
        Rectangle paddleRect = new Rectangle(paddlePosX, PADDLE_Y, paddleWidth, PADDLE_HEIGHT);
        
        if (ballRect.intersects(paddleRect)) {
            ballVelocity.y = -ballVelocity.y;
        }
    }
    
    // Handle brick collisions - Dependency Inversion Principle
    private void handleBrickCollisions() {
        Rectangle ballRect = new Rectangle(ballPosition.x, ballPosition.y, BALL_SIZE, BALL_SIZE);
        
        // Use MapGenerator's method instead of direct access - Encapsulation
        if (map.checkCollision(ballRect)) {
            // Collision handled by MapGenerator
            score += SCORE_PER_BRICK * scoreMultiplier;
            
            // Determine bounce direction based on collision
            if (map.isHorizontalCollision(ballRect)) {
                ballVelocity.x = -ballVelocity.x;
            } else {
                ballVelocity.y = -ballVelocity.y;
            }
        }
    }
    
    // Update ball position - Single Responsibility Principle  
    private void updateBallPosition() {
        ballPosition.x += ballVelocity.x;
        ballPosition.y += ballVelocity.y;
    }
    
    // Handle wall collisions - Single Responsibility Principle
    private void handleWallCollisions() {
        // Side walls
        if (ballPosition.x < 0 || ballPosition.x > GAME_WIDTH - BALL_SIZE) {
            ballVelocity.x = -ballVelocity.x;
        }
        // Top wall
        if (ballPosition.y < 0) {
            ballVelocity.y = -ballVelocity.y;
        }
        // Bottom - lose life
        if (ballPosition.y > GAME_HEIGHT - 30) {
            handleBallLoss();
        }
    }
    
    // Handle ball loss - Single Responsibility Principle
    private void handleBallLoss() {
        lives--;
        play = false;

        if (lives <= 0) {
            gameState = GameState.GAME_OVER;
        } else {
            resetBallAndPaddle();
            // Use Timer for delayed restart - Remove dead code comments
            Timer delayTimer = new Timer(BALL_RESET_DELAY, e -> {
                play = true;
                ((Timer) e.getSource()).stop();
            });
            delayTimer.start();
        }
    }
    
    // Check game win/lose conditions - Single Responsibility Principle
    private void checkGameConditions() {
        if (map.getBricksRemaining() <= 0) {
            play = false;
            if (currentLevel >= MAX_LEVEL) {
                gameState = GameState.WIN;
            } else {
                gameState = GameState.LEVEL_COMPLETE;
            }
        }
    }
    // Reset methods using constants and value objects - DRY Principle
    private void resetBallAndPaddle() {
        ballPosition.x = INITIAL_BALL_X;
        ballPosition.y = INITIAL_BALL_Y;

        ballVelocity.x = -0.1f * speed;
        ballVelocity.y = -0.2f * speed;
        paddlePosX = INITIAL_PADDLE_X;
        paddleWidth = INITIAL_PADDLE_WIDTH;
        scoreMultiplier = 1;
    }
    
    private void resetGame() {
        play = true;
        score = 0;
        lives = INITIAL_LIVES;
        speed = INITIAL_SPEED;
        currentLevel = 1;
        resetBallAndPaddle();
        map = new MapGenerator(MAP_ROWS, MAP_COLS, currentLevel);
    }
    
    private void nextLevel() {
        currentLevel++;
        speed += SPEED_INCREMENT;
        play = true;
        resetBallAndPaddle();
        map = new MapGenerator(MAP_ROWS, MAP_COLS, currentLevel);
    }
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e)
    {

        if (gameState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddlePosX = Math.min(paddlePosX + PADDLE_MOVE_SPEED, GAME_WIDTH - paddleWidth - 10);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
               paddlePosX = Math.max(paddlePosX - PADDLE_MOVE_SPEED, 0);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            switch (gameState) {
                case START:
                case GAME_OVER:
                case WIN:
                    gameState = GameState.PLAYING;
                    resetGame();
                    break;
                case LEVEL_COMPLETE:
                    gameState = GameState.PLAYING;
                    nextLevel();
                    break;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e)
    {}
}
