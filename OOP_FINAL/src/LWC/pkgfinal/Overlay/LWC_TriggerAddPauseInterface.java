package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;
import LWC.pkgfinal.LWC_RPG;
import java.lang.reflect.Constructor;
import org.json.simple.JSONObject;


public class LWC_TriggerAddPauseInterface extends LWC_AbstractTrigger {
    private LWC_AbstractInterface inter = null;
    
    public LWC_TriggerAddPauseInterface(LWC_RPG rpg) {
        super(rpg);
    }
    
    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        rpg.addPauseInterface(inter);
        rpg.removeTriggerOverlay(this);
    }
    
    @Override
    public void config(JSONObject obj){
        super.config(obj);
        if(obj.containsKey("Type")){
            try{
                Constructor c = Class.forName((String)obj.get("Type")).getConstructor(LWC_RPG.class);
                inter = (LWC_AbstractInterface)c.newInstance(rpg);
            }catch(Exception e){
                System.err.println("Error create new instance of " + obj.get("Type"));
                e.printStackTrace();
            }
        }
    }
    
}
