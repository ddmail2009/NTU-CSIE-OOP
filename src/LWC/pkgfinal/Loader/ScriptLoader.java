package LWC.pkgfinal.Loader;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ScriptLoader extends LWC_Loader{    
    public ScriptLoader(String files){
        super(files);
    }
    
    public ArrayList<String> load_script(String npc_token){
        if(npc_token == null)
            return null;
        ArrayList<String> script = new ArrayList<>();
        JSONArray array = (JSONArray)json.get(npc_token);
        JSONObject obj = (JSONObject) array.get(0);
        
        for(int i=0;i < ((Long)obj.get("number")).intValue(); i++){
            JSONObject temp = (JSONObject) array.get(1);
            script.add((String)temp.get(Integer.toString(i+1)));
        }
        return script;
    }

    public ArrayList<String> load_script(String npc_token, String condition){
        System.out.println("in the load_script"+npc_token);

        ArrayList<String> script = new ArrayList<>();
        JSONArray array = (JSONArray)json.get(npc_token);
        if(array == null){
            System.out.println("OOPs, Wrong NPC name");
            return null;
        }
        for(int i=0;i<array.size();i++){
            JSONObject obj = (JSONObject) array.get(i);
            if( ((String)obj.get("type")).equals(condition)){
                for(int j=0;j < ((Long)obj.get("number")).intValue();j++)
                    script.add((String)obj.get(Integer.toString(j+1)));
                break;       
            }
            else
                continue;
        }
        if(script.size() == 0)
            return null;
        else
            return script;
    }
}
