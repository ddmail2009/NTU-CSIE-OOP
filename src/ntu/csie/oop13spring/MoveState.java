package ntu.csie.oop13spring;

import java.awt.event.KeyEvent;

public class MoveState {
    public final static int STATE_STOP = 0;
    public final static int STATE_RIGHT = 1;
    public final static int STATE_LEFT = 1<<1;
    public final static int STATE_UP = 1<<2;
    public final static int STATE_DOWN = 1<<3;
    public final static int STATE_LOCKSTOP = 1<<4;
    
    private int state = STATE_STOP;
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
        return (state & STATE_LOCKSTOP) == STATE_LOCKSTOP;
    }
    public boolean setLock(boolean lock){
        if(lock){
            return (state |= STATE_LOCKSTOP)>0;
        }
        else{
            state &= ~STATE_LOCKSTOP;
            return true;
        }
        
    }
    public int []keyDump(){
        int []s = {UP, RIGHT, DOWN, LEFT};
        return s;
    }
}
