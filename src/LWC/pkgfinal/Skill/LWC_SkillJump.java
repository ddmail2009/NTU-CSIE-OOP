package LWC.pkgfinal.Skill;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Object.*;

public class LWC_SkillJump extends LWC_AbstractSkill {

    @Override
    public boolean skill_require(LWC_AbstractCharacter obj) {
        this.effect = new LWC_Jump(rpg, obj);
        if(obj.getRegister("Hold") == null)
            return true;
        else return false;
    }
    
    public LWC_SkillJump(LWC_RPG rpg) {
        super(new LWC_Jump(rpg, null), rpg);
    }
    
}
