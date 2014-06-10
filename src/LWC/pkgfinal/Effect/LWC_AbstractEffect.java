package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Underlying effect running below
 */
public abstract class LWC_AbstractEffect implements Serializable{
    /**
     * the main class of LWC_World
     */
    protected LWC_RPG rpg;
    /**
     * the effect secret string, using this to store and get value from LWC_Object
     */
    protected String secret;
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        System.err.println(this + " saved");
        out.defaultWriteObject();       
    }
    private void loadObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        System.err.println(this + " loaded");
        in.defaultReadObject();
    }
    
    
    public LWC_AbstractEffect(LWC_RPG rpg, String secret) {
        this.rpg = rpg;
        this.secret = secret;
    }
    
    /**
     * Get the secret string of this effect
     * @return the secret string
     */
    public String getSecret(){
        return secret;
    }
}


