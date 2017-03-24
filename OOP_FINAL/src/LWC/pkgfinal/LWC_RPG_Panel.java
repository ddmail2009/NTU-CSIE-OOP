package LWC.pkgfinal;

import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Overlay.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class LWC_RPG_Panel extends JPanel{
    private LWC_Main main = null;
    private LWC_RPG rpg = null;
    private Rectangle viewArea = new Rectangle();

    public LWC_RPG_Panel(LWC_Main main) {
        this.main = main;
    }
    
    public void setRPG(LWC_RPG rpg){
        this.rpg = rpg;
        rpg.panel = this;
    }
    
    public void setViewPoint(Point p){
        viewArea = new Rectangle(p, new Dimension(getWidth(), getHeight()));
    }
    
    public Rectangle getViewArea(){
        return new Rectangle(viewArea);
    }
    
    public void ViewAreaMove(Point relative){
        if(LWC_Util.isInside(0, viewArea.x+relative.x, rpg.mapLoader.getBackground(rpg).getWidth()-viewArea.width)) 
            viewArea.x += relative.x;
        else if(relative.x < 0 && viewArea.x > rpg.mapLoader.getBackground(rpg).getWidth() - viewArea.width )
            viewArea.x += relative.x;
        else if(relative.x > 0 && viewArea.x < 0)
            viewArea.x += relative.x;
        
        if(LWC_Util.isInside(0, viewArea.y+relative.y, rpg.mapLoader.getBackground(rpg).getHeight()-viewArea.height)) 
            viewArea.y += relative.y;
        else if(relative.y < 0 && viewArea.y > rpg.mapLoader.getBackground(rpg).getHeight() - viewArea.height)
            viewArea.y += relative.y;
        else if(relative.y > 0 && viewArea.y < 0)
            viewArea.y += relative.y;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        viewArea.width = getWidth();
        viewArea.height = getHeight();
        
        synchronized(main.lock){
            if(rpg.mapLoader != null) g.drawImage(rpg.mapLoader.getBackground(rpg), -viewArea.x, -viewArea.y, this);
            if(rpg != null){
                ArrayList<LWC_AbstractObject> objList = rpg.getObjList();
                for(int i=objList.size()-1; i>=0; i--){
                    LWC_AbstractObject character = objList.get(i);
                    if(character.getRegister("Hidden") == null){
                        if(character instanceof LWC_Boss && character.getRegister("Position").equals("right"))
                            g.drawImage(character.show(), rpg.getPosition(character).x-260-viewArea.x, rpg.getPosition(character).y-viewArea.y, this);
                        else
                            g.drawImage(character.show(), rpg.getPosition(character).x-viewArea.x, rpg.getPosition(character).y-viewArea.y, this);
                    }
                }
            }
            if(rpg.mapLoader != null) g.drawImage(rpg.mapLoader.getForwardgroud(rpg), -viewArea.x, -viewArea.y, this);

            if(rpg.interfaces != null){
                for(int i=rpg.interfaces.size()-1; i>=0; i--){
                    LWC_AbstractInterface inter = rpg.interfaces.get(i);
                    g.drawImage(inter.show(), inter.getPosition().x, inter.getPosition().y, this);
                }
            }

            if(rpg.pauseInterfaces != null){
                for(int i=0; i<rpg.pauseInterfaces.size(); i++){
                    LWC_AbstractInterface pause = rpg.pauseInterfaces.get(i);
                    g.drawImage(pause.show(), pause.getPosition().x, pause.getPosition().y, this);
                }
            }
        }
    }
}
