package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Overlay.*;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class LWC_ParallelEffect extends LWC_AbstractTime {
    private ArrayList<LWC_AbstractEffect> list = new ArrayList<>();
    private LWC_AbstractEffectInterface monitor = null;
    
    public LWC_ParallelEffect(LWC_RPG rpg) {
        super(rpg, "Parallel");
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
        //System.out.println("counter:"+counter+"effectsize:"+list.size());
        if(counter == 1){
            if(monitor == null){
                for(int i=0;i<list.size();i++){
                    rpg.addEffect(list.get(i));
                }                
            }
            else{
                for(int i=0;i<list.size();i++){
                    monitor.addEffect(list.get(i));
                }                
            }
        }
        if(monitor == null){
            for(int i=0;i<list.size();i++){
                if(rpg.hasEffect(list.get(i)))
                    return true;
            }
        }
        else{
            for(int i=0;i<list.size();i++){
                if(monitor.hasEffect(list.get(i)))
                    return true;
            }
        }
        return false;
    }
}
