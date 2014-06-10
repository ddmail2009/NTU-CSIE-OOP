package LWC.pkgfinal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class LWC_Keys implements KeyListener{
    transient private HashSet<Integer> currentKey = new HashSet<>();
    transient private HashMap<Integer, ArrayList<Integer>> releaseKey = new HashMap<>();
    transient private HashMap<Integer, Integer> typedKey = new HashMap<>();
    transient private HashMap<Integer, Integer> pressedKey = new HashMap<>();
    
    private int repeatRate = 5;
    private int lagTime = 50;
    
    private int counter;

    synchronized public void update(){
        counter ++;
        
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> iter = releaseKey.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Integer, ArrayList<Integer>> entry = iter.next();
            if(entry.getKey() < counter - 60) iter.remove();
        }
    }
    
    synchronized public boolean isPressed(int key){
        if(pressedKey.containsKey(key)){
            int time = counter - pressedKey.get(key);
            if(getReleasedKey(time).contains(key)) {
                pressedKey.remove(key);
                return false;
            }
            else if(time > lagTime && (time - lagTime)%repeatRate == 0) return true;
        }
        else if(getCurrentKey().contains(key)){
            pressedKey.put(key, counter);
            return true;
        }
        return false;
    }
    
    synchronized public boolean isTyped(int key){
        if(typedKey.containsKey(key)) {
            int time = counter - typedKey.get(key);
            if(time > 300) typedKey.remove(key);
            else if(getReleasedKey(time).contains(key))
                typedKey.remove(key);
            return false;
        }
        else if(getCurrentKey().contains(key)){
            typedKey.put(key, counter);
            return true;
        }
        else return false;
    }
    
    @Override
    synchronized public void keyTyped(KeyEvent e) {
        ;
    }
    
    @Override
    synchronized public void keyPressed(KeyEvent e) {
        currentKey.add(e.getKeyCode());
    }
    
    @Override
    synchronized public void keyReleased(KeyEvent e) {
        currentKey.remove(e.getKeyCode());
        
        ArrayList<Integer> keys = new ArrayList<>();
        if(releaseKey.containsKey(counter))
            keys = releaseKey.get(counter);
        
        keys.add(e.getKeyCode());
        releaseKey.put(counter, keys);
    }
    
    synchronized public HashSet<Integer> getCurrentKey(){
        return currentKey;
    }
    @SuppressWarnings("unchecked")
    synchronized public HashSet<Integer> getReleasedKey(int timeAgo){    
        HashSet<Integer> result = new HashSet<>();
        for (Map.Entry<Integer, ArrayList<Integer>> entry : releaseKey.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<Integer> arrayList = entry.getValue();
            if(integer > counter - timeAgo)
                for (Integer key : arrayList)
                    result.add(key);
        }
        return result;
    }
}
