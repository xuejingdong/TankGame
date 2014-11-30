/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airstrike;

/**
 *
 * @author Dong
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Explosion{
    private Image [] img;
    private int x,y;
    private int count;
    private boolean finished;
    private int type;//1 for small explosions, the other number for big ones
    
    public Explosion(int x, int y, Image[] img){
        this.x = x;
        this.y = y;
        this.count = 0;
        this. finished = false;
        this.img = img;
    }
    
    public boolean getFinished(){
        return this.finished;
    }
    public void update(){

        if(count < img.length-1){
            count ++;
        }else{
            finished = true;
        }
    }
    
    public void draw(Graphics g,ImageObserver obs) {
         if (!finished) {
             g.drawImage(img[count-1], x, y, obs);
         }
     }
}
