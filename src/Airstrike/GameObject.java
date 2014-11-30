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

public class GameObject {
    protected int x, y, width, height, Yspeed; 
    protected Image img;
    
    public GameObject(Image img, int x, int y, int Yspeed){
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        this.Yspeed = Yspeed;
        
    }
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void setX(int a){
        this.x = a;
    }
    
     public void setY(int b){
        this.y = b;
    }
     public void draw(Graphics g, ImageObserver obs){
         g.drawImage(img, x, y, obs);
    
    }
}
