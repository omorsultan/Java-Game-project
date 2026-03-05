package brickBracker;

import java.awt.*;

public class MapGenerator
{
    // Private fields to enforce encapsulation - Information Hiding Principle
    private int map[][];
    private int brickWidth;
    private int brickHeight;
    private int totalBricks;
    
    // Constants for magic numbers - DRY Principle
    private static final int BRICK_START_X = 80;
    private static final int BRICK_START_Y = 50; // Changed from 40 to match collision detection
    private static final int TOTAL_BRICK_WIDTH = 540;
    private static final int TOTAL_BRICK_HEIGHT = 150;
    public MapGenerator(int row,int col,int level)
    {
        map = new int[row][col];
        totalBricks = row * col;

        for(int i=0;i< map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                if (level >= 3 && (i == 0 || j == 0 || j == map[0].length - 1)) {
                    map[i][j] = 3;
                } else if (level >= 2 && (i == 0 || j % 2 == 0)) {
                    map[i][j] = 2;
                } else {
                    map[i][j] = 1;
                }
            }
        }
        brickWidth = TOTAL_BRICK_WIDTH/col;
        brickHeight = TOTAL_BRICK_HEIGHT / row;
    }
    
    // Getter methods for encapsulation - Information Hiding Principle
    public int getBricksRemaining() {
        return totalBricks;
    }
    
    public int getBrickWidth() {
        return brickWidth;
    }
    
    public int getBrickHeight() {
        return brickHeight;
    }
    
    // Collision detection method - Single Responsibility Principle
    public boolean checkCollision(Rectangle ballRect) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] > 0) {
                    Rectangle brickRect = getBrickRectangle(i, j);
                    
                    if(ballRect.intersects(brickRect)) {
                        map[i][j]--;
                        if(map[i][j] == 0) {
                            totalBricks--;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // Helper method to get brick rectangle - DRY Principle
    private Rectangle getBrickRectangle(int row, int col) {
        return new Rectangle(
            col * brickWidth + BRICK_START_X,
            row * brickHeight + BRICK_START_Y,
            brickWidth,
            brickHeight
        );
    }
    
    // Method to determine collision direction - Single Responsibility Principle
    public boolean isHorizontalCollision(Rectangle ballRect) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(map[i][j] > 0) {
                    Rectangle brickRect = getBrickRectangle(i, j);
                    if(ballRect.intersects(brickRect)) {
                        // Check if ball hits from left or right side
                        return (ballRect.x + ballRect.width <= brickRect.x + 1) || 
                               (ballRect.x >= brickRect.x + brickRect.width - 1);
                    }
                }
            }
        }
        return false;
    }
    public void draw(Graphics2D g)
    {
        for(int i=0;i< map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                if(map[i][j]>0)
                {
                    // Use strategy pattern for brick colors - Open/Closed Principle
                    setBrickColor(g, map[i][j]);
                    g.fillRect(j*brickWidth + BRICK_START_X, i*brickHeight + BRICK_START_Y, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j*brickWidth + BRICK_START_X, i*brickHeight + BRICK_START_Y, brickWidth, brickHeight);
                }
            }
        }
    }
    
    // Extract color setting method - Single Responsibility Principle
    private void setBrickColor(Graphics2D g, int brickType) {
        switch(brickType) {
            case 3:
                g.setColor(Color.RED);
                break;
            case 2:
                g.setColor(Color.ORANGE);
                break;
            default:
                g.setColor(Color.WHITE);
        }
    }
    public void setBrickValue(int value,int row , int col)
    {
        map[row][col]=value;
    }
}
