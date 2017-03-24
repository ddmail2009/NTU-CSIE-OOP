/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LWC_MoveAnimate extends LWC_AbstractAnimate {
    private Point position = new Point();
    private int speed = 3;
    private Point destination;
    private LWC_AbstractObject player;
    private LWC_AbstractObject obj;
    private String movestate = null;
    
    public LWC_MoveAnimate(LWC_RPG rpg) {
        super(rpg, "MoveAnimate");
    }
    
    public void config(LWC_AbstractObject p, int speed, Point dest, String movestate){
        this.player = p;
        this.speed = speed;
        this.destination = dest;
        setDuration((int)destination.getX()/speed);
        this.movestate = movestate;
    }

    public void config(LWC_AbstractObject p, int speed, Point dest){
        this.player = p;
        this.speed = speed;
        this.destination = dest;
        setDuration((int)destination.getX()/speed);
        this.movestate = movestate;
    }

    @Override
    protected boolean effect_update() {
        if(counter >= getDuration()){
            //player.clearRegister(movestate);
            return false;
        }
        else{
            if(movestate!=null){
                player.setCounter("Character", player.getCounter("Character")+1);
                player.setRegister(movestate,player);
            }
            rpg.movePosition(player, new Point (speed,0), false);
            
        }
        return true;
    }
}