package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.ImageIcon;

public abstract class LWC_AbstractInterface extends LWC_AbstractOverLay {
    protected int counter = 0;
    transient protected BufferedImage []img = null;
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        System.err.println(getClass().getName()+" saved");
        out.defaultWriteObject();
        if(img == null) out.writeInt(0);
        else{
            out.writeInt(img.length);
            for(int i=0; i<img.length; i++)
                out.writeObject(new ImageIcon(img[i]));
        }
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        System.err.println(getClass().getName()+" loaded");
        in.defaultReadObject();
        int n = in.readInt();
        img = new BufferedImage[n];
        for(int i=0; i<n; i++)
            img[i] = LWC_Util.Image2BufferedImage(((ImageIcon)in.readObject()).getImage());
    }
    
    public LWC_AbstractInterface(LWC_RPG rpg) {
        super(rpg);
    }

    public abstract Point getPosition();
    public abstract BufferedImage show();
    public boolean update(){
        counter = counter + 1;
        return interface_update();
    }
    public abstract boolean interface_update();
}
