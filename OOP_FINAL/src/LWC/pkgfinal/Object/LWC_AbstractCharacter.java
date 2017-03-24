package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import LWC.pkgfinal.AI.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Loader.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

public abstract class LWC_AbstractCharacter extends LWC_AbstractObject{
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(img.length);
        for(int i=0; i<img.length; i++){
            out.writeInt(img[i].length);
            for(int j=0; j<img[i].length; j++){
                out.writeInt(img[i][j].length);
                for(int k=0; k<img[i][j].length; k++)
                    out.writeObject(new ImageIcon(img[i][j][k]));
            }       
        }
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int imgI = in.readInt();
        img = new BufferedImage[imgI][][];
        for(int i=0; i<img.length; i++){
            int imgJ = in.readInt();
            img[i] = new BufferedImage[imgJ][];
            for(int j=0; j<img[i].length; j++){
                int imgK = in.readInt();
                img[i][j] = new BufferedImage[imgK];
                for(int k=0; k<img[i][j].length; k++)
                    img[i][j][k] = LWC_Util.Image2BufferedImage(((ImageIcon) in.readObject()).getImage());
            }
        }
    }
    
    transient protected BufferedImage [][][]img = null;
    protected String name, group;
    protected LWC_AbstractAI ai = new LWC_EmptyAI(rpg, this);
    ItemLoader loader = new ItemLoader("config/Item.txt");

    public LWC_BaseWeapon weapon = (LWC_BaseWeapon)loader.getItem(rpg,"BasicWeapon");
    //new LWC_BasicWeapon(rpg);
    //player.addItem(loader.getItem(rpg,"BasicWeapon"));

    static private HashMap<String, String> npc_condition = new HashMap<String, String>();

    public void setCondition(String condition, boolean init){
        if(init && !npc_condition.containsKey(name))
            npc_condition.put(name,condition);
    }
    public void setCondition(String condition){
        npc_condition.put(name,condition);
    }
    public String getCondition(){
        return npc_condition.get(name);
    }
    public LWC_AbstractAI getAI(){
        return ai;
    }
    public Rectangle getPrefferedSize(){
        return new Rectangle(new Point(), LWC_Util.getImgDim(show()));
    }
    
    public LWC_AbstractCharacter(String name, String group, LWC_RPG rpg) {
        super(rpg);
        this.name = name;
        this.group = group;
        setCounter("Character", 0);
        setRegister("Position", "right");
        setRegister("MaxMP", 25);
        setRegister("MaxHP", 25);
        setRegister("HP", 25);
        setRegister("MP", 25);
        setRegister("Power", 5);
        setRegister("Defence", 10);
    }
    public LWC_AbstractAI setAI(LWC_AbstractAI ai){
        LWC_AbstractAI prev_AI = this.ai;
        this.ai = ai;
        return prev_AI;
    }
    public boolean isDead(){
        return (int)getRegister("HP")<=0;
    }
    public int getHP(){
        return (int)getRegister("HP");
    }
    public int getMP(){
        return (int)getRegister("MP");
    }
    public void setMP(int MP){
        if(MP > (int)getRegister("MaxMP")) MP = (int)getRegister("MaxMP");
        else if(MP <= 0) MP = 0;
        setRegister("MP", MP);
    }
    public void setHP(int HP){
        if(HP > (int)getRegister("MaxHP")) HP = (int)getRegister("MaxHP");
        else if(HP <= 0) HP = 0;
        setRegister("HP", HP);
    }
    public String getName(){
        return name;
    }
    public String getGroup(){
        return group;
    }
    public void setAnimate(BufferedImage []img, int speed, int duration){
        if(img == null) setRegister("Animate", "true");
        else setRegister("Animate", img);
        setCounter("Animate", 0);
        setRegister("AnimateSpeed", speed);
        setRegister("AnimateDuration", duration);
    }
    @Override
    public boolean action(){
        if(getCounter("Character")%100 == 0) setMP(getMP()+1);
        
        if(ai == null){
            System.err.println("Character must have an AI, Object destroyed");
            return false;
        }
        else return ai.action();
    }
    public final void damage(int effectDamage, LWC_AbstractCharacter source){
        clearRegister("Back");
        if(this instanceof LWC_NPC) return ;
        
        int def = (int)getRegister("Defence");
        int pow = (int)source.getRegister("Power");
        int weaponDamage = source.weapon == null ? 1 : source.weapon.getDamage();
        int true_damage = (int) 1.0*pow*weaponDamage*effectDamage/def;
        if(true_damage <= 0) true_damage = 1;
        
        setHP(getHP() - true_damage);
        
        if(getHP() > 0){
            LWC_BlinkEffect blink = new LWC_BlinkEffect(rpg, this);
            blink.setSpeed(5);
            blink.setDuration(15);
            rpg.addEffect(blink);
            rpg.addEffect(new LWC_HurtNumber(rpg, this, true_damage));

            if(this instanceof LWC_Monster)
                rpg.addEffect(new LWC_MonsterHPBar(rpg, this));
        }
    }
}
