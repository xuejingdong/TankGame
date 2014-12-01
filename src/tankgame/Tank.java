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

public class Tank extends GameObject implements Observer{
    private int health,damage,bulletDamage;
    private int up,down,left,right,fire;
    private int lifeCount;
    private boolean boom;
    private Image bulletImg;
    private ArrayList <Bullet> myBulletList;
    //private Image [] healthBars, healthImg;
    //private GameObject healthbar;
    private String soundFileName;
    private SoundPlayer sp;
    private int currentSub;
    
    Tank(Image img,int life, int x, int y, int Yspeed, int up, int down, int left, int right, int fire){
        super(img,x,y,Yspeed);
        health = 200;
         this.damage = damage;
         this.bulletDamage = 4;
         this.up = up;
         this.down = down;
         this.left = left;
         this.right = right;
         this.fire = fire;
         this.boom = false;
         this.myBulletList = new ArrayList<Bullet>();
         //this.healthBars = new Image[4];
         this.currentSub = 0;
    }
    public int getDamage(){
         return this.damage;
     }
     
     public ArrayList<Bullet> getBulletList(){
         return this.myBulletList;
     }
     
     public boolean getBoom(){
         return this.boom;
     }
     public void reduceHealth(int d){
         if(health < d)
             this.isDied();
         this.health -= d;
         
     }
     public void addHealth(int h){
         this.health += h;
     }
     public void isDied(){
         //GameWorld.explosions.add(new Explosion(x,y,GameWorld.bigExp));
         System.out.println("player plane explodes");
         
         //sp.play();
         if(this.lifeCount >1){
            lifeCount --;
            this.health = 200;
            this.x = 200;
            this.y = 360;
         }
         else{
             this.x = 480;
             this.y = 500;
             boom = true;
         }
         
     }
     public void draw(Graphics g, ImageObserver obs){
        /*if(!boom){
            g.drawImage(img, x, y, obs);
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
        }*/
         g.drawImage(img, x, y, x+64, y+64, 64*currentSub, 0, 64*currentSub+64, 64, obs);
         //System.out.println("drawing tank!");
    }
    
     private  void fire(){
        Bullet playerb;
        playerb = new Bullet(bulletImg,x+width/3,y,bulletDamage,0,-3);
        myBulletList.add(playerb);
    }
    
     public void update(Observable obj, Object arg) {
         GameEvents ge = (GameEvents)arg;
             if(ge.getType() == 1) {
                KeyEvent e = (KeyEvent)ge.getEvent();
                int keyCode = e.getKeyCode();
                //System.out.println(keyCode);
                //System.out.println(left + " " + right + " "+ up + " "+ down+ " "+fire);
                if( keyCode == left) {
                    currentSub = (currentSub+1+60)%60;
                } 
                else if(keyCode == right){
                    currentSub = (currentSub-1+60)%60;
                }
                else if(keyCode == up){   
                    this.x = (int)(Math.cos(Math.toRadians(currentSub*6))*Yspeed+this.x);
                    this.y = (int)(-Math.sin(Math.toRadians(currentSub*6))*Yspeed + this.y);                    
                }
                else if(keyCode == down){
                    this.x = (int)(-Math.cos(Math.toRadians(currentSub*6))*Yspeed+this.x);
                    this.y = (int)(Math.sin(Math.toRadians(currentSub*6))*Yspeed + this.y);                    
                }
                else if(keyCode == fire) {
                    fire(); 
                }
           
             }
             else if(ge.getType() == 2) {
                String msg = (String)ge.getEvent();
                String[] msgArray = new String[2];
                StringTokenizer st = new StringTokenizer(msg);
                int i = 0;
                while (st.hasMoreTokens()) {
                    msgArray[i] = st.nextToken();
                    i++;
                }
                if(msgArray[0].equals("Collision")) {
                    int y = Integer.parseInt(msgArray[1]);
                    this.reduceHealth(y);
                    //System.out.println("Explosion! Reduce Health"+y);
                }
           }
       }
 }
    
        


