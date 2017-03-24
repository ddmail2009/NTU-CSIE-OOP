package LWC.pkgfinal.Loader;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Overlay.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.*;
import org.json.simple.*;

public class MapLoader extends LWC_Loader {    
    @SuppressWarnings("unchecked")
    public MapLoader(String files){
        super(files);
    }
    
    public JSONObject getCurrentMap(LWC_RPG rpg){
        JSONObject obj = null;
        if(json.containsKey(rpg.getPlotState())) obj = (JSONObject) json.get(rpg.getPlotState());
        else obj = (JSONObject) json.get("Default");
        return obj;
    }
    
    public BufferedImage getBackground(LWC_RPG rpg){
        return rpg.imgPool().getImage((String)getCurrentMap(rpg).get("Map"));
    }
    
    public BufferedImage getForwardgroud(LWC_RPG rpg){
        return rpg.imgPool().getImage((String)getCurrentMap(rpg).get("ForeGround"));
    }
    
    public String getName(LWC_RPG rpg){
        return (String)getCurrentMap(rpg).get("Name");
    }
    
    public LWC_TriggerSolidMap getSolid(LWC_RPG rpg){
        LWC_TriggerSolidMap solid = new LWC_TriggerSolidMap(rpg);
        solid.setImage(rpg.imgPool().getImage((String)getCurrentMap(rpg).get("Solid")));
        return solid;
    }
    
    public void load(LWC_RPG rpg){
        JSONObject obj = getCurrentMap(rpg);
                
        JSONArray trigger = (JSONArray) obj.get("Trigger");
        JSONArray NPC = (JSONArray) obj.get("NPC");
        JSONArray Obj = (JSONArray) obj.get("Obj");
        try {
            if(trigger != null){
                for(int i=0; i<trigger.size(); i++){
                    JSONObject tmp = (JSONObject) trigger.get(i);
                    String triggerClass = (String)tmp.get("Type");
                    Constructor c = Class.forName(triggerClass).getConstructor(LWC_RPG.class);
                    LWC_AbstractTrigger t = (LWC_AbstractTrigger) c.newInstance(rpg);
                    if(tmp.containsKey("Config")) t.config((JSONObject)tmp.get("Config"));
                    rpg.addTriggerOverlay(t);
                }
            }
            
            if(NPC != null){
                for(int i=0; i<NPC.size(); i++){
                    JSONObject tmp = (JSONObject) NPC.get(i);
                    String npcClass = (String) tmp.get("Type");
                    Constructor c = Class.forName(npcClass).getConstructor(String.class, String.class, LWC_RPG.class);
                    LWC_AbstractCharacter lwcObj = (LWC_AbstractCharacter) c.newInstance(tmp.get("Name"), tmp.get("Group"), rpg);
                    rpg.addObj(lwcObj);
                    rpg.setPosition(lwcObj, loadPoint((JSONObject)tmp.get("EnterPoint")));
                    rpg.addEffect(new LWC_Gravity(rpg, lwcObj));
                }
            }
            
            if(Obj != null){
                for(int i=0; i<Obj.size(); i++){
                    JSONObject tmp = (JSONObject) Obj.get(i);
                    String objClass = (String) tmp.get("Type");
                    Constructor c = Class.forName(objClass).getConstructor(LWC_RPG.class);
                    rpg.addObj((LWC_AbstractObject) c.newInstance(rpg), loadPoint((JSONObject) tmp.get("EnterPoint")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("LoadMap Error: " + ex);
        }
    }

    public Point getEnterPoint(LWC_RPG rpg) {
        JSONObject obj = getCurrentMap(rpg);
        
        JSONArray array = (JSONArray)obj.get("EnterPoint");
        for(int i=0; i<array.size(); i++){
            JSONObject tmp = (JSONObject) array.get(i);
            if(((String)tmp.get("From")).equals(rpg.getMapName()))
                return loadPoint((JSONObject)tmp.get("Player"));
        }
        
        for(int i=0; i<array.size(); i++){
            JSONObject tmp = (JSONObject) array.get(i);
            if(((String)tmp.get("From")).equals(""))
                return loadPoint((JSONObject)tmp.get("Player"));
        }
        return new Point();
    }
    
    public Point getViewPoint(LWC_RPG rpg){
        JSONObject obj = getCurrentMap(rpg);
        
        JSONArray array = (JSONArray)obj.get("EnterPoint");
        for(int i=0; i<array.size(); i++){
            JSONObject tmp = (JSONObject) array.get(i);
            if(((String)tmp.get("From")).equals(rpg.getMapName()))
                return loadPoint((JSONObject)tmp.get("ViewPoint"));
        }
        
        for(int i=0; i<array.size(); i++){
            JSONObject tmp = (JSONObject) array.get(i);
            if(((String)tmp.get("From")).equals(""))
                return loadPoint((JSONObject)tmp.get("ViewPoint"));
        }
        return getEnterPoint(rpg);
    }
    
    private Point loadPoint(JSONObject point){
        return new Point(((Long)point.get("x")).intValue(), ((Long)point.get("y")).intValue());
    }
}
