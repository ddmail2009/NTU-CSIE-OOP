package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class LWC_InterfaceExit extends LWC_AbstractInterface{
    private int choice = 0;
    private String []option = {"重拾", "休兵", "道具", "回首", "棄逃"};
    private Dimension optionSize = new Dimension();
    private Dimension Size = new Dimension(180, 250);
    public LWC_InterfaceExit(LWC_RPG rpg) {
        super(rpg);
        optionSize = new Dimension((int) (Size.width*0.9), (Size.height - 10*(option.length+1))/option.length);

        img = new BufferedImage[1];
        img[0] = new BufferedImage(Size.width, Size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        g.setColor(new Color(10, 10, 10, 200));
        g.fillRect(0, 0, img[0].getWidth(), img[0].getHeight());
        
        g.setColor(new Color(20, 20, 20, 240));
        for(int i=0; i<option.length; i++)
            g.fillRect((int) (Size.width*0.05), 10*(i+1) + optionSize.height*i, optionSize.width, optionSize.height);
        g.dispose();
    }
    
    @Override
    public Point getPosition() {
        return new Point(rpg.getWidth()/2-img[0].getWidth()/2, rpg.getHeight()/2-img[0].getHeight()/2);
    }

    @Override
    public BufferedImage show() {
        BufferedImage tmp = new BufferedImage(img[0].getWidth(), img[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();
        g.drawImage(img[0], 0, 0, null);
        for(int i=0; i<option.length; i++){
            if(i == choice) g.setFont(new Font("微軟正黑體", Font.BOLD, 50));
            else g.setFont(new Font("微軟正黑體", Font.PLAIN, 30));
            Dimension font = new Dimension(g.getFontMetrics().stringWidth(option[i]), (g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent())/2);
            g.drawString(option[i], tmp.getWidth()/2 - font.width/2, 10*(i+1) + optionSize.height*i + (optionSize.height + font.height)/2 );
        }
        g.dispose();
        return tmp;
    }

    @Override
    public boolean interface_update() {
        if(rpg.getKeys().isPressed(KeyEvent.VK_UP)) choice = choice - 1 < 0 ? option.length-1 : choice - 1;
        else if(rpg.getKeys().isPressed(KeyEvent.VK_DOWN)) choice = choice + 1 >= option.length ? 0 : choice + 1;
        else if(rpg.getKeys().isTyped(KeyEvent.VK_ESCAPE)) return false;
        
        if(rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
            switch (option[choice]) {
                case "棄逃":
                    System.exit(0);
                    break;
                case "重拾":
                    rpg.addPauseInterface(new LWC_SaveSlot(rpg, false));
                    break;
                case "休兵":
                    rpg.addPauseInterface(new LWC_SaveSlot(rpg, true));
                    break;
                case "道具":
                    rpg.addPauseInterface(new LWC_InterfaceItems(rpg, rpg.getPlayer()));
                    break;
            }
            return false;
        };
        return true;
    }
}
