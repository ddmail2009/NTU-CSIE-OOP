package LWC.pkgfinal.Object;

import java.awt.image.BufferedImage;
import LWC.pkgfinal.LWC_RPG;

public class LWC_ObjectPortal extends LWC_AbstractObject {

    public LWC_ObjectPortal(LWC_RPG rpg) {
        super(rpg);
        setCounter("Portal", 0);
    }

    @Override
    public boolean action() {
        return true;
    }

    @Override
    public BufferedImage show() {
        BufferedImage[] portal = rpg.imgPool().getFolderImages("img/portal");
        return portal[(getCounter("Portal")/10)%portal.length];
    }
}
