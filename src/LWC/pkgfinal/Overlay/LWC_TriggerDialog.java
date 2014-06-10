/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;

public class LWC_TriggerDialog extends LWC_TriggerMessage {

    public LWC_TriggerDialog(LWC_RPG rpg) {
        super(rpg);
        setImage(rpg.imgPool().getImage("img/background/background1_trigger2.png"));
    }
    
    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        LWC_InterfaceDialog diag = new LWC_InterfaceDialog(rpg);
        diag.setStr("A door, may have a monster behind it, Be careful!!!");
        rpg.addPauseInterface(diag);
        rpg.removeTriggerOverlay(this);
    }
}
