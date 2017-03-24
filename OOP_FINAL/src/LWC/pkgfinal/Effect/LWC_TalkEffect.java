package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Overlay.*;

public class LWC_TalkEffect extends LWC_AbstractEffect implements LWC_Effect2Null{
    //LWC_InterfaceNPCMsg msg;
    LWC_AbstractInterface msg;
    public LWC_TalkEffect(LWC_RPG rpg, LWC_AbstractInterface msg) {
        super(rpg, "Talk");
        this.msg = msg;
    }

    @Override
    public void act() {
        rpg.addPauseInterface(msg);
    }
}