/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Dong
 */
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
            this.gameEvent1.setValue("Collision" + " "+ 50);
            this.gameEvent2.setValue("Collision" + " "+ 50);
            //System.out.println("tank1 and tank2 collision");
        }
    }
    public void TankBulletVSWall(Tank tank1, Tank tank2){
        TankBullet bullet;
        Wall wall;
        ArrayList<TankBullet> tank1Bullet = tank1.getBulletList();
        ArrayList<TankBullet> tank2Bullet = tank2.getBulletList();
        for(int i  = 0; i < tank1Bullet.size(); i++){
            bullet = tank1Bullet.get(i);
            Rectangle bulletBox = new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            for(int j = 0; j < TankGame.wall_list.size(); j++){
                wall = TankGame.wall_list.get(j);
                Rectangle wallBox = new Rectangle(wall.getX(),wall.getY(),wall.getWidth(),wall.getHeight());
                //check collision, 
                if(bulletBox.intersects(wallBox)&&!tank1.getBoom()&& !wall.isDisapeared()){
                    tank1Bullet.remove(bullet);//remove bullet from list 
                    if(wall.getShootable())
                        wall.setBeShoot(true);
                    //System.out.println("tank1 bullet & wall collision hanppens" + bullet.getWidth());
                }
            }
        }
        for(int i  = 0; i < tank2Bullet.size(); i++){
            bullet = tank2Bullet.get(i);
            Rectangle bulletBox = new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            for(int j = 0; j < TankGame.wall_list.size(); j++){
                wall = TankGame.wall_list.get(j);
                Rectangle wallBox = new Rectangle(wall.getX(),wall.getY(),wall.getWidth(),wall.getHeight());
                //check collision, 
                if(bulletBox.intersects(wallBox)&&!tank2.getBoom() && !wall.isDisapeared()){
                    tank2Bullet.remove(bullet);//remove bullet from list 
                    if(wall.getShootable())
                        wall.setBeShoot(true);
                }
            }
        }
    }
    
    public void TankVSTankBullet(Tank tank1, Tank tank2){
        Bullet bullet;
        Rectangle tank1Box = new Rectangle(tank1.getX(), tank1.getY(), tank1.getWidth(), tank1.getHeight());
        Rectangle tank2Box = new Rectangle(tank2.getX(), tank2.getY(), tank2.getWidth(), tank2.getHeight());
        ArrayList<TankBullet> tank1Bullet = tank1.getBulletList();
        ArrayList<TankBullet> tank2Bullet = tank2.getBulletList();
        
        for(int i = 0; i < tank2Bullet.size(); i++){//check collision between tank1 with the bullets of tank2
            bullet = tank2Bullet.get(i);
            Rectangle bullet2Box= new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            if(tank1Box.intersects(bullet2Box) && !tank1.getBoom()){
                //System.out.println("tank1 and tank2 bullet Collision");
                if(!tank1.isInShield()){//send collision msg only when tank1 is not in shield
                    this.gameEvent1.setValue("Collision"+ " "+ bullet.getDamge());//update the overable
                    tank2.addScore(bullet.getDamge());
                }
                tank2Bullet.remove(i);//remove this bullet from the list
            }
        }
        
        for(int i = 0; i < tank1Bullet.size(); i++){//check collision between tank2 with the bullets of tank1
            bullet = tank1Bullet.get(i);
            Rectangle bullet1Box= new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            if(tank2Box.intersects(bullet1Box) && !tank2.getBoom()){
                if(!tank2.isInShield()){//send collision msg only when tank2 is not in shield
                    this.gameEvent2.setValue("Collision"+ " "+ bullet.getDamge());//update the overable
                    tank1.addScore(bullet.getDamge());
                }
                tank1Bullet.remove(i);//remove this bullet from the list
            }
        }
    }
    
    public void TankVSPowerUp(Tank tank1, Tank tank2){
        TankGamePowerUp power;
        Rectangle tank1Box = new Rectangle(tank1.getX(), tank1.getY(), tank1.getWidth(), tank1.getHeight());
        Rectangle tank2Box = new Rectangle(tank2.getX(), tank2.getY(), tank2.getWidth(), tank2.getHeight());
        //check collosion between power up and tank1
        for(int i = 0; i < TankGame.powerup.size(); i++){
            power = TankGame.powerup.get(i);
            Rectangle powerBox = new Rectangle(power.getX(),power.getY(),power.getWidth(),power.getHeight());
            if(powerBox.intersects(tank1Box) && !tank1.getBoom() && power.getShow()){
                power.setCollected(true);
                this.gameEvent1.setValue("PowerUp"+" " + power.getType());
            }
            if(powerBox.intersects(tank2Box) && !tank2.getBoom() && power.getShow()){
                power.setCollected(true);
                this.gameEvent2.setValue("PowerUp"+" " + power.getType());
            }
        }
        
    }
}
