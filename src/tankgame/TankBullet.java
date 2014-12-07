/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Dong
 */
import Airstrike.Bullet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class TankBullet extends Bullet {
    private int currentSub;
    private int Xspeed;
    
    TankBullet(Image img, int x, int y, int damage, int Xspeed,int Yspeed, int currentSub){
        super(img,x,y,damage,Xspeed,Yspeed);
        this.currentSub = currentSub;
        this.Xspeed = Xspeed;
    }
    
    public void update(int w, int h){
        if(y < h-40 && y > 0 && x > 0 && x < w-40 && this.getShow()){
            this.x = this.x + Xspeed;
            this.y = this.y + Yspeed;
        }
        else{
            this.setShow(false);
        }
    }
    public void draw(Graphics g,ImageObserver obs) {
        if(this.getShow()){
            g.drawImage(img, x, y, x + this.height, y + this.height, this.height * currentSub, 0, this.height * currentSub + this.height, this.height, obs);
            //System.out.println(" bullet is drawing");
        }
    }
}
