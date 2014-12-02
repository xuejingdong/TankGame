/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Dong
 */
import Airstrike.*;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TankCollisionDetector{
    GameEvents gameEvent1, gameEvent2;
    
    TankCollisionDetector(GameEvents ge1, GameEvents ge2){
        this.gameEvent1 = ge1;
        this.gameEvent2 = ge2;
    }
   
    public void TankVSTank(Tank tank1, Tank tank2){
        Rectangle tank1Box = new Rectangle(tank1.getX(), tank1.getY(), tank1.getWidth(), tank1.getHeight());
        Rectangle tank2Box = new Rectangle(tank2.getX(), tank2.getY(), tank2.getWidth(), tank2.getHeight());
        if(tank1Box.intersects(tank2Box)){
            this.gameEvent1.setValue("Tank Collision");
            this.gameEvent2.setValue("Tank Collision");
            System.out.println("tank1 and tank2 collision");
        }
    }
    
    public void TankVSTankBullet(Tank tank1, Tank tank2){
        Bullet bullet;
        Rectangle tank1Box = new Rectangle(tank1.getX(), tank1.getY(), tank1.getWidth(), tank1.getHeight());
        Rectangle tank2Box = new Rectangle(tank2.getX(), tank2.getY(), tank2.getWidth(), tank2.getHeight());
        ArrayList<Bullet> tank1Bullet = tank1.getBulletList();
        ArrayList<Bullet> tank2Bullet = tank2.getBulletList();
        
        for(int i = 0; i < tank2Bullet.size(); i++){//check collision between tank1 with the bullets of tank2
            bullet = tank2Bullet.get(i);
            Rectangle bullet2Box= new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            if(tank1Box.intersects(bullet2Box) && !tank1.getBoom()){
                this.gameEvent1.setValue("Collision"+ " "+ bullet.getDamge());//update the overable
                tank2Bullet.remove(i);//remove this bullet from the list
            }
        }
        
        for(int i = 0; i < tank1Bullet.size(); i++){//check collision between tank2 with the bullets of tank1
            bullet = tank1Bullet.get(i);
            Rectangle bullet1Box= new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            if(tank2Box.intersects(bullet1Box) && !tank2.getBoom()){
                this.gameEvent2.setValue("Collision"+ " "+ bullet.getDamge());//update the overable
                tank1Bullet.remove(i);//remove this bullet from the list
            }
        }
    }
}
