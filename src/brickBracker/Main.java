package brickBracker;

import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame obj = new JFrame();

        obj.setSize(700,600);
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

//        obj.add(gamePlay);
        obj.setVisible(true);



    }
}
