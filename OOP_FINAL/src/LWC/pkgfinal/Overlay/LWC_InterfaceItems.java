package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;


public class LWC_InterfaceItems extends LWC_AbstractInterface {
    private int position = 0;
    private int row, col;
    private ArrayList<LWC_BaseItem> items;
    private LWC_Player player;
    private Dimension size;
    private int enter = 5;
    
    public LWC_InterfaceItems(LWC_RPG rpg, LWC_Player player) {
        super(rpg);
        img = new BufferedImage[2];
        img[0] = new BufferedImage(rpg.getWidth()/8, rpg.getHeight()*5/7, BufferedImage.TYPE_INT_ARGB);
        
        this.col = 3;
        this.row = 10;
        size = new Dimension(img[0].getWidth()/col, img[0].getHeight()/row);
        
        Graphics2D g = img[0].createGraphics();
        g.setColor(new Color(10, 10, 10, 200));
        for(int i=0; i<row; i++)
            for(int j=0; j<col; j++)
                g.fillRect(j*size.width, i*size.height, size.width, size.height);
        g.dispose();
        
        this.player = player;
        items = player.getItemList();
        
    }

    @Override
    public Point getPosition() {
        return new Point(rpg.getWidth() - (counter > enter ? enter : counter) * img[0].getWidth() / enter, rpg.getHeight()/7);
    }

    @Override
    public BufferedImage show() {
        img[1] = new BufferedImage(rpg.getWidth()/8, rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img[1].createGraphics();
        
        g.drawImage(img[0], 0, 0, null);
        for(int i=0; i<items.size(); i++){
            if(i >= col * row) break;
            BufferedImage tmp = LWC_Util.getResizedImage(items.get(i).getIcon(), new Dimension(size.width-4, size.height-4));
            g.drawImage(tmp, i%col*size.width + 2, i/col*size.height + 2, null);
        }
        g.setColor(Color.red);
        g.drawRect(position%col*size.width , position/col*size.height, size.width-1, size.height-1);
        
        if(position < items.size()){
            Point p = new Point(0, (int)(((position/3)+1)*size.getHeight()));
            g.setColor(new Color(10, 10, 10, 200));
            g.fillRect(p.x, p.y, rpg.getWidth()/8, 60);
            
            g.setColor(Color.white);
            g.drawString(items.get(position).getName(), p.x, p.y+20);
            BufferedImage textBox = LWC_Util.getDialogBox(items.get(position).getDescription(), new Dimension(rpg.getWidth()/8, 40), null, Color.yellow);
            g.drawImage(textBox, p.x, p.y+20, null);
        }
        
        g.dispose();
            
        return img[1];
    }

    @Override
    public boolean interface_update() {
        if(counter > enter){
            if(rpg.getKeys().isTyped(KeyEvent.VK_ESCAPE) || rpg.getKeys().isTyped(KeyEvent.VK_I))
                return false;
            else if(rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
                if(position < items.size()){
                    Point tmp = new Point(position%3*size.width + getPosition().x , position/3*size.height + getPosition().y);
                    rpg.addPauseInterface(new LWC_InterfaceItemMenu(rpg, player, items.get(position), tmp));
                }
            }
            else if(rpg.getKeys().isPressed(KeyEvent.VK_UP))
                position = position - 3 < 0 ? 0 : position - 3;
            else if(rpg.getKeys().isPressed(KeyEvent.VK_DOWN))
                position = position + 3 >= 30 ? 29 : position + 3;
            else if(rpg.getKeys().isPressed(KeyEvent.VK_LEFT))
                position = position - 1 < 0 ? 0 : position - 1;
            else if(rpg.getKeys().isPressed(KeyEvent.VK_RIGHT))
                position = position + 1 >= 30 ? 29 : position + 1;
        }
        return true;
    }
}
