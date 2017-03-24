package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.*;
import java.awt.image.BufferedImage;


public class LWC_HurtNumber extends LWC_StaticMessage {
    protected Point relative = new Point();
    protected LWC_AbstractObject owner;
    
    public LWC_HurtNumber(LWC_RPG rpg, LWC_AbstractObject Character, int number) {
        super(rpg);
        owner = Character;
        if(number > 0)
            setString(String.format("%d", number));
        else
            setString(String.format("Block"));
        setColor(Color.red);
        setDuration(30);
        setFont(new Font("dialog", Font.BOLD, 30));
        
        img = new BufferedImage[1];
        img[0] = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        g.setColor(color);
        g.setFont(font);
        g.drawString(str, font.getSize(), font.getSize());
        g.dispose();
        
        obj = new LWC_AbstractObject(rpg) {
            @Override
            public boolean action() {
                return alive;
            }
            
            @Override
            public BufferedImage show() {
                return img[0];
            }
        };
        setPosition(new Point(-10, -10));
    }
    
    public void setPosition(Point p){
        relative = new Point(p);
        if(rpg.getPosition(owner) != null){
            Rectangle rect = LWC_Util.getObjArea(rpg, owner);
            position = new Point(rect.x + relative.x, rect.y + relative.y);
        }
    }
    
    @Override
    protected boolean effect_update() {
        boolean result = super.effect_update();
        setPosition(new Point(relative.x+(int)(Math.random()*6-3), relative.y-2));
        rpg.setPosition(obj, position);
        return result;
    }
}
