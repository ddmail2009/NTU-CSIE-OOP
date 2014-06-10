package LWC.pkgfinal.Skill;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_SkillBossSwing extends LWC_AbstractSkill implements LWC_InterfaceAttack{
    public LWC_SkillBossSwing(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        super(new LWC_BossSwing(rpg, obj), rpg);
        setCD(((LWC_AbstractTime)effect).getDuration());
    }

    @Override
    public boolean skill_require(LWC_AbstractCharacter obj) {
        if(obj.getMP() >= 0){
            obj.setRegister("MP", obj.getMP() - 0);
            effect = new LWC_BossSwing(rpg, obj);
            return true;
        }
        else return false;
    }
}
