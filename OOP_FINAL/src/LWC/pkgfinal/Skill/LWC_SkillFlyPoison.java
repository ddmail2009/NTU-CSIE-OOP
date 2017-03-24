package LWC.pkgfinal.Skill;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_SkillFlyPoison extends LWC_AbstractSkill implements LWC_InterfaceAttack{
    public LWC_SkillFlyPoison(LWC_RPG rpg) {
        super(new LWC_FlyPoison(rpg), rpg);
        setCD(((LWC_AbstractTime)effect).getDuration());
    }

    @Override
    public boolean skill_require(LWC_AbstractCharacter obj) {
        if(obj.getMP() >= 1){
            obj.setRegister("MP", obj.getMP() - 1);
            effect = new LWC_FlyPoison(rpg);
            ((LWC_AbstractAttack)effect).setUser(obj);
            Point ObjCenter = LWC_Util.getCenter(rpg, obj);
            switch ((String)obj.getRegister("Position")) {
                case "left":
                    ((LWC_FlyPoison)effect).setPosition(new Point(ObjCenter.x - LWC_Util.getObjArea(rpg, obj).width/4, ObjCenter.y));
                    break;
                case "right":
                    ((LWC_FlyPoison)effect).setPosition(new Point(ObjCenter.x + LWC_Util.getObjArea(rpg, obj).width/4, ObjCenter.y));
                    break;
            }
            
            return true;
        }
        else return false;
    }
}
