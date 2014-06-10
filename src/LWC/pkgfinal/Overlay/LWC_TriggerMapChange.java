package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Loader.MapLoader;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;
import org.json.simple.JSONObject;

public class LWC_TriggerMapChange extends LWC_AbstractTrigger {
    private MapLoader loader;
    public LWC_TriggerMapChange(LWC_RPG rpg) {
        super(rpg);
        setImage(rpg.imgPool().getImage("img/background/background1_trigger2.png"));
    }
    
    @Override
    public void config(JSONObject obj){
        super.config(obj);
        if(obj.containsKey("To"))
            this.loader = new MapLoader((String) obj.get("To"));
    }

    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        LWC_FadeInAndOut inAndOut = new LWC_FadeInAndOut(rpg);
        LWC_MapLoadEffect mapLoadEffect = new LWC_MapLoadEffect(rpg);
        mapLoadEffect.setConfig(loader);
        inAndOut.addEffect(mapLoadEffect);
        
        
        LWC_OrderedEffect order = new LWC_OrderedEffect(rpg);
        order.addEffect(new LWC_EffectHold(rpg));
        order.addEffect(inAndOut);
        order.addEffect(new LWC_EffectReleaseHold(rpg));
        
        rpg.addEffect(order);
        rpg.removeTriggerOverlay(this);
    }    
}
