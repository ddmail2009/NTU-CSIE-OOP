package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Loader.ScriptLoader;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import org.json.simple.JSONObject;

public class LWC_InterfaceNPCMsg extends LWC_InterfaceDialog {
	ArrayList<String> conversation = null;
    private String str;
    private String name = null;
	private int conversation_iter = 0;
    private int talk_speed = 2;
    
    public LWC_InterfaceNPCMsg(LWC_RPG rpg) {
        super(rpg);
        this.setStr("");
    }
    public void addStr(String s){
        if(conversation == null && s!= null){
            conversation = new ArrayList<String>();
            conversation.add(s);
            str = conversation.get(conversation_iter%conversation.size());
            this.setDuration(this.str.length()*talk_speed);
            this.setStr(str);
        }
        else if(s != null)
            conversation.add(s);
    }
    
    @Override
    public void config(JSONObject obj){
        if(obj.containsKey("name") && obj.containsKey("condition")) 
            load_conversation((String) obj.get("name"), (String) obj.get("condition"));
        else if(obj.containsKey("name"))
            load_conversation((String) obj.get("name"));
    }
    
    public boolean load_conversation(String name){
    	ScriptLoader script_loader = new ScriptLoader("config/NPC_TALK.txt");
    	conversation = script_loader.load_script(name);
        if(conversation == null)
            return false;
        else
            this.name = name;
        for(int i=0;i<conversation.size();i++){
            System.out.println(conversation.get(i));
        }
        str = conversation.get(conversation_iter%conversation.size());
        this.setDuration(str.length()*talk_speed);
        this.setStr(str);

        return true;
    }

    public boolean load_conversation(String name, String condition){
        ScriptLoader script_loader = new ScriptLoader("config/NPC_TALK.txt");

        System.out.println(condition);
        conversation = script_loader.load_script(name, condition);
        if(conversation == null)
            return false;

        else this.name = name;

        for(int i=0;i<conversation.size();i++){
            System.out.println(conversation.get(i));
        }
        str = conversation.get(conversation_iter%conversation.size());
        this.setDuration(str.length()*talk_speed);
        this.setStr(str);

        return true;
    }
    
    @Override
    public boolean interface_update() {
        if(str == null || str.length() == 0) return false;
        if(counter >= duration && rpg.getKeys().isTyped(KeyEvent.VK_Z)){
        	counter = 0;
            if(conversation != null){
                conversation_iter = (conversation_iter + 1) % conversation.size();
                str = conversation.get(conversation_iter);
                if(str == null)return false;
                this.setDuration(str.length()*talk_speed);
                this.setStr(str);
                if(conversation_iter == 0){
                    return false;
                }
            }
            else return false;
            
        }
        return true;
    }
}
