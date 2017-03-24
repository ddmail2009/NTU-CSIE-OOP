package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;

public class LWC_MonsterHPBar extends LWC_AbstractAnimate {
    protected Point relative;
    protected Point position;
    protected Dimension size;
    protected Color color;
    protected boolean alive = true;
    protected LWC_AbstractObject obj;
    protected LWC_AbstractObject monitor;

    public LWC_MonsterHPBar(LWC_RPG rpg, LWC_AbstractObject target) {
        super(rpg, "HPBar");   
        img = new BufferedImage[1];
        this.monitor = target;
        this.relative = new Point(0, 10);
        this.position = new Point();
        
        this.size = new Dimension(LWC_Util.getObjArea(rpg, monitor).width, 6);
        this.color = Color.red;
        obj = new LWC_AbstractObject(rpg){
            @Override
            public boolean action() {
                return alive;
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
                   
                    g.dispose();
                    return img[0];
                }
                else return null;
            }
        };
    }
    
    public void setMonitor(LWC_Player obj){
        monitor = obj;
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    public int monitorValue() throws ClassCastException{
        return (int)monitor.getRegister("HP");
    }
    
    public int monitorMaxValue() throws ClassCastException{
        return (int)monitor.getRegister("MaxHP");
    }

    public void setPosition(){
        Rectangle area = LWC_Util.getObjArea(rpg, monitor);
        position = new Point(area.x + relative.x, area.y + area.height + relative.y);
    }

    @Override
    protected boolean effect_update() {
        if(counter == 1) rpg.addObj(obj, position);
        if(counter < getDuration()) {
            setPosition();
            rpg.setPosition(obj, position);    
            return true;
        }
        alive = false;
        return false;
    }
}

