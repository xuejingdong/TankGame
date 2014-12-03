/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airstrike;

/**
 *
 * @author Dong
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class GameWorld extends JApplet implements Runnable{
    private Thread thread;
    Image sea;
    Image myPlane,power,boss_Enemy;
    Image island1, island2, island3, blue_enemyImg,white_enemyImg,yellow_enemyImg, back_enemyImg;
    static Image enemyBulletSmall,enemyBulletBig; 
    static Image [] smallExp = new Image[6];
    static Image [] bigExp = new Image[7];
    private BufferedImage bimg;
    Graphics2D g2;
    int speed = 1, move = 0;
    Random generator = new Random(1234567);
    Island I1, I2, I3;
    int w = 640, h = 480; // fixed size window game 
    GameEvents gameEvent1, gameEvent2;
    int eneCount = 20;
    static boolean isBossDied = false;
    final int GREEN_ENEMY_DAMAGE = 3;
    final int YELLOW_ENEMY_DAMAGE = 6;
    final int WHITE_ENEMY_DAMAGE = 12;
    final int GREEN_ENEMY_HEALTH = 2;
    final int YELLOW_ENEMY_HEALTH = 4;
    final int WHITE_ENEMY_HEALTH = 6;
    final int TOP_ENEMY_DIRECTION = 0;
    final int BACK_ENEMY_DIRECTION =1;
    final int BOSS_DAMAGE = 90;
    final int BOSS_HEALTH = 500;
    int playerPlaneDamage = 10;
    int playerBulletDamage = 4;
    int frameCount = 0;
    Player player1,player2;
    CollisionDetector CD;
    SoundPlayer sp;
    static ArrayList<EnemyPlane> enemyl = new ArrayList<EnemyPlane>(10000);
    static ArrayList<Bullet> enemybl = new ArrayList<Bullet>(100000);
    static ArrayList<Explosion> explosions = new ArrayList<Explosion>(100000);
    static ArrayList<PowerUp> powerUp = new ArrayList<PowerUp>();
          
    public void init() {
        setFocusable(true);
        setBackground(Color.white);
        try {
        //sea = ImageIO.read(new File("Resources/water.png"));
        sea = ImageIO.read(GameWorld.class.getResource("Resources/water.png"));

        island1 = ImageIO.read(GameWorld.class.getResource("Resources/island1.png"));
        island2 = ImageIO.read(GameWorld.class.getResource("Resources/island2.png"));
        island3 = ImageIO.read(GameWorld.class.getResource("Resources/island3.png"));
        myPlane = ImageIO.read(GameWorld.class.getResource("Resources/myplane_1.png"));
        blue_enemyImg = ImageIO.read(GameWorld.class.getResource("Resources/enemy1_1.png"));
        yellow_enemyImg = ImageIO.read(GameWorld.class.getResource("Resources/enemy2_1.png"));
        white_enemyImg = ImageIO.read(GameWorld.class.getResource("Resources/enemy3_1.png"));
        back_enemyImg = ImageIO.read(GameWorld.class.getResource("Resources/enemy4_1.png"));
        enemyBulletSmall = ImageIO.read(GameWorld.class.getResource("Resources/enemybullet1.png"));
        enemyBulletBig = ImageIO.read(GameWorld.class.getResource("Resources/enemybullet2.png"));
       
        boss_Enemy = ImageIO.read(GameWorld.class.getResource("Resources/boss.png"));
        power = ImageIO.read(GameWorld.class.getResource("Resources/powerup.png"));
        //get small explosion image array
        smallExp[0] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_1.png"));
        smallExp[1] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_2.png"));
        smallExp[2] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_3.png"));
        smallExp[3] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_4.png"));
        smallExp[4] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_5.png"));
        smallExp[5] = ImageIO.read(GameWorld.class.getResource("Resources/explosion1_6.png"));
        //get big explosion image array
        bigExp[0] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_1.png"));
        bigExp[1] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_2.png"));
        bigExp[2] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_3.png"));
        bigExp[3] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_4.png"));
        bigExp[4] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_5.png"));
        bigExp[5] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_6.png"));
        bigExp[6] = ImageIO.read(GameWorld.class.getResource("Resources/explosion2_7.png"));
        
        I1 = new Island(island1, 100, 100, speed, generator);
        I2 = new Island(island2, 200, 400, speed, generator);
        I3 = new Island(island3, 300, 200, speed, generator);
        
        player1 = new Player(1,3,KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        player2 = new Player(2,3,KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        
        if(player1 == null)
            System.out.println("Player 1 is null!");
        if(player2 == null)
            System.out.println("Player 2 is null!");
        
        for(int i = 0; i < eneCount; i++){
            boolean canShoot;
            if(i%2 == 0)
                canShoot = true;
            else 
                canShoot = false;
            enemyl.add( new EnemyPlane(blue_enemyImg,1,GREEN_ENEMY_HEALTH,GREEN_ENEMY_DAMAGE,TOP_ENEMY_DIRECTION,-20-10*i,2,generator,canShoot,false));
        }
        
        gameEvent1 = new GameEvents();
        gameEvent2 = new GameEvents();
        gameEvent1.addObserver(player1.getPlane());
        gameEvent2.addObserver(player2.getPlane());
        KeyControl key1 = new KeyControl(gameEvent1);
        KeyControl key2 = new KeyControl(gameEvent2);
        addKeyListener(key1);
        addKeyListener(key2);
        sp = new SoundPlayer(1,"Resources/background.wav");
        CD = new CollisionDetector(gameEvent1,gameEvent2);
        
        }
        catch (Exception e) {
            System.out.print(e.getStackTrace() +" No resources are found");
        }
    }
    //function added to control what kind of enemy plane is showed
    public void timelineControl(){
        //create PowerUp
        if(frameCount%350 == 0 && frameCount < 4000){
            int x = Math.abs(generator.nextInt() % (600 - 30));
            powerUp.add(new PowerUp(power,x,-20,1,1));
        }
        //create 2 enemy planes that fly from the back
        if(frameCount%120 == 0 && frameCount < 2000){
            for(int i =0; i <3; i++){
                enemyl.add( new EnemyPlane(back_enemyImg,4,GREEN_ENEMY_HEALTH,GREEN_ENEMY_DAMAGE,BACK_ENEMY_DIRECTION,480,-2,generator,false,false));
            }
        }
        
        if(frameCount == 150 || frameCount == 250 || frameCount == 450||frameCount == 700 ||frameCount == 2800){
            for(int i = 0; i < eneCount; i++){
                boolean canShoot;
                if(i%3 == 0)
                    canShoot = true;
                else 
                    canShoot = false;
                enemyl.add( new EnemyPlane(blue_enemyImg,1,GREEN_ENEMY_HEALTH,GREEN_ENEMY_DAMAGE+10,TOP_ENEMY_DIRECTION,-20-20*i,2,generator,canShoot,false));
            }
        }
        if(frameCount == 800 || frameCount == 1000 || frameCount == 1250 ||frameCount == 1500 ||frameCount == 3200){
           for(int i = 0; i < eneCount; i++){
                enemyl.add( new EnemyPlane(yellow_enemyImg,2,YELLOW_ENEMY_HEALTH,YELLOW_ENEMY_DAMAGE,TOP_ENEMY_DIRECTION,-20-20*i,2,generator,true,false));
           }
        }
        if(frameCount == 1700 || frameCount == 1950||frameCount == 2200 ||frameCount == 2500 ||frameCount == 3600){
            for(int i = 0; i < eneCount; i++){
                enemyl.add( new EnemyPlane(white_enemyImg,3,WHITE_ENEMY_HEALTH,WHITE_ENEMY_DAMAGE,TOP_ENEMY_DIRECTION,-20-20*i,2,generator,true,false));
           }
        }
        //BOSS
        if(frameCount == 4000){
            enemyl.add( new EnemyPlane(boss_Enemy,5,BOSS_HEALTH,BOSS_DAMAGE,TOP_ENEMY_DIRECTION,-1,1,generator,true,true));
        }
            
    }
    public void drawBackGroundWithTileImage() {
        int TileWidth = sea.getWidth(this);
        int TileHeight = sea.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(sea, j * TileWidth, 
                        i * TileHeight + (move % TileHeight), TileWidth, 
                        TileHeight, this);
            }
        }
        move += speed;
    }

    public void drawDemo() { 
        String content = "PLAYER1 : "+player1.getScore();
        String content1 = "PLAYER2 : "+player2.getScore();
        Font stringFont = new Font( "SansSerif", Font.PLAIN, 18 ); 
        g2.setFont( stringFont ); 
        g2.setColor(Color.white);
        
            if(isBossDied == true){
                stringFont = new Font( "SansSerif", Font.PLAIN, 25 ); 
                g2.setFont( stringFont ); 
                drawBackGroundWithTileImage();
                g2.drawString(" YOU WIN!", 220, 150);
                g2.drawString(content, 200, 220);
                g2.drawString(content1, 200, 250);
            }else if(!player1.isAlive() && !player2.isAlive()){
                stringFont = new Font( "SansSerif", Font.PLAIN, 25 ); 
                g2.setFont( stringFont );
                drawBackGroundWithTileImage();
                g2.drawString("GAME OVER! YOU LOSE!", 170, 150);
                g2.drawString(content, 200, 220);
                g2.drawString(content1, 200, 250);
            }
            else{
            I1.update();
            I2.update();
            I3.update();
            //check collision
            
            CD.playerVSenemy(player1,player2);
            CD.playerBulletVSenemyPlane(player1,player2);
            CD.playerVSenemyBullet(player1, player2);
            CD.playerVSpowerup(player1, player2);
            this.timelineControl();
            
            for(int i = 0; i < enemyl.size(); i++){
                enemyl.get(i).update();
            }
            for(int i = 0; i < enemybl.size(); i++){
                enemybl.get(i).update(w,h);
            }
            for(int i = 0; i < powerUp.size(); i++){
                powerUp.get(i).update();
            }
            //update player1's bullet list
            for(int i = 0; i < player1.getPlane().getBulletList().size(); i++){
                if((player1.getPlane().getBulletList().get(i)).getShow())
                    player1.getPlane().getBulletList().get(i).update(w,h);
                else
                    player1.getPlane().getBulletList().remove(i);
            }
            //update player2's bullet list
            for(int i = 0; i < player2.getPlane().getBulletList().size(); i++){
                if((player2.getPlane().getBulletList().get(i)).getShow())
                    player2.getPlane().getBulletList().get(i).update(w,h);
                else
                    player2.getPlane().getBulletList().remove(i);
            }
            for(int i = 0; i< explosions.size(); i++){
                if(explosions.get(i).getFinished()) {
                    explosions.remove(i);
                    i --;
                }
                else{
                    explosions.get(i).update();
                }
            }
            
            drawBackGroundWithTileImage();
            I1.draw(g2,this);
            I2.draw(g2,this);
            I3.draw(g2,this);
            player1.getPlane().draw(g2,this);
            player2.getPlane().draw(g2, this);
            
            for(int i = 0; i < enemyl.size(); i++){
                enemyl.get(i).draw(g2,this);
            }
            for(int i = 0; i < enemybl.size();i++){
                enemybl.get(i).draw(g2, this);
            }
            for(int i = 0; i < powerUp.size();i++){
                powerUp.get(i).draw(g2, this);
            }
            for(int i = 0; i < player1.getPlane().getBulletList().size(); i++){
                player1.getPlane().getBulletList().get(i).draw(g2,this);
            }
            for(int i = 0; i < player2.getPlane().getBulletList().size(); i++){
                player2.getPlane().getBulletList().get(i).draw(g2,this);
            }
            
            for(int i = 0; i < explosions.size(); i++){
                explosions.get(i).draw(g2, this);
            }
            //sb1.draw(g2, this);
            //sb2.draw(g2, this);
            
            g2.drawString(content, 2, 15);
            g2.drawString(content1, 2, 30);
            }
            //increment frameCounter
            frameCount ++;
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
        final GameWorld demo = new GameWorld();
        demo.init();
        JFrame f = new JFrame("Wingman");
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
