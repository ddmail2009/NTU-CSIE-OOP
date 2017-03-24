/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import java.awt.*;
import java.awt.image.BufferedImage;


public class LWC_InterfaceEmpty extends LWC_AbstractInterface{
    protected Color color;
    private boolean end = true;
    
    public LWC_InterfaceEmpty(LWC_RPG rpg) {    
        super(rpg);
        img = new BufferedImage[2];
        img[0] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        color = Color.black;
        Graphics g = img[0].getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, rpg.getWidth(), rpg.getHeight());
        g.dispose();
        setOpaque(1);
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public void setOpaque(float n){
        img[1] = LWC_Util.setOpaque(img[0], n);
    }

    @Override
    public Point getPosition() {
        return new Point();
    }

    @Override
    public BufferedImage show() {
        return img[1];
    }
    
    public void end(){
        end = false;
    }

    @Override
    public boolean interface_update() {
        return end;
    }
    
}
