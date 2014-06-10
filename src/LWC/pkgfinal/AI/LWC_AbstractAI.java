package LWC.pkgfinal.AI;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.Point;
import java.io.Serializable;

/**
 * General AI class for LWC_World
 */
public abstract class LWC_AbstractAI implements Serializable{
    /**
     * the main class of LWC_World
     */
    protected LWC_RPG rpg;
    /**
     * the Object which has this AI
     */
    protected LWC_AbstractCharacter obj;
    /**
     * Constructor for the general AI class
     * @param rpg the main class of LWC_World
     * @param obj the Object which has this AI
     */
    public LWC_AbstractAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        this.rpg = rpg;
        this.obj = obj;
    }

    
    public boolean action(){
        Point moveVector = move();
        rpg.movePosition(obj, moveVector, true);
        return AIaction();
    }
    
    public abstract Point move();
    /**
     * Define How to act
     * @return false if this object is in its end state
     */
    public abstract boolean AIaction();
}
