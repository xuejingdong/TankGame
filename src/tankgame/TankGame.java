package TankGame;

import Airstrike.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Dong
 */
public class TankGame extends JApplet implements Runnable{
    Thread thread;
    BufferedImage bimg;
    Graphics2D g2;
    int frameCount;
    Image background;
    int w = 1200, h = 1200;;
    
    
    public void init() {
        try{
        background = ImageIO.read(new File("TankResources/background.png"));
        }
        catch(Exception e){
            System.out.println("No resource are found in init()");
        }

    }
    public void drawBackGroundWithTileImage() {
       int TileWidth = background.getWidth(this);
       int TileHeight = background.getHeight(this);

       int NumberX = (int) (w / TileWidth);
       int NumberY = (int) (h / TileHeight);

       for (int i = -1; i <= NumberY; i++) {
           for (int j = 0; j <= NumberX; j++) {
               g2.drawImage(background, j * TileWidth, 
                       i * TileHeight, TileWidth, 
                       TileHeight, this);
           }
       
       }
    }
    public void paint(Graphics g) {
        if(bimg == null) {
            Dimension windowSize = getSize();
            bimg = (BufferedImage) createImage(windowSize.width, 
                    windowSize.height);
            g2 = bimg.createGraphics();
        }
        drawDemo();
        g.drawImage(bimg, 0, 0, this);
    }
    
    public void drawDemo(){
        
        drawBackGroundWithTileImage();
    }
    public void start() {
        System.out.println();
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();  
          try {
                thread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }

    
    public static void main(String argv[]) {
        final TankGame demo = new TankGame();
        demo.init();
        JFrame f = new JFrame("TankWar");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(1200, 1200));
        f.setVisible(true);
        f.setResizable(false);
        demo.start();
    }
    
}
