package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;


public class LWC_SaveSlot extends LWC_AbstractInterface {
    private Dimension slotSize;
    private int slotNumber = 10;
    private int choice = 0;
    private boolean SaveMode = true;
    
    public LWC_SaveSlot(LWC_RPG rpg, boolean SaveMode) {
        super(rpg);
        this.SaveMode = SaveMode;
        slotSize = new Dimension(rpg.getWidth()*3/4, rpg.getHeight()/slotNumber);
        
        img = new BufferedImage[2];
        img[0] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        g.setColor(new Color(10, 10, 10, 200));
        g.fillRect(0, 0, img[0].getWidth(), img[0].getHeight());
        g.setColor(new Color(20, 20, 20, 240));
        g.fillRect((rpg.getWidth()-slotSize.width)/2, 0, slotSize.width, img[0].getHeight());
        g.dispose();
        
        
        img[1] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g = img[1].getGraphics();
        g.setColor(Color.white);
        for(int i=0; i<10; i++){
            g.drawRect(rpg.getWidth()/2-slotSize.width/2, slotSize.height*i, slotSize.width, slotSize.height);
            int stringHeight = (slotSize.height + g.getFontMetrics().getAscent())/2;
            g.drawString(String.format("Slot %d", i+1), rpg.getWidth()/2-slotSize.width/2 + 10, slotSize.height*i + stringHeight);
        }
        g.dispose();
    }

    @Override
    public Point getPosition() {
        return new Point();
    }

    @Override
    public BufferedImage show() {
        slotSize = new Dimension(rpg.getWidth()*3/4, rpg.getHeight()/slotNumber);
        
        img = new BufferedImage[2];
        img[0] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img[0].getGraphics();
        g.setColor(new Color(10, 10, 10, 200));
        g.fillRect(0, 0, img[0].getWidth(), img[0].getHeight());
        g.setColor(new Color(20, 20, 20, 240));
        g.fillRect((rpg.getWidth()-slotSize.width)/2, 0, slotSize.width, img[0].getHeight());
        g.dispose();
        
        img[1] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g = img[1].getGraphics();
        g.setColor(Color.white);
        for(int i=0; i<10; i++){
            g.drawRect(rpg.getWidth()/2-slotSize.width/2, slotSize.height*i, slotSize.width, slotSize.height);
            int stringHeight = (slotSize.height + g.getFontMetrics().getAscent())/2;
            g.drawString(String.format("Slot %d", i+1), rpg.getWidth()/2-slotSize.width/2 + 10, slotSize.height*i + stringHeight);
        }
        g.dispose();
        
        BufferedImage tmp = new BufferedImage(img[0].getWidth(), img[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
        g = tmp.getGraphics();
        g.drawImage(img[0], 0, 0, null);
        g.setColor(new Color(100, 100, 100, 250));
        g.fillRect(rpg.getWidth()/2-slotSize.width/2, slotSize.height*choice, slotSize.width, slotSize.height);
        g.drawImage(img[1], 0, 0, null);
        
        g.setFont(new Font("Dialog", Font.BOLD, 20));
        g.setColor(Color.white);
        int stringHeight = (slotSize.height + g.getFontMetrics().getAscent())/2;
        for(int i=0; i<10; i++){
            String fileName = String.format("save[%d].lwcInfo", i);
            if(new File(fileName).exists()){
                String info = LWC_Util.readFile(fileName);
                g.drawString(info, rpg.getWidth()/2-slotSize.width/2 + 50, slotSize.height*i+stringHeight);
            }
        }
        g.dispose();
        return tmp;
    }

    @Override
    public boolean interface_update() {
        if(rpg.getKeys().isTyped(KeyEvent.VK_ESCAPE)) return false;
        else if(rpg.getKeys().isPressed(KeyEvent.VK_UP)) choice = (choice - 1) < 0 ? slotNumber - 1 : (choice - 1);
        else if(rpg.getKeys().isPressed(KeyEvent.VK_DOWN)) choice = (choice + 1)%slotNumber;
        else if(rpg.getKeys().isTyped(KeyEvent.VK_ENTER)){
            String fileName = String.format("save[%d]", choice);
            
            File file = new File(fileName + ".lwcSave");
            if(SaveMode == true){
                rpg.RPG_Save(fileName);
                return false;
            }
            else if(file.exists()){
                rpg.RPG_Load(fileName + ".lwcSave");
                return false;
            }
        }
        return true;
    }
    
}
