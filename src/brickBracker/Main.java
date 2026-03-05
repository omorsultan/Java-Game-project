package brickBracker;

import javax.swing.*;
import java.awt.*;

public class Main
{
    // Constants to eliminate magic numbers - DRY Principle
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 600;
    
    public static void main(String[] args)
    {
        JFrame obj = new JFrame();

        obj.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        obj.setResizable(false);
        obj.setTitle("Breakout Ball");
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        obj.setContentPane(bgPanel);

        GamePlay gamePlay = new GamePlay();
        gamePlay.setOpaque(false);
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(gamePlay);

        obj.setVisible(true);
    }
}
