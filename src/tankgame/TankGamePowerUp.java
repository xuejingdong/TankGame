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
import java.awt.Rectangle;

public class TankGamePowerUp extends GameObject{
    private int type;
    private int appearDuration;
    private int disappearDuration;
    private int timeCount;
    private boolean isCollected;
    private boolean show;
    
    public TankGamePowerUp(Image img,int x, int y, int speed, int PowerType){
        super(img, x, y, speed);
        this.width = this.height;
        this.type = PowerType;
        this.appearDuration = 100*type;
        this.disappearDuration = 100*type;
        this.timeCount = 0;
        this.isCollected = false;
        this.show = true;
    }
    
    public int getType(){
        return this.type;
    }
    
    public void setCollected(boolean c){
        this.isCollected = c;
    }
    public boolean getShow(){
        return this.show;
    }
    
    public void update(){
        //update if the poweup is showed and not collected and still in appearduration
        if(this.show && !this.isCollected && timeCount < this.appearDuration){
            timeCount++;
        }
        //update if the appearduration is passed and the powerup still not be collected
        else if(this.show && !this.isCollected && timeCount >= this.appearDuration){
            this.show = false;
            timeCount = 0;
        }
        //update if the powerup is collected
        else if(this.show && this.isCollected){
            this.show = false;
            timeCount = 0;
        }
        //update when the powerup is not show on the game field
        else if(!this.show && timeCount < this.disappearDuration){
            timeCount++;
        }
        //update the powerup if it sleep longer enough after being collected or disappeared
        else if(!this.show && timeCount >= this.disappearDuration){
            this.show = true;
            this.isCollected= false;
            timeCount = 0;
        }
    }
    
    public void draw(Graphics g, ImageObserver obs){
        if(show){
            g.drawImage(img, x, y, x + img.getHeight(obs), y + img.getHeight(obs), img.getHeight(obs) * type, 0, img.getHeight(obs) * type +img.getHeight(obs), img.getHeight(obs), obs);
        }
    }
    
}
