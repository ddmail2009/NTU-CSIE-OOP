/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import java.awt.Color;
import java.awt.Point;

public class LWC_InterfaceMPBar extends LWC_InterfaceHPBar {
    public LWC_InterfaceMPBar(LWC_RPG rpg) {
        super(rpg);
        setColor(Color.blue);
    }
    
    @Override
    public Point getPosition() {
        return new Point(10, rpg.getHeight() - 20);
    }
    
    @Override
    public int monitorValue() throws ClassCastException{
        return (int)monitor.getRegister("MP");
    }
    
    @Override
    public int monitorMaxValue() throws ClassCastException{
        return (int)monitor.getRegister("MaxMP");
    }
}
