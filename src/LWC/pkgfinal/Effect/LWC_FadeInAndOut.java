package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Overlay.*;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class LWC_FadeInAndOut extends LWC_OrderedEffect {
    public LWC_FadeInAndOut(LWC_RPG rpg) {
        super(rpg);
        LWC_FadeOutEffect fadeOutEffect = new LWC_FadeOutEffect(rpg);
        LWC_InterfaceEmpty empty = fadeOutEffect.getInterface();
        LWC_FadeInEffect fadeInEffect = new LWC_FadeInEffect(rpg);
        fadeInEffect.setInterface(empty);
        list.add(fadeOutEffect);
        list.add(fadeInEffect);
    }
    
    public LWC_FadeOutEffect getFadeOut(){
        return (LWC_FadeOutEffect) list.get(1);
    }
    
    public LWC_FadeInEffect getFadein(){
        return (LWC_FadeInEffect) list.get(list.size()-1);
    }
    
    @Override
    public void addEffect(LWC_AbstractEffect effect){
        LWC_AbstractEffect fadein = list.get(list.size()-1);
        list.remove(list.size()-1);
        list.add(effect);
        list.add(fadein);
    }
}
