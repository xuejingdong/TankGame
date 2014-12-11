/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Dong
 */
import Airstrike.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Observer;
import java.util.Observable;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tank extends GameObject implements Observer {

    private int health, damage, bulletDamage;
    private int up, down, left, right, fire;
    private int lifeCount;
    private boolean boom;
    private Image basicBulletStrip,bullet1Img,currentBulletStrip;
    private ArrayList<TankBullet> myBulletList;
    private Image [] healthBars, healthImg;
    private GameObject healthbar;
    private String soundFileName;
    private SoundPlayer sp;
    private int currentSub;
    private int score;

    Tank(Image img, int life, int x, int y, int Yspeed, int up, int down, int left, int right, int fire) {
        super(img, x, y, Yspeed);
        this.width = 64;
        health = 200;
        this.damage = damage;
        this.bulletDamage = 50;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.fire = fire;
        this.boom = false;
        this.myBulletList = new ArrayList<TankBullet>();
        this.healthBars = new Image[4];
        this.currentSub = 0;
        this.score = 0;
        
        try{
            this.basicBulletStrip = ImageIO.read(Tank.class.getResource("TankResources/Shell_basic_strip60.png"));
            this.healthBars[0] = ImageIO.read(Tank.class.getResource("TankResources/health.png"));
            this.healthBars[1] = ImageIO.read(Tank.class.getResource("TankResources/health1.png"));
            this.healthBars[2] = ImageIO.read(Tank.class.getResource("TankResources/health2.png"));
            this.healthBars[3] = ImageIO.read(Tank.class.getResource("TankResources/health3.png"));
        }
        catch(Exception e){
            System.out.println(e.getMessage()+ " Image not found in Tank class");
        }
        
    }

    public int getDamage() {
        return this.damage;
    }

    public ArrayList<TankBullet> getBulletList() {
        return this.myBulletList;
    }

    public boolean getBoom() {
        return this.boom;
    }

    public void reduceHealth(int d) {
        if (health < d) {
            this.isDied();
        }
        this.health -= d;

    }

    public void addHealth(int h) {
        this.health += h;
    }

    public void isDied() {
        TankGame.explosion.add(new TankGameExplosion(x,y,7,TankGame.bigExp));

        //sp.play();
        boom = true;
        
    }

    public void draw(Graphics g, ImageObserver obs) {
        if(!boom){
            g.drawImage(img, x, y, x + 64, y + 64, 64 * currentSub, 0, 64 * currentSub + 64, 64, obs);
            if(this.health >= 150){
                healthbar = new GameObject(healthBars[0],x,y+height,Yspeed);
                healthbar.draw(g, obs);
            }
            if(this.health < 150 && this.health >=100){
                healthbar = new GameObject(healthBars[1],x,y+height,Yspeed);
                healthbar.draw(g, obs);
            }
            if(this.health < 100 && this.health >=50){
                healthbar = new GameObject(healthBars[2],x,y+height,Yspeed);
                healthbar.draw(g, obs);
            }
            if(health < 50){
                healthbar = new GameObject(healthBars[3],x,y+height,Yspeed);
                healthbar.draw(g, obs);
            }
         }
        
        
    }

    private void fire() {
        if(!this.boom){
            TankBullet playerb;
            if(this.currentSub < 30){
                playerb = new TankBullet(basicBulletStrip, x + width / 3, y-5, bulletDamage, 
                    (int) (Math.cos(Math.toRadians(currentSub * 6)) * 20 ), (int) (-Math.sin(Math.toRadians(currentSub * 6)) * 20),this.currentSub);
            }
            else
            {
                playerb = new TankBullet(basicBulletStrip, x + width / 3, y+40, bulletDamage, 
                    (int) (Math.cos(Math.toRadians(currentSub * 6)) * 20 ), (int) (-Math.sin(Math.toRadians(currentSub * 6)) * 20),this.currentSub);
            } 
             myBulletList.add(playerb);
        }
    }

    public void update(Observable obj, Object arg) {
        GameEvents ge = (GameEvents) arg;
        if (ge.getType() == 1) {
            KeyEvent e = (KeyEvent) ge.getEvent();
            int keyCode = e.getKeyCode();
            int futureX =this.x, futureY = this.y;

            if (keyCode == left) {
                currentSub = (currentSub + 1 + 60) % 60;
            } else if (keyCode == right) {
                currentSub = (currentSub - 1 + 60) % 60;
            } else if (keyCode == fire) {
                fire();
                //System.out.println("Firing");
            } else {

                if (keyCode == up) {
                    futureX = (int) (Math.cos(Math.toRadians(currentSub * 6)) * Yspeed + this.x);
                    futureY = (int) (-Math.sin(Math.toRadians(currentSub * 6)) * Yspeed + this.y);
                } else if (keyCode == down) {
                    futureX = (int) (-Math.cos(Math.toRadians(currentSub * 6)) * Yspeed + this.x);
                    futureY = (int) (Math.sin(Math.toRadians(currentSub * 6)) * Yspeed + this.y);
                }
                //check collision before tank makes the next movement
                boolean isCls = false;
                Wall wall;
                Rectangle tankBox = new Rectangle(futureX, futureY, this.getWidth(), this.getHeight());
                for (int i = 0; i < TankGame.wall_list.size(); i++) {
                    wall = TankGame.wall_list.get(i);//get wall in the array
                    Rectangle wallBox = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());//compute the rectagle of the wall
                    if (tankBox.intersects(wallBox)&& !wall.isDisapeared()) {//check collision between one tank and one wall
                        isCls = true;
                    }
                }
             
                if (!isCls) {
                    this.x = futureX;
                    this.y = futureY;
                }
            }
            
        } else if (ge.getType() == 2) {
            String msg = (String) ge.getEvent();

            String[] msgArray = new String[2];
            StringTokenizer st = new StringTokenizer(msg);
            int j = 0;
            while (st.hasMoreTokens()) {
                msgArray[j] = st.nextToken();
                j++;
            }
            if (msgArray[0].equals("Collision")) {//handle different types of collision here
                int y = Integer.parseInt(msgArray[1]);
                this.reduceHealth(y);
                //System.out.println("Explosion! Reduce Health"+y);
            }

        }
    }
}
