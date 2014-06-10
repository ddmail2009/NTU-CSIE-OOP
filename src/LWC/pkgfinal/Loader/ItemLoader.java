package LWC.pkgfinal.Loader;

import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.*;
import java.lang.reflect.*;
import org.json.simple.*;

public class ItemLoader extends LWC_Loader{  
    public ItemLoader(String files){
        super(files);
    }
    
    public LWC_BaseItem getItem(LWC_RPG rpg, String token){
        try{
            JSONObject obj = (JSONObject)json.get(token);;
            String type = (String)obj.get("type");
            Constructor c = Class.forName(type).getConstructor(LWC_RPG.class, String.class, String.class);
            LWC_BaseItem item = (LWC_BaseItem)c.newInstance(rpg, (String)obj.get("Name"), (String)obj.get("Description"));
            item.config(obj);
            return item;

        }catch (Exception e) {
            System.err.println("Error get Item: " + token);
            e.printStackTrace();
        }

        return null;
    }
}
