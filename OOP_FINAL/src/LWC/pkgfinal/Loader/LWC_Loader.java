package LWC.pkgfinal.Loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import org.json.simple.*;
import org.json.simple.parser.*;

public class LWC_Loader implements Serializable{
    protected JSONObject json;
     
    @SuppressWarnings("unchecked")
    public LWC_Loader(String files){
        files = "/" + files;
        try {
            JSONParser parser = new JSONParser();
            
            InputStream in = getClass().getResourceAsStream(files);
            InputStreamReader reader = new InputStreamReader(in,"UTF-8");
            json = (JSONObject)parser.parse(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("JSONParser Error while Load file: " + files);
            System.exit(1);
        }
        System.err.println(json.toJSONString());
    }
    
    public JSONObject getJSON(){
        return json;
    }
}
