package LWC.pkgfinal.Effect;
import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;

public abstract class LWC_AbstractStatus extends LWC_AbstractTime{
    protected int start_count = 1;
    protected LWC_AbstractObject owner;
    
    public LWC_AbstractStatus(LWC_RPG rpg, String secret, LWC_AbstractObject obj) {
        super(rpg, secret);
        owner = obj;
    } 
    
    public boolean lock(){
        if(owner.getRegister(secret) != null)return false;
        else owner.setRegister(secret, this);
        System.err.println(this + " Set Lock("+getSecret()+") for " + owner);
        return true;
    }
    
    public void unlock(){
        System.err.println(this + " UnLock Lock("+getSecret()+") for " + owner);
        owner.clearRegister(secret);
    }
    
    @Override
    protected boolean effect_update(){
        if(counter == start_count){
            if(lock() == false) return false;
            else return true;
        }
        if(status_update() == false) {
            unlock();
            return false;
        }
        return true;
    }

    protected abstract boolean status_update();
}
