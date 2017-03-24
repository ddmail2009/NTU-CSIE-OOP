package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public abstract class Pet extends POOPet {
    protected POOJComp comp;
    protected ArrayList<POOJComp> statcomp = new ArrayList<>();
    protected int State = POOConstant.STAT_DOWN;
    
    /**
     * The register for Time Effect
     */
    protected HashMap<String, Object> register = new HashMap<>();
    /**
     * the registered timer for Time Effect
     */
    protected HashMap<String, Integer> timer = new HashMap<>();
    /**
     * actionkeys of the pet, {UP, RIGHT, DOWN, LEFT, ATTACK, GUARD, JUMP}
     */
    protected Integer []actionkeys = new Integer[7];
    
    /**
     * INITIALIZATION OF PET
     * @param HP HP
     * @param MP MP
     * @param AGI AGI
     * @param str NAME
     */
    protected abstract void init(int HP, int MP, int AGI, String str);
    /**
     * initial the image icon 
     */
    protected abstract void initImage();
    /**
     * Initialize Main Component
     * @param container Swing Container
     */
    protected abstract void initComp(Container container);
    /**
     * initial status component
     * @param container Swing Container
     */
    protected abstract void initStatComp(Container container);
    /**
     * get current pet direction
     * @return current direction
     */
    public abstract int GetCurrentDirection();
    /**
     * set current direction
     * @param direction the direction
     */
    public abstract void SetCurrentDirection(int direction);
    /**
     * draw the image of the pet
     * @param arena arena
     */
    public abstract void draw(Arena arena);
    
    public final int getState(){
        return State;
    }
    public final void setState(int state){
        State = state;
    }
    public final void setRegister(String secret, Object n){
        register.put(secret, n);
    }
    public final Object getRegister(String secret){
        return register.get(secret);
    }
    public final Object getClearRegister(String secret){
        Object t = getRegister(secret);
        clearRegister(secret);
        return t;
    }
    public final void clearRegister(String secret){
        register.remove(secret);
    }
    public final Integer getTimer(String secret){
        return timer.get(secret);
    }
    public final Integer clearTimer(String secret){
        return timer.remove(secret);
    }
    public final Integer setTimer(String secret, int n){
        return timer.put(secret, n);
    }
    
    public int damage(Arena arena, Pet from, int number, int attack_type ){
        if( !POOUtil.isStatus(State, POOConstant.STAT_GUARD) || POOUtil.isStatus(attack_type, POOSkillConstant.IGNORE_GUARD) ){
            if( !POOUtil.isStatus(attack_type, POOSkillConstant.SELF_ATTACK) && from.getName().equals(getName()))return getHP();
            TimerEffects skill = new BodyBlink();
            boolean active = skill.require(this, (Arena)arena);
            
            if(active){
                skill.startTimer();
                arena.skillList.add(skill);
            } 
            TimerEffects skill2 = new Message();
            setRegister("Message", String.format("%d", getHP()-number));
            if(getRegister("MessageColor") == null) 
                setRegister("MessageColor", new Color(255, 0, 110));
            if(skill2.require(this, (Arena)arena)){
                skill2.startTimer();
                arena.skillList.add(skill2);
            }
            setHP(number < 0 ? 0 : number);
        }
        return getHP();
    }
    
    public String getActionKeyString(){
        return String.format("UP: %s, RIGHT: %s, DOWN: %s, LEFT: %s, ATK: %s, GUARD: %s, JUMP: %s", KeyEvent.getKeyText(actionkeys[0]), KeyEvent.getKeyText(actionkeys[1]), KeyEvent.getKeyText(actionkeys[2]),
                                                                                                   KeyEvent.getKeyText(actionkeys[3]), KeyEvent.getKeyText(actionkeys[4]), KeyEvent.getKeyText(actionkeys[5]),
                                                                                                   KeyEvent.getKeyText(actionkeys[6]));
                                                                                                       
    }
    @Override
    public String toString(){
        return String.format("%s, HP:%d MP:%d AGI:%d", getName(), getHP(), getMP(), getAGI());
    }
}