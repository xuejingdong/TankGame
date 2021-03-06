/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Dong
 */

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Wall extends GameObject{
    private boolean shootable;
    private boolean beShoot;
    private int disappearTime = 150;
    private int count;
    private int health;
    private String soundFileName;
    private SoundPlayer sp;
    
    Wall(Image img, int x, int y, int speed, boolean shootable){
        super(img,x,y,speed);
        this.shootable = shootable;
        this.beShoot = false;
        this.count = 0;
        this.health = 10;
        
    }
    public boolean isDisapeared(){
        return (this.beShoot && this.shootable);//a wall is disappeared only when it is shootbale and gets shoot
    }
    public boolean getBeShoot(){
        return this.beShoot;
    }
    public boolean getShootable(){
        return this.shootable;
    }
    public void setBeShoot(boolean s){
        this.soundFileName = "TankResources/Explosion_small.wav";
        this.sp = new SoundPlayer(2,soundFileName);
        TankGame.explosion.add(new TankGameExplosion(x,y,6,TankGame.smallExp));
        sp.play();
        this.beShoot = s;
        this.count = 0;//set count as 0 if the wall has bean shoot
    }
    public void reduceHealth(int h){
        health -= h;
    }
    
    public void update(){
        if(this.beShoot == true && this.count < this.disappearTime){
            this.count++;//update the diappear time
        }
        else{
            this.beShoot = false;
            this.count = 0;
        }
    }
    
    public void draw(Graphics g, ImageObserver obs){
        if(this.isDisapeared() == false) 
            g.drawImage(img, x, y, obs);
    
    }
    
    
}
