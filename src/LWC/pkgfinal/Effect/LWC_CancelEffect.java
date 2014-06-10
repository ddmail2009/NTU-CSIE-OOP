/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LWC_CancelEffect extends LWC_AbstractAnimate {
    private Point position = new Point();
    private int speed = 3;
    private Point destination;
    private LWC_AbstractObject player;
    private LWC_AbstractObject obj;
    private String state = null;
    
    public LWC_CancelEffect(LWC_RPG rpg) {
        super(rpg, "CancelEffect");
    }
    
    public void config(LWC_AbstractObject p, String state){
        this.player = p;
        this.state = state;
    }

    @Override
    protected boolean effect_update() {
        player.clearRegister(state);
        return false;
    }
}