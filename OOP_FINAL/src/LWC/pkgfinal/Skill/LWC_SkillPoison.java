package LWC.pkgfinal.Skill;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_SkillPoison extends LWC_AbstractSkill implements LWC_InterfaceAttack{
    public LWC_SkillPoison(LWC_RPG rpg) {
        super(new LWC_Poison(rpg), rpg);
        setCD(((LWC_AbstractTime)effect).getDuration());
    }

    @Override
    public boolean skill_require(LWC_AbstractCharacter obj) {
        
        if(obj.getMP() >= 1){
            obj.setRegister("MP", obj.getMP() - 1);
            effect = new LWC_Poison(rpg);
            ((LWC_AbstractAttack)effect).setUser(obj);
            Point ObjCenter = LWC_Util.getCenter(rpg, obj);
            switch ((String)obj.getRegister("Position")) {
                case "left":
                    ((LWC_Poison)effect).setPosition(new Point(ObjCenter.x, ObjCenter.y));
                    break;
                case "right":
                    ((LWC_Poison)effect).setPosition(new Point(ObjCenter.x, ObjCenter.y));
                    break;
            }
            
            return true;
        }
        else return false;
    }
}
