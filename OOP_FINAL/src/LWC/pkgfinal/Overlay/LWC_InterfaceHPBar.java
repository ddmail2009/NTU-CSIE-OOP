/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public class LWC_InterfaceHPBar extends LWC_AbstractInterface {
    protected LWC_Player monitor;
    protected Point relative;
    protected Dimension size;
    protected Color color;
    
    public LWC_InterfaceHPBar(LWC_RPG rpg) {
        super(rpg);
        img = new BufferedImage[1];
        this.monitor = null;
        this.relative = new Point();
        this.size = new Dimension(250, 15);
        this.color = Color.red;
    }
    
    public void setMonitor(LWC_Player obj){
        monitor = obj;
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    @Override
    public Point getPosition() {
        return new Point(10, rpg.getHeight() - 40);
    }
       
    public int monitorValue() throws ClassCastException{
        return (int)monitor.getRegister("HP");
    }
    
    public int monitorMaxValue() throws ClassCastException{
        return (int)monitor.getRegister("MaxHP");
    }
    
    @Override
    public BufferedImage show() {
        if(monitor != null){
            img[0] = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img[0].getGraphics();
            g.setColor(new Color(10, 10, 10, 100));
            g.fillRect(0, 0, size.width, size.height);
            
            g.setColor(color);
            g.fillRect(0, 0, monitorValue()*size.width/monitorMaxValue(), size.height);
            g.drawRect(0, 0, size.width-2, size.height-1);
            
            g.setColor(Color.white);
            g.drawString(String.format("%d/%d", monitorValue(), monitorMaxValue()), size.width/2-20, size.height-2);
            g.dispose();
            return img[0];
        }
        else return null;
    }

    @Override
    public boolean interface_update() {
        if(monitor == null) return false;
        else return true;
    }
}
