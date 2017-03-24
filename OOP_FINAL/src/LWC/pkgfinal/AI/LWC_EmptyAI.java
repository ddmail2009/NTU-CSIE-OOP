/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.AI;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import java.awt.Point;


public class LWC_EmptyAI extends LWC_AbstractAI {
    public LWC_EmptyAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        super(rpg, obj);
    }

    @Override
    public Point move() {
        return new Point();
    }

    @Override
    public boolean AIaction() {
        if(obj.getHP() > 0) return true;
        else return false;
    }
    
}
