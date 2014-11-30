/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airstrike;

/**
 *
 * @author Dong
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;
public class Player {
    private int life,ID, score;
    private PlayerPlane myPlane;
    private int up,down, left, right, fire;
    private int playerPlaneDamage = 10;
    private Image planeImg;
    
    public Player(int id, int life, int up, int down, int left, int right, int fire){
        this.life = life;
        this.ID = id;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.fire = fire;
        this.score = 0;
        try{
             planeImg = ImageIO.read(Player.class.getResource("Resources/myplane_1.png"));
             
         }
         catch (Exception e) {
            System.out.print("No resources are found in Player Class");
        }
        
         myPlane = new PlayerPlane(planeImg,this.life,playerPlaneDamage,250*ID,360,5,up,down,left,right,fire);
        
        
        
    }
    
    public int getScore(){
        return this.score;
    }
    
    public int getPlayerID(){
        return this.ID;
    }
    public PlayerPlane getPlane(){
        return this.myPlane;  
    }
    public boolean isAlive(){
        return !myPlane.getBoom();
    }
    
    public void addScore(int s){
        this.score += s;
    }
    
}
