/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ntu.csie.oop13spring;

import java.awt.Rectangle;
import java.awt.image.*;
import java.io.File;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * General Function of this project
 * @author Dan
 */
public class POOUtil {
    private POOUtil(){}
    
    /**
     * Get the current working Directory
     * @return Current Working Directory
     */
    public static String getCWD(){
        return System.getProperty("user.dir")+File.separator;
    }
    /**
     * Check status based on POOConstant
     * @param a status to be checked
     * @param status checking status type
     * @return true if a is of status type.
     */
    public static boolean isStatus(int a, int status){
        return ( (a & status) == status );
    }
    /**
     * Set a with status type
     * @param a orginal status
     * @param status status type to be added
     * @return new status with the status type being saved
     */
    public static int setStatus(int a, int status){
        return a | status;
    }
    /**
     * remove status type from a
     * @param a original status
     * @param status status type to be removed
     * @return new status without the status type
     */
    public static int delStatus(int a, int status){
        return a - (a & status);
    }
    /**
     * Get BufferedImage
     * @param path path to the image
     * @return BufferedImage
     */
    public static BufferedImage getImage(String path){
        try{
            return ImageIO.read(new File(path));
        }catch(Exception e){
            System.err.println(path + "  Can't read");
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Get ImageIcon
     * @param path path to the image
     * @return ImageIcon
     */
    public static ImageIcon getIcon(String path){
        try {
            return new ImageIcon(new URL("file:/"+path));
        } catch (MalformedURLException ex) {
            System.err.println("Can't read "+path);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get the direction based on the state
     * @param state state to be checked
     * @return derived direction based on the state
     */
    public static int getDirection(int state){
        if(isStatus(state, POOConstant.STAT_UP))return POOConstant.STAT_UP;
        else if(isStatus(state, POOConstant.STAT_RIGHT))return POOConstant.STAT_RIGHT;
        else if(isStatus(state, POOConstant.STAT_DOWN))return POOConstant.STAT_DOWN;
        else if(isStatus(state, POOConstant.STAT_LEFT))return POOConstant.STAT_LEFT;
        else return POOConstant.STAT_STOP;
    }
    /**
     * Check if low  smaller than middle smaller than high
     * @param low low
     * @param middle middle
     * @param high high
     * @return true if in order
     */
    public static boolean isInside(int low, int middle, int high){
        return (low<=middle && middle<high);
    }
    /**
     * if n is bigger than limit or n is smaller than negative limit, return limit or negative limit
     * @param n n
     * @param limit limit
     * @return the fixed n
     */
    public static int ABSLimit(int n, int limit){
        return n>limit ? limit : (n < -limit ? -limit : n);
    }
    /**
     * Find if a is collapse with b
     * @param a checking area
     * @param b checking area
     * @return true if two area collapse
     */
    public static boolean isBlockInside(Rectangle a, Rectangle b){
        int a_area = a.height*a.width;
        int b_area = b.height*b.width;
        Rectangle small = a, big = b;
        if(b_area < a_area){
            Rectangle tmp = small;
            small = big;
            big = tmp;
        }
        if(isInside(big.x, small.x, big.x+big.height) && isInside(big.y, small.y, big.y+big.width))return true;
        if(isInside(big.x, small.x, big.x+big.height) && isInside(big.y, small.y+small.width, big.y+big.width))return true;
        if(isInside(big.x, small.x+small.height, big.x+big.height) && isInside(big.y, small.y, big.y+big.width))return true;
        if(isInside(big.x, small.x+small.height, big.x+big.height) && isInside(big.y, small.y+small.width, big.y+big.width))return true;
        return false;
    }
}

