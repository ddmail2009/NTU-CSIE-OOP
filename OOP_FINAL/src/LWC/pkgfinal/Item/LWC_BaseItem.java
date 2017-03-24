    package LWC.pkgfinal.Item;

import LWC.pkgfinal.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.ImageIcon;
import org.json.simple.JSONObject;

public class LWC_BaseItem implements Serializable{
    protected String name = "", description = "";
    protected LWC_RPG rpg = null;
    transient private BufferedImage img;

    public LWC_BaseItem(LWC_RPG rpg, String name, String description) {
        this.rpg = rpg;
        if(name != null) this.name = name;
        if(description != null) this.description = description;
    }
    
    public void setIcon(BufferedImage img){
        this.img = img;
    }
    public BufferedImage getIcon(){
        return img;
    }
    
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new ImageIcon(img));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        img = LWC_Util.Image2BufferedImage(((ImageIcon)in.readObject()).getImage());
    }

    public void config(JSONObject obj){
        if(obj.containsKey("Path")) 
            setIcon(rpg.imgPool().getImage((String)obj.get("Path")));
    }
}
