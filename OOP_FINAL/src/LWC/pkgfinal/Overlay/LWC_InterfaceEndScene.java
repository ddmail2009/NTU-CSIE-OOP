package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;

public class LWC_InterfaceEndScene extends LWC_EndScreen{

    public LWC_InterfaceEndScene(LWC_RPG rpg) {
        super(rpg);
        img[1] = rpg.imgPool().getImage("img/menu/endScene.png");
    }
    
}
