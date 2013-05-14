package ntu.csie.oop13spring;

import java.awt.event.KeyEvent;

public class MoveState {    
    private int state = POOConstant.STAT_STOP;
    private int UP = KeyEvent.VK_UP;
    private int DOWN = KeyEvent.VK_DOWN;
    private int LEFT = KeyEvent.VK_LEFT;
    private int RIGHT = KeyEvent.VK_RIGHT;

    public MoveState() {
        ;
    }
    
    public MoveState(int UP, int DOWN, int LEFT, int RIGHT) {
        this.UP   = UP;
        this.DOWN = DOWN;
        this.LEFT = LEFT;
        this.RIGHT = RIGHT;
    }
    
    public boolean isRight(int key){
        if(isLock())return false;
        else if(key == RIGHT)return true;
        else return false;
    }
    public boolean isLeft(int key){
        if(isLock())return false;
        else if(key == LEFT)return true;
        else return false;
    }
    public boolean isUP(int key){
        if(isLock())return false;
        else if(key == UP)return true;
        else return false;
    }
    public boolean isDOWN(int key){
        if(isLock())return false;
        else if(key == DOWN)return true;
        else return false;
    }
    public boolean isLock(){
        return POOUtil.isStatus(state, POOConstant.STAT_LOCK);
    }
    public boolean setLock(boolean lock){
        if(lock) state = POOUtil.setStatus(state, POOConstant.STAT_LOCK);
        else state = POOUtil.delStatus(state, POOConstant.STAT_LOCK);
        return lock;
    }
    public int []keyDump(){
        int []s = {UP, RIGHT, DOWN, LEFT};
        return s;
    }
}
