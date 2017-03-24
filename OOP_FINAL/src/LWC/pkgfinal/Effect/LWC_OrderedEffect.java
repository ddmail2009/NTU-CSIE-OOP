package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Overlay.*;
import java.util.ArrayList;


public class LWC_OrderedEffect extends LWC_AbstractTime {
    protected ArrayList<LWC_AbstractEffect> list = new ArrayList<>();
    protected LWC_AbstractEffectInterface monitor = null;
    
    public LWC_OrderedEffect(LWC_RPG rpg) {
        super(rpg, "Order");
        list.add(null);
    }

    public void addEffect(LWC_AbstractEffect effect){
        list.add(effect);
    }
    
    public void setMonitor(LWC_AbstractEffectInterface inter){
        monitor = inter;
    }
    
    @Override
    protected boolean effect_update() {
        if(monitor == null && !list.isEmpty()){
            if(!rpg.hasEffect(list.get(0))){
                list.remove(0);
                if(!list.isEmpty()){
                    if(list.get(0) instanceof LWC_OrderedEffect) 
                        ((LWC_OrderedEffect) list.get(0)).setMonitor(monitor);
                    rpg.addEffect(list.get(0));
                }
            }
        }
        else if(!list.isEmpty()){
            if(!monitor.hasEffect(list.get(0))){
                list.remove(0);
                if(!list.isEmpty()){
                    if(list.get(0) instanceof LWC_OrderedEffect) 
                        ((LWC_OrderedEffect) list.get(0)).setMonitor(monitor);
                    monitor.addEffect(list.get(0));
                }
            }
        }
        
        if(list.isEmpty()) 
            return false;
        else return true;
    }
}
