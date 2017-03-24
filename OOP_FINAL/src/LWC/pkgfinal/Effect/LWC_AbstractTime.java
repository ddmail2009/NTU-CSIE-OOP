package LWC.pkgfinal.Effect;
import LWC.pkgfinal.*;

/**
 * Effect that need time to accomplish
 */
public abstract class LWC_AbstractTime extends LWC_AbstractEffect {
    /** 
     * Counter of the effect
     */
    protected int counter = 0;
    private int duration = 30;

    /**
     * The default constructor of a time effect
     * @param rpg the main class of LWC_World
     * @param secret the secret string
     * @param who Indicate who launched this effect
     */
    public LWC_AbstractTime(LWC_RPG rpg, String secret) {
        super(rpg, secret);
    }
    
    /**
     * update the skill, counter will add automatically
     * @return false if the effect is in its end state
     */
    public final boolean update(){
        counter = counter + 1;
        return effect_update();
    }
    /**
     * effect update itself
     * @return false if the effect is in its end state
     */
    protected abstract boolean effect_update();

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
