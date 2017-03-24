package LWC.pkgfinal.Item;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Object.LWC_AbstractObject;

import org.json.simple.*;

public class LWC_RestoreItem extends LWC_BaseItem implements LWC_ConsumableItem{
    private LWC_AbstractObject obj;
    private int restoreMP = 0;
    private int restoreHP = 0;

    public LWC_RestoreItem(LWC_RPG rpg, String name, String description) {
        super(rpg, name, description);
    }
    
    @Override 
    public void config(JSONObject obj){
        super.config(obj);
        if(obj.containsKey("MP")) restoreMP = ((Long)obj.get("MP")).intValue();
        if(obj.containsKey("HP")) restoreHP = ((Long)obj.get("HP")).intValue();
    }

    @Override
    public void use() {
        ((LWC_AbstractCharacter)obj).setMP(((LWC_AbstractCharacter)obj).getMP() + restoreMP);
        ((LWC_AbstractCharacter)obj).setHP(((LWC_AbstractCharacter)obj).getHP() + restoreHP);
    }

    @Override
    public void setObj(LWC_AbstractObject obj) {
        this.obj = obj;
    }
}
