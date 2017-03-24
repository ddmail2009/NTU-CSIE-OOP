package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/** General Object of LWC_World */
public abstract class LWC_AbstractObject implements Serializable{
    private void writeObject(ObjectOutputStream out) throws IOException {
        System.err.println(this + " saved");
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.err.println(this + " loaded");
        in.defaultReadObject();
    }
    
    
    /** the main class of LWC_World */
    protected LWC_RPG rpg = null;
    private HashMap<String, Object> register = new HashMap<>();
    private HashMap<String, Integer> counter = new HashMap<>();

    /** Default constructor of LWC_AbstractObject
     *  @param rpg the main class for LWC_World
     */
    public LWC_AbstractObject(LWC_RPG rpg) {
        this.rpg = rpg;
    }
    
    /** 
     *  Get Register which was previously set by the secret
     *  @param secret the secret key of the object
     *  @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public Object getRegister(String secret){
        return register.get(secret);
    }
    /** 
     * set Register by the previously key
     * @param secret the secret key for the object
     * @param obj the object which want to store
     */
    public void setRegister(String secret, Object obj){
        register.put(secret, obj);
    }
    /**
     * clear the Register to null
     * @param secret the secret key
     */
    public void clearRegister(String secret){
        register.remove(secret);
    }
    
    /**
     * Get the counter value by the secret key
     * @param secret the secret key of the counter
     * @return the counter to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public Integer getCounter(String secret){
        return counter.get(secret);
    }
    
    /**
     * Set the counter value by the secret key
     * @param secret the secret key of the counter
     * @param n the counter value
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public Integer setCounter(String secret, int n){
        return counter.put(secret, n);
    }
    /**
     * Clear the counter value and set it to null
     * @param secret the secret key of the counter
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public Integer clearCounter(String secret){
        return counter.remove(secret);
    }
        
    /**
     * Update the object's status, counter and do the corresponding action.
     * @return false if the object is in the end state(which means no updates anymore, and rpg will remove it)
     */
    public boolean update(){
        for (String string : counter.keySet())
            counter.put(string, counter.get(string)+1);
        return action();
    }
    /**
     * Do the action method
     * @return false if the object is in the end state(which means no updates anymore, and rpg will remove it)
     */
    public abstract boolean action();
    public abstract BufferedImage show();
}
