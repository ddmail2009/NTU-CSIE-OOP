package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class LWC_InterfaceDialog extends LWC_AbstractInterface {
    private Dimension size = null;
    private String str = "Template String";
    protected int duration = 60;
    private Font font = new Font("Dialog", Font.BOLD, 20);
    
    public LWC_InterfaceDialog(LWC_RPG rpg) {
        super(rpg);
        setSize(new Dimension(rpg.getWidth(), rpg.getHeight()/4));
    }

    @Override
    public Point getPosition() {
        return new Point(0, rpg.getHeight()-img[0].getHeight());
    }

    @Override
    public BufferedImage show() {
        img = new BufferedImage[1];
        img[0] = LWC_Util.getResizedImage(rpg.imgPool().getImage("img/chat/chat.png"), size);
        
        Graphics2D g2d = img[0].createGraphics();
        String tmp = (counter<duration) ? str.substring(0, (int)((double)counter*str.length()/duration)) : str;
        BufferedImage tmpimg = LWC_Util.getDialogBox(tmp, size, font, Color.WHITE);
        g2d.drawImage(tmpimg, img[0].getWidth()/15, img[0].getHeight()/7, null);
        
        if(counter > duration){
            BufferedImage tmppimg = LWC_Util.getResizedImage(rpg.imgPool().getImage("img/button/enter.png"), new Dimension(30, 30));
            g2d.drawImage(tmppimg, img[0].getWidth()*13/15, img[0].getHeight()*5/8, null);
        }
        g2d.dispose();
        return img[0];
    }

    @Override
    public boolean interface_update() {
        if(counter >= duration && rpg.getKeys().isTyped(KeyEvent.VK_Z))
            return false;
        return true;
    }
    
    public void setSize(Dimension dim){
        size = dim;
    }

    /**
     * @param str the str to set
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
