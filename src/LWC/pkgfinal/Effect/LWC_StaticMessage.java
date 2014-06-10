package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;

public class LWC_StaticMessage extends LWC_AbstractAnimate {
    protected String str = "Template Message, Please Use setString(String) to set";
    protected Font font = new Font("Dialog", Font.BOLD, 12);
    protected Color color = Color.WHITE;
    
    protected boolean alive = true;
    protected Point position = new Point();
    protected LWC_AbstractObject obj;
    
    public LWC_StaticMessage(LWC_RPG rpg) {
        super(rpg, "Message");
        
        img = new BufferedImage[1];
        img[0] = rpg.imgPool().getImage("img/chat/chat.png");
        
        obj = new LWC_AbstractObject(rpg) {
            @Override
            public boolean action() {
                return alive;
            }
            
            @Override
            public BufferedImage show() {
                BufferedImage img2 = new BufferedImage(img[0].getWidth(), img[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = img2.getGraphics();
                
                BufferedImage tmp = LWC_Util.getDialogBox(str, new Dimension(img[0].getWidth()*12/14, img[0].getHeight()*5/6), font, color);
                g.drawImage(img[0], 0, 0, null);
                g.drawImage(tmp, img[0].getWidth()/14, img[0].getHeight()/5, null);
                g.dispose();
                return img2;
            }
        };
        
        setString(str);
    }
    
    public void setSize(Dimension size){
        img[0] = LWC_Util.getResizedImage(img[0], size);
    }

    public void setString(String str){
        this.str = str;
    }
    
    public void setFont(Font t){
        font = t;
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    @Override
    protected boolean effect_update() {
        if(counter == 1) rpg.addObj(obj, position);
        if(counter < getDuration())    
            return true;
        alive = false;
        return false;
    }
}

