package ntu.csie.oop13spring;

import java.awt.*;
import java.util.*;

public abstract class Pet extends POOPet {
    protected MyComp comp;
    protected ArrayList<MyComp> statcomp = new ArrayList<>();
    protected int State = POOConstant.STAT_DOWN;
    protected ArrayList<TimerSkills> skilllist = new ArrayList<>();
    
    protected abstract void init(int HP, int MP, int AGI, String str);
    protected abstract void initImage();
    protected abstract void initComp(Container container);
    protected abstract void initStatComp(Container container);
    /**
     * Set Actionkeys for the pet
     * @param actionkeys contain corresponding {top, right, down, left, attack, guard, jump}
     */
    public abstract void setActionkeys(Integer []actionkeys);
    public abstract int GetCurrentDirection();
    public abstract void SetCurrentDirection(int direction);
    public abstract void draw(Arena arena);
    
    public int getState(){
        return State;
    }
    public void setState(int state){
        State = state;
    }
    
    public void damage(int number, int attack_type ){
        if( !POOUtil.isStatus(State, POOConstant.STAT_GUARD) || POOUtil.isStatus(attack_type, POOSkillConstant.IGNORE_GUARD) ){
            TimerSkills skill = new BodyBlink();
            if(skill.require(this)){
                skill.startTimer(null);
                skilllist.add(skill);
            }
            setHP(number);
        }
    }
   
    public MyComp getComp(){
        return comp;
    }
    
    @Override
    public String toString(){
        return String.format("%s, HP:%d MP:%d AGI:%d", getName(), getHP(), getMP(), getAGI());
    }
}