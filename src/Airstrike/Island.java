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
public class Island extends GameObject{
    protected Random gen;

    public Island(Image img, int x, int y, int speed, Random gen) {
        super(img,x,y,speed);
        this.gen = gen;
    }

     public void update() {
         y += Yspeed;
         if (y >= 480) {
             y = -100;
             x = Math.abs(gen.nextInt() % (640 - 30));
         }
     }

     public void draw(Graphics g,ImageObserver obs) {
          g.drawImage(img, x, y, obs);
     }
    
}
