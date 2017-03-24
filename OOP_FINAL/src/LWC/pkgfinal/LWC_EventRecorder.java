package LWC.pkgfinal;

import LWC.pkgfinal.Effect.LWC_Gravity;
import LWC.pkgfinal.Loader.LWC_Loader;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Overlay.LWC_AbstractInterface;
import LWC.pkgfinal.Overlay.LWC_AbstractTrigger;
import java.awt.Point;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.json.simple.*;

public class LWC_EventRecorder implements Serializable {
    transient private JSONObject json = null;
    transient private LWC_RPG rpg = null;
    transient private String PlotState = "Normal3";
    
    protected void setPlotState(String str){
        PlotState = str;
    }
    public String getPlotState(){
        return PlotState;
    }
    
    public LWC_EventRecorder(LWC_RPG rpg) {
        this.rpg = rpg;
        
        LWC_Loader loader = new LWC_Loader("config/Events.txt");
        json = loader.getJSON();
    }
    
    private JSONArray getEvent(String PlotState){
        if(json.containsKey(PlotState))
            return (JSONArray) json.get(PlotState);
        return null;
    }
    
    public static ArrayList<Field> getAllFields(ArrayList<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null)
            fields = getAllFields(fields, type.getSuperclass());
        return fields;
    }

    private boolean checkField(JSONObject Object, Object obj){
        try{
            int count = 0;
            ArrayList<Field> fields = new ArrayList<>();
            getAllFields(fields, obj.getClass());
            System.err.println("fields.length = " + fields.size());
            for (Field field : fields) {
                System.err.println(field.getName());
                if(Object.containsKey(field.getName())){
                    field.setAccessible(true);
                    System.err.println(field.get(obj));
                    if(!field.get(obj).equals((String)Object.get(field.getName()))) 
                        return false;
                    else count++;
                }
            }
            if(count == Object.keySet().size()) return true;
            else return false;
        }catch(Exception e){
            System.err.println("Error checking field: " + e);
            e.printStackTrace();
            return false;
        }
    }
    
    public void addTrigger(JSONArray array){
        if(array.size() == 0) return;
        else{
            try{
                for(int i=0; i<array.size(); i++){
                    JSONObject obj = (JSONObject) array.get(i);
                    Constructor c = Class.forName((String)obj.get("Type")).getConstructor(LWC_RPG.class);
                    LWC_AbstractTrigger trigger = (LWC_AbstractTrigger) c.newInstance(rpg);
                    trigger.config((JSONObject) obj.get("Config"));
                    rpg.addTriggerOverlay(trigger);
                }
            }catch(Exception e){
                System.err.println("Error Do addInterface: " + e);
                e.printStackTrace();
            }
        }
    }
    public void addInterface(JSONArray array){
        if(array.size() == 0) return;
    }
    public void addPauseInterface(JSONArray array){
        if(array.size() == 0) return;
        else{
            try{
                for(int i=0; i<array.size(); i++){
                    JSONObject tmp = (JSONObject) array.get(i);
                    String inteClass = (String) tmp.get("Type");
                    Constructor c = Class.forName(inteClass).getConstructor(LWC_RPG.class);
                    LWC_AbstractInterface inter = (LWC_AbstractInterface) c.newInstance(rpg);
                    if(tmp.containsKey("Config")) inter.config((JSONObject) tmp.get("Config"));
                    rpg.addPauseInterface(inter);
                }
            }catch(Exception e){
                System.err.println("Error add PauseInterface while parsing : " + array.toJSONString());
                e.printStackTrace();
            }
        }
    }
    public void addEffect(JSONArray array){
        if(array.size() == 0) return;
    }
    public void addObj(JSONArray array){
        if(array.size() == 0) return;
        else{
            try{
                for(int i=0; i<array.size(); i++){
                    JSONObject tmp = (JSONObject) array.get(i);
                    String npcClass = (String) tmp.get("Type");
                    Constructor c = Class.forName(npcClass).getConstructor(String.class, String.class, LWC_RPG.class);
                    LWC_AbstractCharacter lwcObj = (LWC_AbstractCharacter) c.newInstance(tmp.get("Name"), tmp.get("Group"), rpg);
                    rpg.addObj(lwcObj);
                    rpg.setPosition(lwcObj, loadPoint((JSONObject)tmp.get("EnterPoint")));
                    rpg.addEffect(new LWC_Gravity(rpg, lwcObj));
                }
            }catch(Exception e){
                System.err.println("Error add Object while parsing : " + array.toJSONString());
                e.printStackTrace();
            }
        }
    }
    
    private void changePlot(JSONObject action, String Next){
        PlotState = Next;
        System.err.println("\nChange PlotState to : " + PlotState + "\n");
        if(action.containsKey("Trigger")) addTrigger((JSONArray)action.get("Trigger"));
        if(action.containsKey("Interface")) addInterface((JSONArray)action.get("Interface"));
        if(action.containsKey("PauseInterface")) addPauseInterface((JSONArray)action.get("PauseInterface"));
        if(action.containsKey("Effect")) addEffect((JSONArray)action.get("Effect"));
        if(action.containsKey("Obj")) addObj((JSONArray)action.get("Obj"));
    }
    private boolean isMatched(JSONObject obj, String type, Object inter){
        if(type.equals((String)obj.get("Type")) && inter.getClass().getName().equals((String)obj.get("ClassType")))
            if(checkField((JSONObject)obj.get("Field"), inter)) return true;
        return false;
    }
    
    public Object getData(String field, JSONObject obj){
        String Refer = (String) obj.get("Refer");
        int ReferIndex = 0;
        if(((Long) obj.get("ReferIndex")) != null) ReferIndex = ((Long) obj.get("ReferIndex")).intValue();
        
        JSONObject reference = null;
        if(Refer != null) reference = (JSONObject) getEvent(Refer).get(ReferIndex);
        
        if(obj.get(field) != null) return obj.get(field);
        else return getData(field, reference);
    }
    
    public void record(String type, Object inter){
        try{
            JSONArray current = getEvent(PlotState);
            if(current == null) throw new Exception();
            
            for(int i=0; i<current.size(); i++){
                String next = (String) getData("Next", (JSONObject)current.get(i));
                
                String conditionType = (String) getData("Type", (JSONObject)current.get(i));
                String condition = (String) getData("ClassType", (JSONObject)current.get(i));
                JSONObject field = (JSONObject) getData("Field", (JSONObject)current.get(i));
                
                JSONObject action = (JSONObject) getData("Action", (JSONObject)current.get(i));                
                if(conditionType.equals(type) && inter.getClass().getName().equals(condition))
                    if(checkField(field, inter)){
                        changePlot(action, next);
                        return;
                    }
            }
        }catch(Exception e){
            System.err.println("Error parsing, when in current State " + PlotState);
            e.printStackTrace();
        }
    }
    
    private Point loadPoint(JSONObject point){
        return new Point(((Long)point.get("x")).intValue(), ((Long)point.get("y")).intValue());
    }
}
