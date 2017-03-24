/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;

/**
 * Set the entire Background from black to transparency
 */
public class LWC_FadeInEffect extends LWC_FadeOutEffect {    
    public LWC_FadeInEffect(LWC_RPG rpg) {
        super(rpg);
    }
    
    @Override
    protected boolean effect_update() {
        if(counter == 1 && !rpg.hasInterface(empty)) rpg.addInterface(empty);

        if(counter <= getDuration()){
            empty.setOpaque((float) (getDuration() - counter)/getDuration());
            return true;
        }
        else {
            empty.end();
            return false;
        }
    }
    
}
