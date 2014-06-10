package LWC.pkgfinal.Object;

import LWC.pkgfinal.AI.LWC_NPCAI;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Overlay.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Skill.*;
import LWC.pkgfinal.AI.*;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;



public class LWC_NPC_Dragon extends LWC_NPC {
    public LWC_NPC_Dragon(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        setCondition("Self");
        if(!msg.load_conversation(name, rpg.getPlotState()))
            msg.load_conversation(name,getCondition());
        else
            setCondition(rpg.getPlotState());
    }
    @Override
    public LWC_AbstractInterface getMsg(){
        if(!msg.load_conversation(name, rpg.getPlotState())){
            msg.load_conversation(name, getCondition());
            if(getCondition().equals("Self")){
                LWC_Player player = rpg.getPlayer();
                LWC_PlayerAI ai = (LWC_PlayerAI)player.getAI();
                ai.addSkills(KeyEvent.VK_F, new LWC_SkillBatAttack(rpg));
                rpg.addInterface(new LWC_InterfaceHelper(rpg, "獲得技能 「蝙蝠拳」"));
            }
        }
        else
            setCondition(rpg.getPlotState());
        setCondition("Self1");

        return msg;
    }
}