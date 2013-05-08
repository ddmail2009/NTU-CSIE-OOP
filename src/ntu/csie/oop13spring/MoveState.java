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
    
    public boolean isSTOP(){
        if(isLock())return true;
        else return (state & STATE_STOP) == STATE_STOP;
    }
    public boolean isRight(){
        if(isLock())return false;
        else return (state & STATE_RIGHT) == STATE_RIGHT;
    }
    public boolean isLeft(){
        if(isLock())return false;
        else return (state & STATE_LEFT) == STATE_LEFT;
    }
    public boolean isUP(){
        if(isLock())return false;
        else return (state & STATE_UP) == STATE_UP;
    }
    public boolean isDOWN(){
        if(isLock())return false;
        else return (state & STATE_DOWN) == STATE_DOWN;
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
    public boolean setState(int keycode){
        int action = checkKey(keycode);
        if(action == STATE_STOP) return false;
        state |= action;
        return true;
    }
    public boolean delState(int keycode){
        int action = checkKey(keycode);
        if(action == STATE_STOP) return false;
        state &= ~action;
        return true;
    }
    public int []keyDump(){
        int []s = {UP, RIGHT, DOWN, LEFT};
        return s;
    }
    
    private int checkKey(int keycode){
        if(keycode == UP) return STATE_UP;
        else if(keycode == DOWN) return STATE_DOWN;
        else if(keycode == LEFT) return STATE_LEFT;
        else if(keycode == RIGHT) return STATE_RIGHT;
        else return STATE_STOP;
    }
}
