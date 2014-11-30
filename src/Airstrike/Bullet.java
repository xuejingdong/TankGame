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

public class Bullet extends GameObject {
    int damage,Xspeed;
    boolean show;
    
    public Bullet(Image img, int x, int y, int damage, int Xspeed,int Yspeed){
        super(img,x,y,Yspeed);
        this.damage = damage;
        this.Xspeed = Xspeed;
        this.show = true;
    }
         
    public int getDamge(){
        return this.damage;
    }
    public boolean getShow(){
        return this.show;
    }
    public void setShow(boolean s){
        this.show = s;
    }
    public void update(){
        if(y < 430 && y > 0 && x > 0 && x < 600 && show){
            x = x + Xspeed;
            y = y + Yspeed;
        }
        else{
            this.show = false;
        }
    }
    public void draw(Graphics g,ImageObserver obs) {
        if(show)  
            g.drawImage(img, x, y, obs);
     }
}
