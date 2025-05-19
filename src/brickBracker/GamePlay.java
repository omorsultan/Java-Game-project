package brickBracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePlay extends JPanel implements KeyListener , ActionListener {

    private enum GameState { START, PLAYING, GAME_OVER, LEVEL_COMPLETE, WIN }

    private GameState gameState = GameState.START;
    private boolean play = false;
    private int score = 0;
    private int lives = 3;
    private int currentLevel = 1;
    private int totalBricks = 21 ;
    private int scoreMultiplier = 1;

    private Timer timer;
    private int delay = 8;

    private  int ballPosX = 120;
    private int ballPosY = 350;

    private float speed =30;
    private float ballXdir  ;
    private float ballYdir ;
    private int paddleWidth =150;
    private  int paddlePosX = 320;

    private MapGenerator map;
    private Random random = new Random();
    public GamePlay()
    {
        setOpaque(false);
        map = new MapGenerator(3,7,currentLevel);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
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

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 500, 30);
        g.drawString("Lives: " + lives, 100, 30);
        g.drawString("Level: " + currentLevel, 300, 30);

//paddle
        g.setColor(Color.green);
        g.fillRect(paddlePosX, 550, paddleWidth, 8);

// ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);
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
        if (gameState == GameState.PLAYING && play)
        {
            if(play)
            {
                if(new Rectangle(ballPosX,ballPosY,20,20).intersects(new Rectangle(paddlePosX,550,paddleWidth,8)))
                {
                    ballYdir = - ballYdir;
                }
                boolean brickHit = false;
                A:  for(int i =0;i<map.map.length;i++)
                {
                    for(int j=0;j<map.map[0].length;j++)
                    {
                        if(map.map[i][j] > 0) {
                            Rectangle brickRect = new Rectangle(
                                    j * map.brickWidth + 80,
                                    i * map.brickHeight + 50,
                                    map.brickWidth,
                                    map.brickHeight
                            );

                            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(brickRect)) {
                                map.map[i][j]--;
                                if(map.map[i][j] == 0) {
                                    map.totalBricks--;
                                    System.out.println("Bricks left: " + map.totalBricks); // Debug
                                }
                                score += 5 * scoreMultiplier;

//                                System.out.println(score);
                                if(ballPosX +19<=brickRect.x||ballPosX+1>=brickRect.x +brickRect.width)
                                    ballXdir = - ballXdir;
                                else
                                    ballYdir=-ballYdir;
                                brickHit = true;
                                break A;
                            }
                        }
                    }
                }
        }
//        timer.start();

            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if(ballPosX < 0 || ballPosX> 670)
            {
                ballXdir = -ballXdir;
            }
            if(ballPosY < 0)
            {
                ballYdir = - ballYdir;
            }
            if (ballPosY > 570)
            {
                lives--;
                play = false;

                if (lives <= 0) {
                    gameState = GameState.GAME_OVER;
                } else {
                    resetBallAndPaddle();
                    new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            play = true;
                            ((Timer) e.getSource()).stop();
                        }
                    }).start();
                }
            }
            if (map.totalBricks <= 0) {
                play = false;
                if (currentLevel >= 3) {
                    gameState = GameState.WIN;
                } else {
                    gameState = GameState.LEVEL_COMPLETE;
                }
            }

        }


        repaint();
    }
    private void resetBallAndPaddle() {
        ballPosX = 120;
        ballPosY = 350;

        ballXdir = -.1f*speed;
        ballYdir = -.2f*speed;
        paddlePosX = 310;
        paddleWidth = 150;
//        consecutiveHits = 0;
        scoreMultiplier = 1;
    }
    private void resetGame() {
        play = true;
        score = 0;
        lives = 3;
        speed=30;
        currentLevel = 1;
        resetBallAndPaddle();
        map = new MapGenerator(3, 7, currentLevel);
//        activePowerUp = null;
    }
    private void nextLevel() {
        currentLevel++;
        speed+=7;
        play = true;
        resetBallAndPaddle();
        map = new MapGenerator(3, 7, currentLevel);
//        activePowerUp = null;
    }
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e)
    {

        if (gameState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddlePosX = Math.min(paddlePosX + 40, 700-paddleWidth-10);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
               paddlePosX = Math.max(paddlePosX- 40, 0);
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
