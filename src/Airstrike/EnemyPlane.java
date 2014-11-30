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
import java.awt.image.ImageObserver;
import java.util.Random;

/**
 *
 * @author Dong
 */
public class EnemyPlane extends GameObject{
    private int damage,health;
    private Random gen;
    private boolean is_Died;
    private Random generator = new Random();
    private GameEvents gameEvents;
    private boolean canShoot;
    private boolean isBoss;
    private int shootFreq;
    private int direction;//0: from top, 1: from back
    private int enemyType;//1: green, 2: yellow, 3: white, 4: back, 5: BOSS
    private int bullet_type;//1: green &yellow; 2: white;
    private int bullet_damage;
    private Image bullet_image;
    private String soundFileName;
    private SoundPlayer sp;
   
    EnemyPlane(Image img,int enemyType,int health, int damage,int direction,int y, int Yspeed,Random gen,boolean canShoot, boolean isBoss){ 
       super(img, Math.abs(gen.nextInt() % (600 - 30)),y,Yspeed);
       this.enemyType = enemyType;
       this.health = health;
       this.gen = gen;
       this.is_Died = false;
       this.damage = damage;
       this.direction = direction;
       this.canShoot = canShoot;
       this.shootFreq = 0 ;
       this.soundFileName = "Resources/snd_explosion1.wav";
       this.sp = new SoundPlayer(2,soundFileName);
       this.isBoss = isBoss;
       if(enemyType == 1 || enemyType == 2){
           this.bullet_type = 1;
           this.bullet_damage = 3;
           this.bullet_image = GameWorld.enemyBulletSmall;
       }
       if(enemyType == 3 || enemyType == 5){
           this.bullet_type = 2;
           this.bullet_damage = 6;
           this.bullet_image = GameWorld.enemyBulletBig;
           //this.gameEvents = gameEvent;
       }    
       if(this.isBoss == true){
           this.x = 300;
       }
    }
    
    public int getDamage(){
        return this.damage;
    }
    
    public int getBulletDamage(){
        return this.getBulletDamage();
    }
   
     public void setDamage(int d){
         this.damage = d;
     }
     
     public void reduceHealth(int d){
         this.health -= d;
     }
     
     public void shoot(){
         if(shootFreq%15 == 0 && enemyType == 1){
            Bullet enemyb;
            enemyb = new Bullet(bullet_image,x-width/10,y+5,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
         }
         if(shootFreq% 40== 0 && enemyType == 2){
            Bullet enemyb;
            enemyb = new Bullet(bullet_image,x-width/10-1,y+5,bullet_damage,-1,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/10,y+5,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/10+1,y+5,bullet_damage,1,4);
            GameWorld.enemybl.add(enemyb);
         }
         if(shootFreq%30== 0 && enemyType ==3){
            Bullet enemyb;
            enemyb = new Bullet(bullet_image,x-width/10,y+5,bullet_damage,-1,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/10,y+5,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/10,y+5,bullet_damage,1,4);
            GameWorld.enemybl.add(enemyb);
         }
         if(shootFreq%15== 0 && enemyType == 5){
            Bullet enemyb;
            enemyb = new Bullet(bullet_image,x+width/4-10,y+height,bullet_damage,-1,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/4-5,y+height,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/4,y+height,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/4+5,y+height,bullet_damage,0,4);
            GameWorld.enemybl.add(enemyb);
            enemyb = new Bullet(bullet_image,x+width/4+10,y+height,bullet_damage,1,4);
            GameWorld.enemybl.add(enemyb);
         }
     }
    //update for enemy planes come from both directions 
    public void update() {
        if(canShoot)
            this.shoot();
        if(this.direction == 0){
            if(y > 430){
                this.reset();
            }
            if(this.health <= 0)
                isDied();
             y += Yspeed;
        } 
        else{
            if(y < 0){
               this.reset();
           }
           if(this.health <= 0)
               isDied();
           y += Yspeed;
        }
        shootFreq++;
    }
   
    //call explosion, and remove itself from the enemyPlane array
    public void isDied(){
        GameWorld.enemyl.remove(this);
        GameWorld.explosions.add(new Explosion(x,y,GameWorld.smallExp));
        sp.play();
        this.is_Died = true;
        if(this.isBoss)
            GameWorld.isBossDied = true;
        //System.out.println("enemy explosion");
    }
    
    public void reset() {
        if(this.direction == 0){
            this.x = Math.abs(generator.nextInt() % (600 - 30));
            this.y = -10;
        }
        else{
            this.x = Math.abs(generator.nextInt() % (600 - 30));
            this.y = 480;
        }
     }

     public void draw(Graphics g,ImageObserver obs) {
             g.drawImage(img, x, y, obs);
         
     }
}
