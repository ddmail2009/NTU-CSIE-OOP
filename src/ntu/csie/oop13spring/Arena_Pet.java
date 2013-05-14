package ntu.csie.oop13spring;

import java.awt.Container;
import java.util.ArrayList;

public abstract class Arena_Pet extends POOPet {
    protected MyComp comp;
    protected ArrayList<MyComp> statcomp = new ArrayList<>();
    protected MoveState movestate = new MoveState();
    protected int State = 0;
    protected ArrayList<TimerSkills> skilllist = new ArrayList<>();
    
    protected abstract void init(int HP, int MP, int AGI, String str, MoveState movestate);
    protected abstract void initImage();
    protected abstract void initComp(Container container);
    protected abstract void initStatComp(Container container);
    public abstract void setMovestate(MoveState movestate);
    public abstract int GetCurrentDirection();
    public abstract void SetCurrentDirection(int direction);
    public abstract void draw(Arena arena);
    
    public int getState(){
        return State;
    }
    public void setState(int state){
//        System.out.printf("state=%d, %d\n", State, state);
        State = state;
    }
    
    public void damage(int number){
        if( !POOUtil.isStatus(State, POOConstant.STAT_GUARD) ){
            System.out.println("Hit" + "Current State = "+State);
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