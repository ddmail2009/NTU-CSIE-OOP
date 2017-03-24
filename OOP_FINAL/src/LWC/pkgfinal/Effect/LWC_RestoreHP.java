/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Object.LWC_AbstractObject;

public class LWC_RestoreHP extends LWC_AbstractEffect implements LWC_Effect2Object{
    private LWC_AbstractObject obj;
    
    public LWC_RestoreHP(LWC_RPG rpg) {
        super(rpg, "restoreHP");
    }

    @Override
    public void act() {
        if(obj instanceof LWC_AbstractCharacter)
            ((LWC_AbstractCharacter)obj).setHP(((LWC_AbstractCharacter)obj).getHP() + 10);
        else
            System.err.println("Can't restore HP on Obj which isn't LWC_Character");
    }

    @Override
    public void setObj(LWC_AbstractObject obj) {
        this.obj = obj;
    }
    
}
