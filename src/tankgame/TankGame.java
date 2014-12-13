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
    BufferedImage bimg,miniBuf, leftImg,rightImg,gDisplayBuf;
    Image miniMap;
    Graphics2D g2,gMini,gDisplay;
    int frameCount;
    Image background, wall1, wall2, tankRed, tankBlue,powerUp;
    static Image bigExp,smallExp;
    int w = 1200, h = 1200;
    Wall testW;
    Tank tank1,tank2;
    GameEvents gameEvent1,gameEvent2;
    KeyControl key1,key2;
    int[][] map;
    static ArrayList<Wall> wall_list;
    static ArrayList<TankGameExplosion> explosion;
    static ArrayList<TankGamePowerUp> powerup;
    TankCollisionDetector CD;
    SoundPlayer sp;

    public void init() {
        setFocusable(true);
        try {
            background = ImageIO.read(TankGame.class.getResource("TankResources/background.png"));
            wall1 = ImageIO.read(TankGame.class.getResource("TankResources/wall1.png"));
            wall2 = ImageIO.read(TankGame.class.getResource("TankResources/wall2.png"));
            tankBlue = ImageIO.read(TankGame.class.getResource("TankResources/Tank1_strip60.png"));
            tankRed = ImageIO.read(TankGame.class.getResource("TankResources/Tank2_strip60.png"));
            bigExp = ImageIO.read(TankGame.class.getResource("TankResources/Explosion_large_strip7.png"));
            smallExp = ImageIO.read(TankGame.class.getResource("TankResources/Explosion_small_strip6.png"));
            powerUp = ImageIO.read(TankGame.class.getResource("TankResources/Weapon_strip4.png"));
            //System.out.println(wall1.getWidth(this) + " " + wall1.getHeight(this));
        } catch (Exception e) {
            System.out.println("No resource are found in init()");
        }

        tank1 = new Tank(tankBlue, 1, 280, 738, 6, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        tank2 = new Tank(tankRed,1,878,738,6,KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        gameEvent1 = new GameEvents();
        gameEvent2 = new GameEvents();
        gameEvent1.addObserver(tank1);
        gameEvent2.addObserver(tank2);
        key1 = new KeyControl(gameEvent1);
        key2 = new KeyControl(gameEvent2);
        addKeyListener(key1);
        addKeyListener(key2);
        CD = new TankCollisionDetector(gameEvent1, gameEvent2);
        wall_list = new ArrayList<Wall>(2000);
        explosion = new ArrayList<TankGameExplosion>(200);
        powerup = new ArrayList<TankGamePowerUp>(10);
        map = readMap("map.csv", 37, 38,wall_list,powerup);
        sp = new SoundPlayer(1,"TankResources/background.wav");
        
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
        for(int i = 0; i < powerup.size(); i++){
            powerup.get(i).draw(g2, this);
        }
    }
    
    public void updateMap(){
        for(int i = 0; i < wall_list.size(); i++){
            wall_list.get(i).update();
        }
        for(int i = 0; i < powerup.size(); i++){
            powerup.get(i).update();
        }
    }
    public int[][] readMap(String fileName, int height, int width, ArrayList<Wall> w,ArrayList<TankGamePowerUp> p) {

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
                    if(map[i][j] == 3){
                        p.add(new TankGamePowerUp(powerUp,32*j,32*i,0,1));
                        //System.out.println(32*j + " "+ 32*i);
                    }
                    if(map[i][j] == 4){
                        p.add(new TankGamePowerUp(powerUp,32*j,32*i,0,2));
                        //System.out.println(32*j + " "+ 32*i);
                    }
                    if(map[i][j] == 5){
                        p.add(new TankGamePowerUp(powerUp,32*j,32*i,0,3));
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
            bimg = (BufferedImage) createImage(w,h);
            gDisplayBuf = (BufferedImage) createImage(640,480);
            g2 = bimg.createGraphics();
            gDisplay = gDisplayBuf.createGraphics();
            
        }
        
        drawDemo();
        g.drawImage(gDisplayBuf, 0, 0, this);
        
    }

    public void drawDemo() {
        String content = ""+tank1.getScore();
        String content1 = ""+tank2.getScore();
        Font stringFont = new Font( "SansSerif", Font.PLAIN, 18 ); 
        g2.setFont(stringFont); 
        g2.setColor(Color.white);
        
        if(tank1.getBoom()){
            stringFont = new Font( "SansSerif", Font.BOLD, 40); 
            gDisplay.setFont( stringFont ); 
            gDisplay.setColor(Color.white);
            gDisplay.drawString(" PLAYER2 WON! ", 170, 220);
            sp.stop();;
         }else if(tank2.getBoom()){
            stringFont = new Font( "SansSerif", Font.BOLD, 40 ); 
            gDisplay.setFont( stringFont );
            gDisplay.setColor(Color.white);
            gDisplay.drawString("PALYER1 WON!", 170, 220);
            sp.stop();
         }
         else{
        //updating the gameObjects by first checking the collisions
        CD.TankVSTank(tank1, tank2);
        CD.TankBulletVSWall(tank1, tank2);
        CD.TankVSTankBullet(tank1, tank2);
        CD.TankVSPowerUp(tank1, tank2);
        updateMap();
        for(int i = 0; i < tank1.getBulletList().size();i++){
            if(tank1.getBulletList().get(i).getShow())
                tank1.getBulletList().get(i).update(w,h);
        }
        for(int i = 0; i < tank2.getBulletList().size();i++){
            if(tank2.getBulletList().get(i).getShow())
                tank2.getBulletList().get(i).update(w,h);
        }
         for(int i = 0; i< explosion.size(); i++){
                if(explosion.get(i).getFinished()) {
                    explosion.remove(i);
                    i --;
                }
                else{
                    explosion.get(i).update();
                }
            }
                
        //draw the gameObjects after updating
        drawBackGroundWithTileImage();
        drawMap();
        tank1.draw(g2, this);
        tank2.draw(g2, this);
        for(int i = 0; i < tank1.getBulletList().size(); i++){
                tank1.getBulletList().get(i).draw(g2,this);
        }
        for(int i = 0; i < tank2.getBulletList().size(); i++){
                tank2.getBulletList().get(i).draw(g2,this);
        }
        for(int i = 0; i < explosion.size(); i++){
                explosion.get(i).draw(g2, this);
        }
        g2.drawString(content, tank1.getX()-5, tank1.getY()-5);
        g2.drawString(content1, tank2.getX()-5, tank2.getY()-5);
        //cut the iamge into left and right
        leftImg = bimg.getSubimage(tank1.getX()-150, tank1.getY()-240, 316, 480);
        rightImg = bimg.getSubimage(tank2.getX()-150, tank2.getY()-240, 316, 480);
        
        //get scaled image
        BufferedImage temp = bimg.getSubimage(150, 250, 900, 710);
        miniMap = temp.getScaledInstance(120, 100, Image.SCALE_SMOOTH);
       //put all three map segments into one buffered iamge
        gDisplay.drawImage(leftImg, 0, 0, this);
        gDisplay.drawImage(rightImg,317,0,this);
        gDisplay.drawImage(miniMap, 260,300,this);
       }  
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
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
        f.setResizable(false);
        demo.start();
    }
}
