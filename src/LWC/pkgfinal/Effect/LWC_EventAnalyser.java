package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;

public class LWC_EventAnalyser extends LWC_AbstractTime {
    public LWC_EventAnalyser(LWC_RPG rpg, String secret) {
        super(rpg, secret);
    }

    @Override
    protected boolean effect_update() {
        
        return true;
    }
}
