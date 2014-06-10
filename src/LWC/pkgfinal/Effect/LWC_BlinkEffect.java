package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;

public class LWC_BlinkEffect extends LWC_AbstractStatus {
    private int blinkSpeed = 5;
    
    public LWC_BlinkEffect(LWC_RPG rpg, LWC_AbstractObject obj) {
        super(rpg, "Blink", obj);
    }
    
    public void setSpeed(int speed){
        blinkSpeed = speed;
    }
    
    @Override
    protected boolean status_update() {
        if(counter % blinkSpeed == 0){
            if(owner.getRegister("Hidden") == null) owner.setRegister("Hidden", true);
            else owner.clearRegister("Hidden");
        }
        
        if(counter > getDuration()){
            owner.clearRegister("Hidden");
            return false;
        }
        return true;
    }
}
