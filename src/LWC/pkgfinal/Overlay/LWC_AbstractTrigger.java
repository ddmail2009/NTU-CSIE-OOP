package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.ImageIcon;
import org.json.simple.*;

public abstract class LWC_AbstractTrigger extends LWC_AbstractOverLay{
    transient public BufferedImage img;
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
        out.writeObject(new ImageIcon(img));
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        img = LWC_Util.Image2BufferedImage(((ImageIcon)in.readObject()).getImage());
    }
    
    
    /**
     * Overlay Constructor to construct the image
     * @param rpg The main class of LWC_World
     */
    public LWC_AbstractTrigger(LWC_RPG rpg){
        super(rpg);
    }
    
    /**
     * check if the given rectangle is in the trigger area
     * @param rec the query rectangle 
     * @return true if it is triggered
     */
    public boolean isTriggered(Rectangle rec){
        return isTriggered(rec, null);
    }
    
    /**
     * check if the given rectangle is in the trigger area
     * @param rec the query rectangle 
     * @param recordPoint record which points is triggered
     * @return true if it is triggered
     */
    public boolean isTriggered(Rectangle rec, Point recordPoint){
        for (int i=rec.x; i<rec.x+rec.width; i++)
            for (int j=rec.y; j<rec.y+rec.height; j++)
                if(!isTriggered(new Point(i, j))){
                    if(recordPoint != null){
                        recordPoint.x = i;
                        recordPoint.y = j;
                    }
                    return false;
                }
        return true;
    }
    
    /**
     * Check if the given point is triggered
     * @param p the query point
     * @return true if this point isn't transparent in the original image
     */
    public boolean isTriggered(Point p){
        int color;
        try{
            color = img.getRGB(p.x, p.y);
            if( ((color>>24)&0xff) != 0 ) return true;
            else return false;
        }catch(Exception e){
            return true;
        }
    }
    
    public void setImage(BufferedImage img){
        this.img = img;
    }
    public void setRect(Rectangle rect){
        System.err.println("SetRect: "+ rect);
        img = new BufferedImage(rpg.getBackgroundSize().width, rpg.getBackgroundSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.dispose();
    }
    /**
     * the action to be done if triggered
     * @param rpg the main class of LWC_World
     * @param obj the obj which is triggered
     * @param enter_vector the obj entering vector
     */
    public abstract void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector);
    
    public void ignore(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector){
        ;
    }
    public void config(JSONObject obj){
        if(obj.containsKey("img"))
            setImage(rpg.imgPool().getImage((String)obj.get("img")));
        else if(obj.containsKey("rect")){
            JSONObject tmp = (JSONObject) obj.get("rect");
            Rectangle rect = new Rectangle();
            if(tmp.containsKey("x")) rect.x = ((Long)tmp.get("x")).intValue();
            if(tmp.containsKey("y")) rect.y = ((Long)tmp.get("y")).intValue();
            if(tmp.containsKey("width")) rect.width = ((Long)tmp.get("width")).intValue();
            if(tmp.containsKey("height")) rect.height = ((Long)tmp.get("height")).intValue();
            setRect(rect);
        }
    }
}
