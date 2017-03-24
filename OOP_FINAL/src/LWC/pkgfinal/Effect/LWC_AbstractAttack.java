package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.*;


public abstract class LWC_AbstractAttack extends LWC_AbstractAnimate{
    protected LWC_AbstractCharacter user = null;
    
    public LWC_AbstractAttack(LWC_RPG rpg, String secret) {
        super(rpg, secret);
    }
    
    public void setUser(LWC_AbstractCharacter user){
        this.user = user;
    }
    
    @Override
    protected boolean effect_update(){
        if(user == null && rpg.getPosition(user) != null) {
            System.err.println("Attack Must have a User");
            return false;
        }
        return attack_update();
    }
    
    protected abstract boolean attack_update();
}
