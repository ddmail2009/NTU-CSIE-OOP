package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.Effect.LWC_AbstractEffect;
import LWC.pkgfinal.Effect.LWC_AbstractTime;
import LWC.pkgfinal.Effect.LWC_Effect2Null;
import LWC.pkgfinal.Effect.LWC_Effect2Object;
import LWC.pkgfinal.LWC_RPG;
import java.util.ArrayList;

public abstract class LWC_AbstractEffectInterface extends LWC_AbstractInterface{
    protected ArrayList<LWC_AbstractEffect> effectList = new ArrayList<>();

    public LWC_AbstractEffectInterface(LWC_RPG rpg) {
        super(rpg);
    }
    
    public void addEffect(LWC_AbstractEffect effect){
        if(effect instanceof LWC_AbstractTime) 
            effectList.add((LWC_AbstractTime) effect);
        else if(effect instanceof LWC_Effect2Null)
            ((LWC_Effect2Null)effect).act();
        else if(effect instanceof LWC_Effect2Object) 
            ((LWC_Effect2Object)effect).act();
    }
    
    public boolean hasEffect(LWC_AbstractEffect effect){
        if(effectList.contains(effect)) return true;
        else return false;
    }
    
    @Override
    public boolean interface_update(){
        for(int i=effectList.size()-1; i>=0; i--){
            if(((LWC_AbstractTime)effectList.get(i)).update() == false)
                effectList.remove(i);
        }
        return effect_interface_update();
    }
    
    public abstract boolean effect_interface_update();
}