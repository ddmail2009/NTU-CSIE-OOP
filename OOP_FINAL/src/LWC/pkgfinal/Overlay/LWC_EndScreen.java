package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class LWC_EndScreen extends LWC_AbstractEffectInterface {
    protected int duration = 300;
    
    public LWC_EndScreen(LWC_RPG rpg) {
        super(rpg);
        img = new BufferedImage[2];
        img[0] = rpg.imgPool().getImage("img/menu/endBackground.png");
        img[1] = rpg.imgPool().getImage("img/menu/endImage.png");
    }

    @Override
    public boolean effect_interface_update() {
        if(counter > duration && rpg.getKeys().isTyped(KeyEvent.VK_ESCAPE)) {
            rpg.addPauseInterface(new LWC_StartMenu(rpg));
            return false;
        }
        if(counter > duration && rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
            rpg.addPauseInterface(new LWC_StartMenu(rpg));
            return false;
        }
        return true;
    }

    @Override
    public Point getPosition() {
        return new Point();
    }

    @Override
    public BufferedImage show() {
        BufferedImage tmp = new BufferedImage(rpg.getWindowSize().width, rpg.getWindowSize().height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = tmp.getGraphics();
        g.drawImage(img[0], 0, 0, null);
        BufferedImage tmpp;
        if(counter < duration) tmpp = LWC_Util.setOpaque(img[1], ((float)counter)/duration);
        else tmpp = img[1];
        g.drawImage(tmpp, (rpg.getWindowSize().width-tmpp.getWidth())/2, (rpg.getWindowSize().height-tmpp.getHeight())/2, null);
        g.dispose();
        return tmp;
    }
}
