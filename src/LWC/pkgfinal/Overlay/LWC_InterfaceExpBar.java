package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public class LWC_InterfaceExpBar extends LWC_InterfaceHPBar {
    private int exp = 0;
    
    public LWC_InterfaceExpBar(LWC_RPG rpg) {
        super(rpg);
        setColor(Color.magenta);
        size = new Dimension(400, size.height);
    }
    
    @Override
    public void setMonitor(LWC_Player obj){
        super.setMonitor(obj);
        exp = monitorValue();
    }
    
    @Override
    public Point getPosition() {
        return new Point(300, rpg.getHeight() - 20);
    }
    
    @Override
    public int monitorValue() throws ClassCastException{
        return (int)monitor.getRegister("Exp");
    }
    
    @Override
    public int monitorMaxValue() throws ClassCastException{
        return monitorBase()*2;
    }
    
    public int monitorBase() throws ClassCastException{
        return (int)Math.pow(2, monitor.getLv());
    }
    
    @Override
    public BufferedImage show() {
        if(monitor != null){
            if(monitorValue() > exp) exp += 1;
            
            img[0] = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img[0].getGraphics();
            g.setColor(new Color(10, 10, 10, 100));
            g.fillRect(0, 0, size.width, size.height);
            
            g.setColor(color);
            g.fillRect(0, 0, (exp-monitorBase())*size.width/(monitorMaxValue()-monitorBase()), size.height);
            g.drawRect(0, 0, size.width-2, size.height-1);
            
            g.setColor(Color.white);
            g.drawString(String.format("LV: %d, Exp: %d/%d", monitor.getLv(), exp, monitorMaxValue()), size.width/2-20, size.height-2);
            g.dispose();
            return img[0];
        }
        else return null;
    }
    
}
