package LWC.pkgfinal.Skill;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Object.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;


/**
 * Skill Class Which wrap Effect with requirement
 */
abstract public class LWC_AbstractSkill implements Serializable{
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
        out.writeObject(new ImageIcon(img));
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        img = LWC_Util.Image2BufferedImage(((ImageIcon)in.readObject()).getImage());
    }
    
    protected LWC_AbstractEffect effect;
    protected LWC_RPG rpg = null;
    transient protected BufferedImage img = null;
    protected int CD = 0;
    
    public LWC_AbstractSkill(LWC_AbstractEffect effect, LWC_RPG rpg) {
        this.rpg = rpg;
        this.effect = effect;
        img = rpg.imgPool().getImage("img/skill/defaultSkill.png");
    }
    
    public BufferedImage getIcon(){
        return img;
    }
    
    public void register(LWC_AbstractObject obj){
        rpg.addEffect(effect);
    }
    
    public void setCD(int cd){
        this.CD = cd;
    }
    
    public boolean require(LWC_AbstractCharacter obj){
        if(obj.getCounter(effect.getSecret()) != null)
            if(obj.getCounter(effect.getSecret()) < CD)
                return false;
        obj.setCounter(effect.getSecret(), 0);
        return skill_require(obj);
    }
    
    public abstract boolean skill_require(LWC_AbstractCharacter obj);
}