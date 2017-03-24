package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.AI.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import org.json.simple.JSONObject;


public class LWC_TriggerGrab extends LWC_AbstractTrigger {
    private HashMap<LWC_Player, LWC_AbstractAI> ai = new HashMap<>();
    
    public LWC_TriggerGrab(LWC_RPG rpg) {
        super(rpg);
        setImage(rpg.imgPool().getImage("img/background/background1[grab].png"));
    }

    
    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        if(obj == rpg.getPlayer() && obj.getRegister("Grab") == null){
            LWC_Player p = (LWC_Player) obj;
            
            Dimension imgSize = LWC_Util.getImgDim(obj.show());
            Point objPoint = rpg.getPosition(obj);
            if((rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_UP) && isTriggered(new Rectangle(objPoint.x, objPoint.y - imgSize.height/2, imgSize.width, imgSize.height)))){
                p.setRegister("Grab", p.setAI(new LWC_GrabbingAI(rpg, p)));
                ((LWC_Gravity)p.getRegister("Gravity")).stop(true);
            }
            else if(rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_DOWN) && isTriggered(new Rectangle(objPoint.x, objPoint.y + imgSize.height/2, imgSize.width, imgSize.height))){
                p.setRegister("Grab", p.setAI(new LWC_GrabbingAI(rpg, p)));
                ((LWC_Gravity)p.getRegister("Gravity")).stop(true);
            }
        }
    }

    @Override   
    public void ignore(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector){
        if(obj instanceof LWC_Player && obj.getRegister("Grab") != null){
            LWC_Player p = (LWC_Player)obj;
            p.setAI((LWC_AbstractAI) p.getRegister("Grab"));
            p.clearRegister("Grab");
            ((LWC_Gravity)p.getRegister("Gravity")).stop(false);
        }
    }    
}
