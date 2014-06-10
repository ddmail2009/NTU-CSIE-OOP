package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;

public class LWC_EffectHold extends LWC_AbstractEffect implements LWC_Effect2Object{
    private LWC_AbstractObject obj = null;
    
    public LWC_EffectHold(LWC_RPG rpg) {
        super(rpg, "EffHold");
    }

    @Override
    public void act() {
        if(obj != null)
            obj.setRegister("Hold", true);
        else
            System.err.println("EffectHold Doesn't Set Owner");
    }

    @Override
    public void setObj(LWC_AbstractObject obj) {
        this.obj = obj;
    }
    
}
