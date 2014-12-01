package tankgame;

import Airstrike.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Dong
 */
public class TankGame extends JApplet implements Runnable {

    Thread thread;
    BufferedImage bimg;
    Graphics2D g2;
    int frameCount;
    Image background, wall1, wall2, tank;
    int w = 900, h = 720;
    Wall testW;
    Tank testT;
    GameEvents gameEvent;
    KeyControl key;
    int[][] map;
    ArrayList<Wall> wall_list;

    public void init() {
        setFocusable(true);
        try {
            background = ImageIO.read(TankGame.class.getResource("TankResources/background.png"));
            wall1 = ImageIO.read(TankGame.class.getResource("TankResources/wall1.png"));
            wall2 = ImageIO.read(TankGame.class.getResource("TankResources/wall2.png"));
            tank = ImageIO.read(TankGame.class.getResource("TankResources/Tank1_strip60.png"));
            //System.out.println(wall1.getWidth(this) + " " + wall1.getHeight(this));
        } catch (Exception e) {
            System.out.println("No resource are found in init()");
        }

        //testW = new Wall(wall1, 10, 10, 0, false);
        testT = new Tank(tank, 1, 100, 100, 10, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        gameEvent = new GameEvents();
        gameEvent.addObserver(testT);
        key = new KeyControl(gameEvent);
        //KeyControl key2 = new KeyControl(gameEvent2);
        addKeyListener(key);
        //addKeyListener(key2);
        wall_list = new ArrayList<Wall>();
        map = readMap("map.csv", 22, 28,wall_list);
        
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
    
    public void drawMap(){
        for(int i = 0; i < wall_list.size(); i++){
            wall_list.get(i).draw(g2, this);
        }
    }
    public int[][] readMap(String fileName, int height, int width, ArrayList<Wall> w) {

        Scanner fileScanner;
        int map[][] = new int[height][width];
       
        try {
            fileScanner = new Scanner(new FileInputStream(fileName));
            fileScanner.useDelimiter(",|\\n|\\r");
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    String s = fileScanner.next();
                    //System.out.print(s);
                    if (s.equals("")) {
                        j--;
                        continue;
                    }
                    map[i][j] = Integer.parseInt(s);
                    //System.out.println(map[i][j]);
                    if(map[i][j] == 1){
                        w.add(new Wall(wall1,32*j,32*i,0,false));
                        //System.out.println(32*j + " "+ 32*i);
                    }
                    if(map[i][j] == 2){
                        w.add(new Wall(wall2,32*j,32*i,0,true));
                        //System.out.println(32*j + " "+ 32*i);
                    }
                }
            }
            /*for(int ii = 0; ii < height; ii++){
             for(int jj = 0; jj < width; jj++) {
             System.out.print(map[ii][jj]);
             }
             System.out.println();
             }*/
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    public void paint(Graphics g) {
        if (bimg == null) {
            Dimension windowSize = getSize();
            bimg = (BufferedImage) createImage(windowSize.width,
                    windowSize.height);
            g2 = bimg.createGraphics();
        }
        drawDemo();
        g.drawImage(bimg, 0, 0, this);
    }

    public void drawDemo() {

        drawBackGroundWithTileImage();
        drawMap();
        //testW.draw(g2, this);
        testT.draw(g2, this);
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
        f.setSize(new Dimension(900, 720));
        f.setVisible(true);
        f.setResizable(false);
        demo.start();
    }
}
