package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;

public class LWC_StartMenu extends LWC_AbstractInterface {
    private BufferedImage background = null;
    private int choice = 1;
    
    public LWC_StartMenu(LWC_RPG rpg) {
        super(rpg);
        rpg.mp3Pool().replacePlayer("background", "music/Maplestory OST - Main Theme - Temple of Time.mp3").play();
        
        img = new BufferedImage[5];
        img[0] = rpg.imgPool().getImage("img/menu/Title.png");
        img[1] = rpg.imgPool().getImage("img/menu/Title2.png");
        img[2] = rpg.imgPool().getImage("img/menu/Title3.png");
        img[3] = rpg.imgPool().getImage("img/menu/Title4.png");
        img[4] = rpg.imgPool().getImage("img/menu/TitleBar.png");
        
        background = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = background.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        g.dispose();
    }

    @Override
    public Point getPosition() {
        return new Point();
    }

    @Override
    public BufferedImage show() {
        background = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = background.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        g.dispose();
        
        BufferedImage tmp = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g = tmp.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.drawImage(img[0], (tmp.getWidth()-img[0].getWidth())/2, 5, null);
        for(int i=1; i<=3; i++)
            g.drawImage(img[i], (tmp.getWidth()-img[i].getWidth())/2, 200+(i-1)*120, null);
        g.drawImage(img[4], (tmp.getWidth()-img[4].getWidth())/2, 200+(choice-1)*120+110, null);
        g.dispose();
        
        return tmp;
        
    }

    @Override
    public boolean interface_update() {        
        if(rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
            if(choice == 1){
                LWC_MP3Player mp3 = new LWC_MP3Player("music/decide2.mp3");
                mp3.play();
                rpg.RPG_init();
                return false;
            }
            else if(choice == 2){
                LWC_MP3Player mp3 = new LWC_MP3Player("music/decide2.mp3");
                mp3.play();
                rpg.addPauseInterface(new LWC_SaveSlot(rpg, false));
            }
            else if(choice == 3)
                System.exit(0);
        }
        else if(rpg.getKeys().isPressed(KeyEvent.VK_UP)){
            choice = choice - 1 >= 1 ? choice - 1 : 1;
            LWC_MP3Player mp3 = new LWC_MP3Player("music/Cursor2.mp3");
            mp3.play();
        }
        else if(rpg.getKeys().isPressed(KeyEvent.VK_DOWN)) {
            choice = choice + 1 <= 3 ? choice + 1 : 3;
            LWC_MP3Player mp3 = new LWC_MP3Player("music/Cursor2.mp3");
            mp3.play();
        }
        return true;
    }
    
}
