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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Observer;
import java.util.Observable;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PlayerPlane extends GameObject implements Observer{
   
    private int health,damage,bulletDamage;
    private int up,down,left,right,fire;
    private int lifeCount;
    private boolean boom;
    private Image bulletImg, leftBulletImg, rightBulletImg,lifeImg;
    private ArrayList <Bullet> myBulletList;
    private Image [] healthBars, healthImg;
    private GameObject healthbar;
    String soundFileName;
    SoundPlayer sp;
    
    PlayerPlane(Image img,int life, int damge,int x, int y, int Yspeed,int up, int down, int left, int right, int fire) {
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
         this.healthBars = new Image[4];
         this.soundFileName = "Resources/snd_explosion2.wav";
         this.sp = new SoundPlayer(2,soundFileName);
         this.lifeCount = life;
         try{
             bulletImg = ImageIO.read(PlayerPlane.class.getResource("Resources/bullet.png"));
             leftBulletImg = ImageIO.read(PlayerPlane.class.getResource("Resources/bulletLeft.png"));
             rightBulletImg = ImageIO.read(PlayerPlane.class.getResource("Resources/bulletRight.png"));
             healthBars[0] = ImageIO.read(PlayerPlane.class.getResource("Resources/health.png"));
             healthBars[1] = ImageIO.read(PlayerPlane.class.getResource("Resources/health1.png"));
             healthBars[2] = ImageIO.read(PlayerPlane.class.getResource("Resources/health2.png"));
             healthBars[3] = ImageIO.read(PlayerPlane.class.getResource("Resources/health3.png"));
             lifeImg = ImageIO.read(PlayerPlane.class.getResource("Resources/life.png"));
         }
         catch (Exception e) {
            System.out.print(e.getMessage() + "Player plane: no resources are found");
        }
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
         GameWorld.explosions.add(new Explosion(x,y,GameWorld.bigExp));
         System.out.println("player plane explodes");
         
         sp.play();
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
        if(!boom){
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
            for(int i = 0; i < lifeCount;i++){
                GameObject lifeObj = new GameObject(this.lifeImg,x+i*lifeImg.getWidth(null)-15,y+height+healthbar.getHeight(),Yspeed);
                lifeObj.draw(g, obs);
            }
        }
        
    }
    
     private  void fire(){
        Bullet playerb;
        playerb = new Bullet(bulletImg,x+width/3,y,bulletDamage,-1,-3);
        myBulletList.add(playerb);
        playerb = new Bullet(bulletImg,x+width/3,y,bulletDamage,0,-3);
        myBulletList.add(playerb);
        playerb = new Bullet(bulletImg,x+width/3,y,bulletDamage,1,-3);
        myBulletList.add(playerb);
        //System.out.println("fire!");
           
     }
    
     public void update(Observable obj, Object arg) {
         GameEvents ge = (GameEvents) arg;
             if(ge.type == 1) {
                KeyEvent e = (KeyEvent) ge.event;
                int keyCode = e.getKeyCode();
                //System.out.println(keyCode);
                //System.out.println(left + " " + right + " "+ up + " "+ down+ " "+fire);
                if( keyCode == left) {
                    if(x > 0)
                        x -= Yspeed;
                } 
                else if(keyCode == right){
                    if(x < 570)
                        x += Yspeed;
                }
                else if(keyCode == up){   
                    if(y > 0)
                        y -= Yspeed;
                }
                else if(keyCode == down){
                    if(y < 400)
                        y += Yspeed;
                }
                else if(keyCode == fire) {
                    fire(); 
                }
           
             }
             else if(ge.type == 2) {
                String msg = (String)ge.event;
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
    
        

