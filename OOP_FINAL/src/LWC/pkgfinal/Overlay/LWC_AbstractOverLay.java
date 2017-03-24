package LWC.pkgfinal.Overlay;
import LWC.pkgfinal.LWC_RPG;
import java.io.Serializable;
import org.json.simple.*;

/**
 * General Overlay Class
 */
public abstract class LWC_AbstractOverLay implements Serializable{
    protected LWC_RPG rpg;

    public LWC_AbstractOverLay(LWC_RPG rpg) {
        this.rpg = rpg;
    }
    public void config(JSONObject obj){
        ;
    }
}
