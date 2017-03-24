package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import LWC.pkgfinal.AI.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Overlay.*;
import LWC.pkgfinal.Skill.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LWC_Player extends LWC_AbstractCharacter{
    private ArrayList<LWC_BaseItem> itemList = new ArrayList<>();
    private int lv = 0;
    
    public LWC_Player(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        img = new BufferedImage[4][2][];
        
        setRegister("Exp", 32);
        getLv();
        
        ai = new LWC_PlayerAI(rpg, this);
        ((LWC_PlayerAI)ai).addSkills(KeyEvent.VK_SPACE, new LWC_SkillJump(rpg));
        ((LWC_PlayerAI)ai).addSkills(KeyEvent.VK_F, new LWC_SkillIceBlink(rpg));
        ((LWC_PlayerAI)ai).addSkills(KeyEvent.VK_Z, new LWC_SkillTalk(rpg));
        
        img[0][0] = rpg.imgPool().getFolderImages("img/character/Player/runLeft/");
        img[0][1] = LWC_Util.flipImages(img[0][0]);
        img[1][0] = rpg.imgPool().getFolderImages("img/character/Player/standLeft");
        img[1][1] = LWC_Util.flipImages(img[1][0]);
        img[2][0] = rpg.imgPool().getFolderImages("img/character/Player/climb");
        img[2][1] = LWC_Util.flipImages(img[2][0]);
        img[3][1] = rpg.imgPool().getFolderImages("img/character/Player/dizzyLeft");
        img[3][0] = LWC_Util.flipImages(img[3][1]);
        
        LWC_InterfaceHPBar hpbar = new LWC_InterfaceHPBar(rpg);
        hpbar.setMonitor(this);
        rpg.addInterface(hpbar);
        
        LWC_InterfaceMPBar mpbar = new LWC_InterfaceMPBar(rpg);
        mpbar.setMonitor(this);
        rpg.addInterface(mpbar);
        
        LWC_InterfaceExpBar expbar = new LWC_InterfaceExpBar(rpg);
        expbar.setMonitor(this);
        rpg.addInterface(expbar);
        
        LWC_SkillInterface skillbar = new LWC_SkillInterface(rpg);
        skillbar.setPlayer(this);
        rpg.addInterface(skillbar);
    }

    public void addItem(LWC_BaseItem item){
        if(item == null)System.err.println("item is null");
        else if(item.getName() == null ) System.err.println("item name is null");

        if(item instanceof LWC_ConsumableItem) ((LWC_ConsumableItem)item).setObj(this);
        itemList.add(item);
        rpg.addInterface(new LWC_InterfaceHelper(rpg, "獲得 " + item.getName()));
    }
    
    public void removeItem(LWC_BaseItem item){
        itemList.remove(item);
    }
    
    public ArrayList<LWC_BaseItem> getItemList(){
        return itemList;
    }
    
    public int getLv(){
        int actLv = (int) (Math.log((int)getRegister("Exp"))/Math.log(2));
        if(lv != actLv){
            System.err.println("Actual LV = "+actLv);
            lv = actLv;
            rpg.addInterface(new LWC_InterfaceHelper(rpg, String.format("等級上升： lv%d", actLv)));
            setRegister("MaxHP", 25+lv*10);
            setRegister("MaxMP", 25+lv*2);
            setRegister("HP", 25+lv*10);
            setRegister("MP", 25+lv*2);
            setRegister("Power", (int)(10+2*lv));
            setRegister("Defence", (int)(15+lv));
        }
        return actLv;
    }
    
    @Override
    public BufferedImage show() {
        try{
            if(getRegister("Animate") != null){
                BufferedImage []img = (BufferedImage[]) getRegister("Animate");
                int i = (getCounter("Animate")/(int)getRegister("AnimateSpeed"))%img.length;
                if((int)getCounter("Animate") == (int)getRegister("AnimateDuration")) {
                    clearRegister("AnimateDuration");
                    clearRegister("AnimateSpeed");
                    clearCounter("Animate");
                    clearRegister("Animate");
                }
                return img[i];
            }
            throw new Exception("No Animation");
        }catch(Exception e){
            if(getRegister("Grab") != null){
                return img[2][0][(getCounter("Character")/10)%img[2][0].length];
            }
            else{
                switch ((String)getRegister("Position")) {
                    case "left":
                        if (getRegister("dizzy")!=null)
                            return img[3][0][(getCounter("Character")/10)%img[3][0].length];
                        else if(getRegister("run") != null)
                            return img[0][0][(getCounter("Character")/10)%img[0][0].length];
                        else
                            return img[1][0][(getCounter("Character")/10)%img[1][0].length];
                    case "right":
                        if (getRegister("dizzy")!=null)
                            return img[3][1][(getCounter("Character")/10)%img[3][1].length];
                        else if(getRegister("run") != null)
                            return img[0][1][(getCounter("Character")/10)%img[0][1].length];

                        else
                            return img[1][1][(getCounter("Character")/10)%img[1][1].length];
                    default:
                        return img[1][0][(getCounter("Character")/40)%img[1][0].length];
                }
            }
        }
    }
}
