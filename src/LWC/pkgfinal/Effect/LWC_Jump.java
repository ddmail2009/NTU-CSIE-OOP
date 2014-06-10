package LWC.pkgfinal.Effect;
import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;

public class LWC_Jump extends LWC_AbstractEffect implements LWC_Effect2Object{
    protected int speed = 15;
    protected LWC_AbstractObject owner;
    
    @Override
    public void act() {
        LWC_Gravity g = ((LWC_Gravity)(owner.getRegister("Gravity")));
        if(g != null && g.error > 3 ) g.speed -= speed;
    }

    public LWC_Jump(LWC_RPG rpg, LWC_AbstractObject obj) {
        super(rpg, "Jump");
        setObj(obj);
    }

    @Override
    public void setObj(LWC_AbstractObject obj) {
        owner = obj;
    }
}
