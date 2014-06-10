package LWC.pkgfinal.Skill;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Object.*;
import java.util.*;
import java.awt.*;

public class LWC_SkillTalk extends LWC_AbstractSkill {
    public LWC_SkillTalk(LWC_RPG rpg) {
		super(new LWC_TalkEffect(rpg, null), rpg);
    }
    
    @Override
    public boolean skill_require(LWC_AbstractCharacter obj) {
        ArrayList<LWC_AbstractObject> block = new ArrayList<LWC_AbstractObject>();
    	block.add(obj);
    	ArrayList<LWC_AbstractObject> obj_list = LWC_Util.getAllCharacterExcept(rpg, block);
        
        ArrayList<LWC_NPC> result = new ArrayList<>();
        Rectangle p = LWC_Util.getObjArea(rpg, obj);
        if(obj.getRegister("Position").equals("left")) p.x -= p.width/2;
        p.width *= 1.5;
        
        for(int i=0; i<obj_list.size(); i++){
            if(!(obj_list.get(i) instanceof LWC_NPC)) continue;
            Rectangle p2 = LWC_Util.getObjArea(rpg, obj_list.get(i));
            if(LWC_Util.isInside(p, p2)) result.add((LWC_NPC)obj_list.get(i));
        }
    	
        if(result.isEmpty()) return false;
        else{
            LWC_NPC character = result.get((int)Math.random()*(result.size()-1));
            effect = new LWC_TalkEffect(rpg, character.getMsg());
            return true;
        }
    }
}