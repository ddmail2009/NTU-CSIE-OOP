package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_MP3Player;
import LWC.pkgfinal.LWC_RPG;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LWC_InterfaceHelper extends LWC_AbstractInterface {
    private int enter = 30;
    private int duration = 60;
    private static ArrayList<LWC_InterfaceHelper> holder = new ArrayList<>();
    
    public LWC_InterfaceHelper(LWC_RPG rpg) {
        super(rpg);
        setStr("");
    }
    
    public LWC_InterfaceHelper(LWC_RPG rpg, String helper) {
        super(rpg);
        setStr(helper);
    }
    
    void setStr(String helper){
        img = new BufferedImage[1];
        img[0] = new BufferedImage(150, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        
        g.setColor(new Color(10, 10, 10, 210));
        g.fillRect(0, 0, img[0].getWidth(), img[0].getHeight());
        
        g.setColor(Color.white);
        g.setFont(new Font("Dialog", Font.PLAIN, 15));
        g.drawString(helper, 10, g.getFontMetrics().getAscent());
        g.dispose();
    }
    
    @Override
    public Point getPosition() {
        Point p = new Point();
        if(counter <= enter) p.x = rpg.getWidth() - counter*img[0].getWidth()/enter;
        else if(counter <= enter + duration)p.x = rpg.getWidth() - img[0].getWidth();
        else p.x = rpg.getWidth() - img[0].getWidth() + (counter-enter-duration)*img[0].getWidth()/enter;
        
        p.y = img[0].getHeight()*(holder.indexOf(this)) + img[0].getHeight();
        return p;
    }

    @Override
    public BufferedImage show() {
        return img[0];
    }

    @Override
    public boolean interface_update() {
        if(counter == 1) {
            LWC_MP3Player dingdong = new LWC_MP3Player("music/dingdong.mp3");
            dingdong.play();
            holder.add(this);
        }
        if(counter > 2*enter + duration) {
            holder.remove(this);
            return false;
        }
        return true;
    }
}
