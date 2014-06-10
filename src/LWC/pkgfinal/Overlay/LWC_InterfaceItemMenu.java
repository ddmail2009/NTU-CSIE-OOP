package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class LWC_InterfaceItemMenu extends LWC_AbstractInterface {
    private LWC_Player player;
    private LWC_BaseItem item;
    private Dimension optionSize;
    private String []Options;
    private int index;
    private Point position;
    
    public LWC_InterfaceItemMenu(LWC_RPG rpg, LWC_Player player, LWC_BaseItem item, Point position) {
        super(rpg);
        img = new BufferedImage[2];
        
        this.index = 0;
        this.optionSize = new Dimension(50, 25);
        this.player = player;
        this.item = item;
        this.position = new Point(position.x - optionSize.width, position.y);
        
        if(item instanceof LWC_BaseWeapon) Options = new String[]{"換武", "丟棄", "取消"};
        else if(item instanceof LWC_UsableItem) Options = new String[]{"使用", "丟棄", "取消"};
        else if(item instanceof LWC_ConsumableItem) Options = new String[]{"使用", "丟棄", "取消"};
        else Options = new String[]{"丟棄", "取消"};
        
        img[0] = new BufferedImage(optionSize.width, optionSize.height*Options.length, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].createGraphics();
        g.setColor(new Color(40, 40, 40, 200));
        g.fillRect(0, 0, optionSize.width, optionSize.height*Options.length);
        g.setColor(Color.white);
        for(int i=0; i<Options.length; i++)
            g.drawRect(0, optionSize.height*i, optionSize.width-1, optionSize.height-1);
        g.dispose();
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public BufferedImage show() {
        img[1] = new BufferedImage(optionSize.width, optionSize.height*Options.length, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[1].createGraphics();
        g.drawImage(img[0], 0, 0, null);
        for(int i=0; i<Options.length; i++){
            if(i == index) g.setColor(Color.red);
            else g.setColor(Color.white);
            g.drawString(Options[i], 2, optionSize.height*(i+1) - optionSize.height/4);
        }
        g.dispose();
        return img[1];
    }

    @Override
    public boolean interface_update() {       
        if(rpg.getKeys().isTyped(KeyEvent.VK_ESCAPE))
            return false;
        else if(rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
            switch (Options[index]) {
                case "換武":
                    if(item instanceof LWC_BaseWeapon){
                        player.addItem(player.weapon);
                        player.removeItem(item);
                        player.weapon = (LWC_BaseWeapon) item;
                    }
                    return false;
                case "丟棄":
                    player.removeItem(item);
                    return false;
                case "使用":
                    if(item instanceof LWC_ConsumableItem){
                        ((LWC_ConsumableItem)item).use();
                        player.removeItem(item);
                    }
                    if(item instanceof LWC_UsableItem)
                        ((LWC_UsableItem)item).use();
                    return false;
                case "取消":
                    return false;
            }
        }
        else if(rpg.getKeys().isPressed(KeyEvent.VK_DOWN))
            index = index + 1 >= Options.length ? Options.length-1 : index + 1;
        else if(rpg.getKeys().isPressed(KeyEvent.VK_UP))
            index = index - 1 < 0 ? 0 : index - 1;
        return true;
    }
}
