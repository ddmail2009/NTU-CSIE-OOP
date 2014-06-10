package LWC.pkgfinal.Item;

import LWC.pkgfinal.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LWC_BaseWeapon extends LWC_BaseItem {
    protected int damage = 1;
    
    public LWC_BaseWeapon(LWC_RPG rpg, String name, String description) {
        super(rpg, name, description);
    }

    @Override 
    public void config(JSONObject obj){
        super.config(obj);
        if(obj.containsKey("Damage")) 
            damage = ((Long)obj.get("Damage")).intValue();
    }

    public int getDamage(){
        return damage;
    }
    public void setDamage(int damage){
        this.damage = damage;
    }
}
