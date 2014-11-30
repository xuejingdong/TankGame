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
import java.awt.Rectangle;

public class PowerUp extends GameObject {
    int powerType;

    
    PowerUp(Image img, Random gen, int y, int speed,int powerTpye){
        super(img, Math.abs(gen.nextInt() % (600 - 30)),y,speed);
        this.powerType = powerType;
    }
    
    public int getPowerType(){
        return this.powerType;
    }
    
    
    public void update(){
        if(y > 430)
            GameWorld.powerUp.remove(this);
        else
            y = y + Yspeed;
    }
    
    public void draw(Graphics g, ImageObserver obs){
            g.drawImage(img, x, y, obs);
    
    }
}
