package ntu.csie.oop13spring;

import java.awt.*;
import java.util.*;

public abstract class Pet extends POOPet {
    protected MyComp comp;
    protected ArrayList<MyComp> statcomp = new ArrayList<>();
    protected int State = POOConstant.STAT_DOWN;
    
    protected HashMap<String, Object> register = new HashMap<>();
    protected HashMap<String, Integer> timer = new HashMap<>();
    protected Integer []actionkeys = new Integer[7];
    
    protected abstract void init(int HP, int MP, int AGI, String str);
    protected abstract void initImage();
    protected abstract void initComp(Container container);
    protected abstract void initStatComp(Container container);
    public abstract int GetCurrentDirection();
    public abstract void SetCurrentDirection(int direction);
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
    public final void clearRegister(String secret){
        register.remove(secret);
    }
    public final void resetTimer(String secret){
        timer.put(secret, 0);
    }
    public final Integer getTimer(String secret){
        if(timer.get(secret) == null)timer.put(secret, 0);
        return timer.get(secret);
    }
    
    public int damage(Arena arena, Pet from, int number, int attack_type ){
        if( !POOUtil.isStatus(State, POOConstant.STAT_GUARD) || POOUtil.isStatus(attack_type, POOSkillConstant.IGNORE_GUARD) ){
            if(from.getName().equals(getName()))return getHP();
            TimerSkills skill = new BodyBlink();
            boolean active = skill.require(this, (Arena)arena);
            
            if(active){
                skill.startTimer();
                arena.skilllist.add(skill);
            } 
            TimerSkills skill2 = new Message();
            setRegister("Damage", String.format("%d", getHP()-number));
            if(skill2.require(this, (Arena)arena)){
                skill2.startTimer();
                arena.skilllist.add(skill2);
            }
            setHP(number < 0 ? 0 : number);
        }
        return getHP();
    }
    @Override
    public String toString(){
        return String.format("%s, HP:%d MP:%d AGI:%d", getName(), getHP(), getMP(), getAGI());
    }
}