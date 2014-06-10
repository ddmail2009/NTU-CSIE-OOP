package LWC.pkgfinal.AI;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Skill.LWC_AbstractSkill;
import LWC.pkgfinal.Skill.LWC_InterfaceAttack;
import LWC.pkgfinal.Skill.LWC_SkillTalk;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

public class LWC_PlayerAI extends LWC_AbstractAI {
    protected HashMap<Integer, LWC_AbstractSkill> skillset = new HashMap<>();
    protected ArrayList<LWC_AbstractSkill> attackList = new ArrayList<>();
    
    public LWC_PlayerAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        super(rpg, obj);
    }
    
    public BufferedImage[] getAttackIcon(){
        BufferedImage []result = new BufferedImage[attackList.size()];
        for(int i=0; i<attackList.size(); i++)
            result[i] = attackList.get(i).getIcon();
        return result;
    }
    
    public void addSkills(int key, LWC_AbstractSkill skill){
        if(skill instanceof LWC_InterfaceAttack && skill instanceof LWC_AbstractSkill){
            if(attackList.isEmpty())
                skillset.put(key, skill);
            attackList.add((LWC_AbstractSkill)skill);
        }
        else if(skillset.containsKey(key))
            throw new IllegalArgumentException("Duplicate Key " + key);
        else skillset.put(key, skill);
    }
    public void clearSkills(int key, LWC_AbstractSkill skill){
        skillset.clear();
    }

    @Override
    public Point move() {
        if(obj.getRegister("Hold") == null && obj.getRegister("Animate") == null){
            HashSet<Integer> currentKey = rpg.getKeys().getCurrentKey();
            Point p = new Point(0, 0);

            if(currentKey.contains(KeyEvent.VK_RIGHT)) p.x += 8;
            if(currentKey.contains(KeyEvent.VK_LEFT)) p.x -= 8;
            if(p.x != 0) obj.setRegister("run", true);
            else obj.clearRegister("run");

            if(p.x > 0) obj.setRegister("Position", "right");
            else if(p.x < 0) obj.setRegister("Position", "left");

            if(p.x != 0) p.y -= 3;
            return p;
        }
        else {
            obj.clearRegister("run");
            return new Point();
        }
    }

    @Override
    public boolean AIaction() {
        if(obj.getHP() > 0){
            if(rpg.getKeys().isTyped(KeyEvent.VK_CONTROL)){
                LWC_AbstractSkill shift = attackList.get(0);
                attackList.remove(0);
                attackList.add(shift);
                
                skillset.put(KeyEvent.VK_F, attackList.get(0));
            }
            
            for (Integer integer : skillset.keySet()) {
                if(rpg.getKeys().isTyped(integer) && skillset.get(integer).require(obj) == true){
                    skillset.get(integer).register(obj);
                    return true;
                }
            }
            return true;
        }
        else return false;
    }
    
}
