/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airstrike;

/**
 *
 * @author Dong
 */
import java.awt.Rectangle;
import java.util.ArrayList;
public class CollisionDetector {
    
    GameEvents gameEvent1, gameEvent2;
    
    public CollisionDetector(GameEvents ge1, GameEvents ge2){
        this.gameEvent1 = ge1;
        this.gameEvent2 = ge2;
    }
    
    public void playerVSplayer(PlayerPlane pp, PlayerPlane pp2){
        //check if there is intersection between 2 plane
        Rectangle pbox = new Rectangle(pp.getX(), pp.getY(), pp.getWidth(), pp.getHeight());
        Rectangle pbox2 = new Rectangle(pp2.getX(), pp2.getY(), pp2.getWidth(), pp2.getHeight());
        //if(pbox.intersects(pbox2))
           
    }
    public void playerVSenemy(Player player1,Player player2){
        EnemyPlane enemy;
        PlayerPlane p1 = player1.getPlane();
        PlayerPlane p2 = player2.getPlane();
        
        Rectangle p1_box = new Rectangle(p1.getX(), p1.getY(), p1.getWidth(), p1.getHeight());
        Rectangle p2_box = new Rectangle(p2.getX(), p2.getY(), p2.getWidth(), p2.getHeight());
        for(int i =0; i < GameWorld.enemyl.size(); i++){
            enemy = GameWorld.enemyl.get(i);
            Rectangle eBox = new Rectangle(enemy.getX(), enemy.getY(),enemy.getWidth(), enemy.getHeight());
            if(p1_box.intersects(eBox)&&!p1.getBoom()){//check intersection
                enemy.isDied();// update the enemy -> explosion
                gameEvent1.setValue("Collision"+" "+enemy.getDamage() ); // update player plane' health
                
            }
            if(p2_box.intersects(eBox)&&!p2.getBoom()){//check intersection
                enemy.isDied();// update the enemy -> explosion
                gameEvent2.setValue("Collision"+" "+enemy.getDamage()); // update player plane' health
            }
            
        }
    }
    public void playerVSenemyBullet(Player p1,Player p2){
        Bullet enemyBullet;
        PlayerPlane pp1 = p1.getPlane();
        PlayerPlane pp2 = p2.getPlane();
        Rectangle p1_box = new Rectangle(pp1.getX(), pp1.getY(), pp1.getWidth(), pp1.getHeight());
        Rectangle p2_box = new Rectangle(pp2.getX(), pp2.getY(), pp2.getWidth(), pp2.getHeight());
        for(int i =0; i < GameWorld.enemybl.size(); i++){
            enemyBullet = GameWorld.enemybl.get(i);
            Rectangle eBox = new Rectangle(enemyBullet.getX(), enemyBullet.getY(),enemyBullet.getWidth(), enemyBullet.getHeight());
            if(p1_box.intersects(eBox)&&!pp1.getBoom()){//check intersection
                gameEvent1.setValue("Collision"+" "+enemyBullet.getDamge()); // update player plane' health
                GameWorld.enemybl.remove(i);// remove this bullet
                //System.out.println("player VS enemy bullet");
            }
            if(p2_box.intersects(eBox)&&!pp2.getBoom()){//check intersection
                gameEvent2.setValue("Collision"+" "+enemyBullet.getDamge()); // update player plane' health
                GameWorld.enemybl.remove(i);// remove this bullet
                //System.out.println("player VS enemy bullet");
            }
        }
        
    }
    public void playerBulletVSenemyPlane(Player player1,Player player2){
        Bullet bullet;
        EnemyPlane enemy;
        ArrayList<Bullet> player1_bl = player1.getPlane().getBulletList();
        ArrayList<Bullet> player2_bl = player2.getPlane().getBulletList();
        for(int i  = 0; i < player1_bl.size(); i++){
            bullet = player1_bl.get(i);
            Rectangle bulletBox = new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            for(int j = 0; j < GameWorld.enemyl.size(); j++){
                enemy = GameWorld.enemyl.get(j);
                Rectangle enemyBox = new Rectangle(enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());
                //check collision, 
                if(bulletBox.intersects(enemyBox)&&!player1.getPlane().getBoom()){
                    player1_bl.remove(bullet);//remove bullet from list 
                    player1.addScore(enemy.getDamage());
                    enemy.reduceHealth(bullet.getDamge());//reduce enemy health
                }
            }
        }
        for(int i  = 0; i < player2_bl.size(); i++){
            bullet = player2_bl.get(i);
            Rectangle bulletBox = new Rectangle(bullet.getX(),bullet.getY(),bullet.getWidth(),bullet.getHeight());
            for(int j = 0; j < GameWorld.enemyl.size(); j++){
                enemy = GameWorld.enemyl.get(j);
                Rectangle enemyBox = new Rectangle(enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());
                //check collision, 
                if(bulletBox.intersects(enemyBox)&&!player2.getPlane().getBoom()){
                    player2_bl.remove(bullet);//remove bullet from list 
                    player2.addScore(enemy.getDamage());
                    enemy.reduceHealth(bullet.getDamge());//reduce enemy health
                }
            }
        }
    }
    public void playerVSpowerup(Player player1, Player player2){
        PlayerPlane playerPlane1 = player1.getPlane();
        PlayerPlane playerPlane2 = player2.getPlane();
        PowerUp power;
        Rectangle p1box = new Rectangle(playerPlane1.getX(), playerPlane1.getY(), playerPlane1.getWidth(), playerPlane1.getHeight());
        Rectangle p2box = new Rectangle(playerPlane2.getX(), playerPlane2.getY(), playerPlane2.getWidth(), playerPlane2.getHeight());
        for(int i =0; i < GameWorld.powerUp.size(); i++){
            power = GameWorld.powerUp.get(i);
            Rectangle puBox = new Rectangle(power.getX(), power.getY(),power.getWidth(), power.getHeight());
            if(p1box.intersects(puBox)&&!playerPlane1.getBoom()){//check intersection
                playerPlane1.addHealth(50);// update player plane' health
                GameWorld.powerUp.remove(i);// remove this bullet
            }
            if(p2box.intersects(puBox)&&!playerPlane2.getBoom()){//check intersection
                playerPlane2.addHealth(50);// update player plane' health
                GameWorld.powerUp.remove(i);// remove this bullet
            }
        }
            
     }
 }

