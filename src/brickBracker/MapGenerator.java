package brickBracker;

import java.awt.*;

public class MapGenerator
{
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public int totalBricks;
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
        brickWidth = 540/col;
        brickHeight =150 / row;
    }
    public void draw(Graphics2D g)
    {
        for(int i=0;i< map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                if(map[i][j]>0)
                {
                    if(map[i][j]==3){
                        g.setColor(Color.RED);
                    }
                    else if (map[i][j] == 2) {
                        g.setColor(Color.orange);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(j*brickWidth + 80 ,i*brickHeight+40,brickWidth,brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j*brickWidth + 80 ,i*brickHeight+40,brickWidth,brickHeight);
                }
            }
        }
    }
    public void setBrickValue(int value,int row , int col)
    {
        map[row][col]=value;
    }
}
