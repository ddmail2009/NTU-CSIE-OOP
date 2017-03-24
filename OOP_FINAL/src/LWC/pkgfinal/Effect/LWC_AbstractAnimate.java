package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public abstract class LWC_AbstractAnimate extends LWC_AbstractTime {
    transient protected BufferedImage[] img = null;
    
    public LWC_AbstractAnimate(LWC_RPG rpg, String secret) {
        super(rpg, secret);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
        if(img == null){
            System.err.println(this);
            out.writeInt(0);
        }
        else {
            out.writeInt(img.length);
            for(int i=0; i<img.length; i++){
                out.writeObject(new ImageIcon(img[i]));
            }
        }
        
    }
    private void loadObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        int count = in.readInt();
        img = new BufferedImage[count];
        for(int i=0; i<count; i++){
            ImageIcon icon = (ImageIcon)in.readObject();
            img[i] = LWC_Util.Image2BufferedImage(icon.getImage());
        }
    }
}
