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

    private  int playerX = 310;
//    private int paddleWidth = 200;

    private  int ballPosX = 120;
    private int ballPosY = 350;

    private int ballXdir = -2;
    private int ballYdir = -4;

    private MapGenerator map;
    private Random random = new Random();
    public GamePlay()
    {
        map = new MapGenerator(3,2,currentLevel);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();

    }
    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D) g);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score is " + score, 500, 30);

        // the paddale
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (ballPosY > 570)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over ! \n Score is : "+score,220,280);
        }

//        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play)
        {
            if(new Rectangle(ballPosX,ballPosY,20,20).intersects(new Rectangle(playerX,550,100,8)))
            {
                ballYdir = - ballYdir;
            }
          A:  for(int i =0;i<map.map.length;i++)
            {
                    for(int j=0;j<map.map[0].length;j++)
                    {
                        if(map.map[i][j]>0)
                        {
                            int brickX = j*map.brickWidth + 80;
                            int brickY =i*map.brickHeight + 50;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                            Rectangle ballRect = new Rectangle(ballPosX,ballPosY,20,20);
                            Rectangle brickRect = rect;

                            if(ballRect.intersects(brickRect))
                            {
                                map.setBrickValue(0,i,j);
                                totalBricks -- ;
                                score += 5;
//                                System.out.println(score);
                                if(ballPosX +19<=brickRect.x||ballPosX+1>=brickRect.x +brickRect.width)
                                {
                                    ballXdir = - ballXdir;
                                }
                                else
                                {
                                    ballYdir=-ballYdir;
                                }
                                break A;
                            }
                        }
                    }
            }
            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if(ballPosX < 0 )
            {
                ballXdir = -ballXdir;
            }
            if(ballPosY < 0)
            {
                ballYdir = - ballYdir;
            }
            if(ballPosX> 670)
            {
                ballXdir = - ballXdir;
            }

        }


        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e){ }
    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode()== KeyEvent.VK_RIGHT)
        {
            if(playerX>=600)
            {
                playerX=600;
            }
            else
            {
                moveRight();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
        {
            if(playerX<10)
            {
                playerX=10;
            }
            else
            {
                moveLeft();
            }
        }

    }
    public void moveRight()
    {
        play = true;
        playerX+=50;
    }
    public void moveLeft()
    {
        play = true;
        playerX-=50;
    }
}
