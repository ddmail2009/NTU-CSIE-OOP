package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Loader.ScriptLoader;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class LWC_InterfaceOpeningCinema extends LWC_InterfaceEmpty{
    private Dimension size = null;
    private Font font = new Font("Dialog", Font.BOLD, 20);
    protected Color color;
    private boolean end = true;
    private String str = "temp";
    private int talk_speed = 2;
    protected int duration = 60;
    private int openScript_iter = 0;
    ArrayList<String> openScript;


    public LWC_InterfaceOpeningCinema(LWC_RPG rpg) {
        super(rpg);
        size = new Dimension(rpg.getWidth(), rpg.getHeight());
    }
    
    public void setStr(String str) {
        this.str = str;
        duration = str.length()*talk_speed;
    }

    public void load_script(String token){
        ScriptLoader scriptLoader = new ScriptLoader("config/OpenCinema.txt");
        System.err.println("token = " + token);
        openScript = scriptLoader.load_script(token);
        if(openScript == null)
            return;
        System.err.println(openScript.size());
        System.err.println(openScript.get(0));
        this.setStr(openScript.get(openScript_iter));
    }
    @Override
    public boolean interface_update() {
        if(counter >= duration+10 && rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_Z)){
            counter = 0;
            newImage();
            openScript_iter++;
            if(openScript_iter < openScript.size()){
                this.setStr(openScript.get(openScript_iter));
                System.out.println("openScript_iter:"+openScript_iter+",openScript.size:"+openScript.size()+",str:"+str);
                System.out.println(str.length());
            }
            else
            return false;
        }
        return true;
    }
    private void newImage(){
        img = new BufferedImage[2];
        img[0] = new BufferedImage(rpg.getWidth(), rpg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        color = Color.black;
        Graphics g = img[0].getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, rpg.getWidth(), rpg.getHeight());
        g.dispose();
        setOpaque(1);
    }
    @Override
    public BufferedImage show(){
        Graphics2D g2d = img[0].createGraphics();
        String tmp = (counter<duration) ? str.substring(0, (int)((double)counter*str.length()/duration)) : str;
        BufferedImage tmpimg = LWC_Util.getDialogBox(tmp, size, font, Color.WHITE);
        g2d.drawImage(tmpimg, img[0].getWidth()/15, img[0].getHeight()/7, null);
        
        if(counter > duration){
            BufferedImage tmppimg = LWC_Util.getResizedImage(rpg.imgPool().getImage("img/button/enter.png"), new Dimension(30, 30));
            g2d.drawImage(tmppimg, img[0].getWidth()*13/15, img[0].getHeight()*5/8, null);
        }
        g2d.dispose();
        return img[0];
    }
    @Override
    public Point getPosition() {
        return new Point(0, 0);
    }
}
