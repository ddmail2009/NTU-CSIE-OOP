package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Loader.MapLoader;


public class LWC_MapLoadEffect extends LWC_AbstractEffect implements LWC_Effect2Null {
    private MapLoader loader = null;
    
    public LWC_MapLoadEffect(LWC_RPG rpg) {
        super(rpg, "CHBckground");
    }
    
    public void setConfig(MapLoader loader){
        this.loader = loader;
    }

    @Override
    public void act() {
        if(loader != null) {
            rpg.setMap(loader);
//            loader.enter(rpg, rpg.getPlayer());
//            loader.load(rpg);
        }
    }
}
