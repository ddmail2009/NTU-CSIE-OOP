/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ntu.csie.oop13spring;

public abstract class Count_Task {
    private int count = 0;
    private int speed;

    public Count_Task(int n) {
        speed = n;
    }
    
    public int run(){
        count = (count+1)%speed;
        if(count == speed-1) return task();
        else return count;
    }
    
    public abstract int task();
}
