package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Object.*;
import java.awt.Point;
import org.json.simple.JSONObject;

public class LWC_TriggerMessage extends LWC_AbstractTrigger {
    private String msg = "I'm trapped, Please Help me";
    
    public LWC_TriggerMessage(LWC_RPG rpg) {
        super(rpg);
        setImage(rpg.imgPool().getImage("img/background/background1_trigger.png"));
    }
    
    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        LWC_FollowedMessage message = new LWC_FollowedMessage(rpg, obj);
        message.setString(msg);
        message.setPosition(new Point(obj.show().getWidth(), -obj.show().getHeight()));
        rpg.addEffect(message);
        rpg.removeTriggerOverlay(this);
        
        rpg.addEffect(new LWC_FadeOutEffect(rpg));
    }

    @Override
    public void config(JSONObject obj){
        super.config(obj);
        if(obj.containsKey("text"))
            msg = (String)obj.get("text");
    }
}

