/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ntu.csie.oop13spring;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class POOUtil {
    private POOUtil(){}
    
    public static String getCWD(){
        return System.getProperty("user.dir")+File.separator;
    }
    public static boolean isStatus(int a, int status){
        return ( (a & status) == status );
    }
    public static int setStatus(int a, int status){
        return a | status;
    }
    public static int delStatus(int a, int status){
        return a - (a & status);
    }
    public static BufferedImage getImage(String path){
        try{
            return ImageIO.read(new File(path));
        }catch(Exception e){
            System.err.println(path + "  Can't read");
            e.printStackTrace();
            return null;
        }
    }
    public static BufferedImage flipHorizontal(BufferedImage img){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        return new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(img, null);
    }
    public static int getDirection(int state){
        if(isStatus(state, POOConstant.STAT_DOWN))return POOConstant.STAT_DOWN;
        else if(isStatus(state, POOConstant.STAT_UP))return POOConstant.STAT_UP;
        else if(isStatus(state, POOConstant.STAT_LEFT))return POOConstant.STAT_LEFT;
        else if(isStatus(state, POOConstant.STAT_RIGHT))return POOConstant.STAT_RIGHT;
        else return POOConstant.STAT_STOP;
    }
    public static boolean isInside(int low, int middle, int high){
        return (low<=middle && middle<high);
    }
    public static int ABSLimit(int n, int limit){
        return n>limit ? limit : (n < -limit ? -limit : n);
    }
}

