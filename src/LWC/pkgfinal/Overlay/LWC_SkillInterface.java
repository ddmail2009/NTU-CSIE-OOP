package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.AI.LWC_PlayerAI;
import java.awt.Point;
import java.awt.image.BufferedImage;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_Player;
import java.awt.Color;
import java.awt.Graphics;

public class LWC_SkillInterface extends LWC_AbstractInterface {
    private LWC_Player owner;
    
    public LWC_SkillInterface(LWC_RPG rpg) {
        super(rpg);
        img = new BufferedImage[2];
        img[0] = new BufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        g.setColor(Color.ORANGE);
        g.drawRect(0, 0, 25, 25);
        g.setColor(new Color(10, 10, 10, 200));
        g.fillRect(0, 0, 25, 25);
        g.dispose();
        
        img[1] = new BufferedImage(g.getFontMetrics().charWidth('F'), g.getFontMetrics().getAscent(), BufferedImage.TYPE_INT_ARGB);
        g = img[1].getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, img[1].getWidth(), img[1].getHeight());
        g.setColor(Color.white);
        g.drawString("F", 0, g.getFontMetrics().getAscent());
        g.dispose();
        
        owner = rpg.getPlayer();
    }

    public void setPlayer(LWC_Player player){
        owner = player;
    }
    
    @Override
    public Point getPosition() {
        return new Point(rpg.getWidth() - 60, rpg.getHeight() - 40);
    }

    @Override
    public BufferedImage show() {
        BufferedImage tmp = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();
        g.drawImage(img[0], 1, 1, null);
        g.drawImage(img[0], 3, 3, null);
        g.drawImage(img[0], 5, 5, null);
        g.drawImage(img[0], 10, 10, null);
        if(owner.getAI() instanceof LWC_PlayerAI){
            BufferedImage []skillIcon = ((LWC_PlayerAI)owner.getAI()).getAttackIcon();
            if(skillIcon.length > 1) g.drawImage(skillIcon[1], 5, 5, 25, 25, null);
            if(skillIcon.length > 0) g.drawImage(skillIcon[0], 10, 10, 25, 25, null);
        }
        g.drawImage(img[1], 0, 30-img[1].getHeight(), null);
        g.dispose();
        return tmp;
    }

    @Override
    public boolean interface_update() {
        return true;
    }
    
}

