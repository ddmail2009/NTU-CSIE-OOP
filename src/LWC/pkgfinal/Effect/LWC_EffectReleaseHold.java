/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;


public class LWC_EffectReleaseHold extends LWC_AbstractEffect implements LWC_Effect2Object{
    private LWC_AbstractObject obj = null;
    public LWC_EffectReleaseHold(LWC_RPG rpg) {
        super(rpg, "EffRelHold");
    }

    @Override
    public void act() {
        if(obj != null)
            obj.clearRegister("Hold");
    }

    @Override
    public void setObj(LWC_AbstractObject obj) {
        this.obj = obj;
    }
    
}
