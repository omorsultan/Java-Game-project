package brickBracker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {

        backgroundImage = createDefaultBackground();

    }

     Image createDefaultBackground() {
        // Create a simple gradient background if image loading fails
        BufferedImage img = new BufferedImage(700, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();


         Color color1 = new Color(10, 0, 30);
         GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth()/2, getHeight()/2,color1);
         
         g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 700, 600);


        g2d.setColor(Color.WHITE);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(700);
            int y = rand.nextInt(600);
            int size = rand.nextInt(3) + 1;
            g2d.fillOval(x, y, size, size);
        }

        g2d.dispose();
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}