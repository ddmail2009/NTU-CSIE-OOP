/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ntu.csie.oop13spring;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class POOUtil {

    static double ABSLimit(double x, int agi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
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
    public static ImageIcon getIcon(String path){
        try {
            return new ImageIcon(new URL("file:/"+path));
        } catch (MalformedURLException ex) {
            System.err.println("Can't read "+path);
            ex.printStackTrace();
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

