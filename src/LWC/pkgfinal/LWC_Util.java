package LWC.pkgfinal;

import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 * General Utility of LWC_World
 * @author daniel
 */
public final class LWC_Util {

    public static BufferedImage Image2BufferedImage(Image image) {
        BufferedImage tmp = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return tmp;
    }

    public static String readFile(String fileName) {
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            result = reader.readLine();
            reader.close();
        } catch (Exception e) {
            System.err.println("read file " + fileName + " failed");
            e.printStackTrace();
        }
        return result;
    }
    private LWC_Util(){}
    /**
     * Get the current working directory(CWD)
     * @return the current working directory, ex: '/user/home/LWC_WORLD/'
     */
    public static String getCWD(){
        return System.getProperty("user.dir")+getSep();
    }
    /**
     * Get the separator of this system
     * @return '/' if in Linux, '\' if in Windows
     */
    public static String getSep(){
        return File.separator;
    }
    /**
     * check the parameter's order
     * @param small the smallest number
     * @param middle the meddle number
     * @param large the large number
     * @return true if small is smaller than or equal to middle and middle is smaller than large
     */
    public static boolean isInside(int small, int middle, int large){
        return (small <= middle) && (middle < large);
    }
    /**
     * Return the Point which indicate the vector a to b
     * @param a
     * @param b
     * @return new Point(b.x-a.x, b.y-a.y);
     */
    public static Point getVector(Point a, Point b){
        return new Point(b.x - a.x, b.y - a.y);
    }
    /**
     * Check if point in block
     * @param rec the block rectangle
     * @param p the point 
     * @return true if point is in the block
     */
    public static boolean isPointInBlock(Rectangle rec, Point p){
        if(isInside(rec.x, p.x, rec.x+rec.width) && isInside(rec.y, p.y, rec.y+rec.height))return true;
        else return false;
    }
    /**
     * Check if the two block is intersect with each other or not
     * @param a the first rectangle
     * @param b the second rectangle
     * @return true if two blocks are intersected with each other
     */
    public static boolean isInside(Rectangle a, Rectangle b){
        return a.intersects(b);
    }
    
    public static boolean isBlockInside(Rectangle outerBlock, Rectangle innerBlock){
        if( (outerBlock.x < innerBlock.x && outerBlock.x + outerBlock.width > innerBlock.x + innerBlock.width) &&
             (outerBlock.y < innerBlock.y && outerBlock.y + outerBlock.height > outerBlock.y + outerBlock.height) )
            return true;
        else return false;
    }
    /**
     * Get the image Size
     * @param img the image
     * @return return the image Size = new Dimension(width, height);
     */
    public static Dimension getImgDim(BufferedImage img){
        return new Dimension(img.getWidth(), img.getHeight());
    }
    /**
     * Get the resized image
     * @param img the original image
     * @param size the target size
     * @return the resized image
     */
    public static BufferedImage getResizedImage(BufferedImage img, Dimension size){
        Image tmp = img.getScaledInstance(size.width, size.height, BufferedImage.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return result;
    }
    /**
     * Calculate distance between two point
     * @param a point a
     * @param b point b
     * @return the distance between a and b
     */
    public static double distance(Point a, Point b){
        return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
    }
    
    public static Point vectorNormalize(Point a, int length){
        double sum = Math.sqrt(a.x*a.x + a.y*a.y);
        Point p = new Point((int)(a.x*length/sum), (int)(a.y*length/sum));
        return p;
    }
    
    public static BufferedImage getDialogBox(String str, Dimension size, Font font, Color color){
        JTextArea text = new JTextArea();
        text.setText(str);
        if(font != null) text.setFont(font);
        if(color != null) text.setForeground(color);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        if(size != null)text.setSize(size);
        else text.setSize(text.getPreferredSize());
        text.setOpaque(false);
        BufferedImage img = new BufferedImage(text.getWidth(), text.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        text.paint(g);
        g.dispose();
        return img;
    }
    
    public static Point getCenter(LWC_RPG rpg, LWC_AbstractObject obj){
        Rectangle area = getObjArea(rpg, obj);
        return new Point(area.x + area.width/2, area.y + area.height/2);
    }
    
    public static Point calCenterPosition(Point p, Dimension dim){
        return new Point(p.x + dim.width/2, p.y + dim.height/2);
    }
    
    public static ArrayList<LWC_AbstractObject> getAllCharacterExcept(LWC_RPG rpg, ArrayList<LWC_AbstractObject> block){
        ArrayList<LWC_AbstractObject> result = rpg.getObjList();
        for(int i=result.size()-1; i>=0; i--){
            if(block != null && block.indexOf(result.get(i)) >= 0)
                result.remove(i);
            else if(!(result.get(i) instanceof LWC_AbstractCharacter))
                result.remove(i);
        }
        return result;
    }
    
    public static Rectangle getObjArea(LWC_RPG rpg, LWC_AbstractObject obj){
        Point p = rpg.getPosition(obj);
        Rectangle area;
        if(obj instanceof LWC_AbstractCharacter){
            area = ((LWC_AbstractCharacter)obj).getPrefferedSize();
            area.x += p.x;
            area.y += p.y;
        }
        else area = new Rectangle(rpg.getPosition(obj), getImgDim(obj.show()));
        return area;
    }
    
    public static ArrayList<LWC_AbstractCharacter> getCharacterInArea(LWC_RPG rpg, Rectangle area){
        ArrayList<LWC_AbstractObject> tmp = getAllCharacterExcept(rpg, null);
        ArrayList<LWC_AbstractCharacter> result = new ArrayList<>(tmp.size());
        for(int i=0; i<tmp.size(); i++){
            LWC_AbstractCharacter chr = (LWC_AbstractCharacter)tmp.get(i);
            if(isBlockInside(area, getObjArea(rpg, chr))) result.add(chr);
        }
        return result;
    }
    
    public static ArrayList<LWC_AbstractCharacter> getCharacterWithInArea(LWC_RPG rpg, Rectangle area){
        ArrayList<LWC_AbstractObject> tmp = getAllCharacterExcept(rpg, null);
        ArrayList<LWC_AbstractCharacter> result = new ArrayList<>(tmp.size());
        for(int i=0; i<tmp.size(); i++){
            LWC_AbstractCharacter chr = (LWC_AbstractCharacter)tmp.get(i);
            if(isInside(area, getObjArea(rpg, chr))) result.add(chr);
        }
        return result;
    }

    public static ArrayList<LWC_AbstractCharacter> getCharacterInRadius(LWC_RPG rpg, LWC_AbstractObject obj, double radius){
        ArrayList<LWC_AbstractObject> blockList = new ArrayList<>();
        blockList.add(obj);
        ArrayList<LWC_AbstractObject> characters = getAllCharacterExcept(rpg, blockList);
        
        Point objLocation = rpg.getPosition(obj);
        ArrayList<LWC_AbstractCharacter> result = new ArrayList<>();
        for (LWC_AbstractObject character : characters) {
            Point p = rpg.getPosition(character);
            if(LWC_Util.distance(p, objLocation) < radius) 
                result.add((LWC_AbstractCharacter)character);
        }
        return result;
    }
    
    public static BufferedImage setOpaque(BufferedImage img, float n){
        BufferedImage tmp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)tmp.getGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, n));
        
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return tmp;
    }
    public static BufferedImage []flipImages(BufferedImage []img){
        BufferedImage []result = new BufferedImage[img.length];
        for(int i=0; i<result.length; i++){
            result[i] = new BufferedImage(img[i].getWidth(), img[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = result[i].createGraphics();
            
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-img[i].getWidth(), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            g2d.drawImage(img[i], tx, null);
            g2d.dispose();
        }
        return result;
    }
    
    public static boolean isOpaque(BufferedImage img, Point p){
        int color;
        try{
            color = img.getRGB(p.x, p.y);
            if( ((color>>24)&0xff) != 0 ) return false;
            else return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static Rectangle []ApproxCenterAndDim(LWC_AbstractObject obj){
        BufferedImage img = obj.show();
        Rectangle []result = new Rectangle[2];
        
        Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), max = new Point();
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++){
                if(isOpaque(img, new Point(i, j)) == false) {
                    if(min.x > i) min.x = (int) (i);
                    if(min.y > j) min.y = (int) (j);
                    if(max.x < i) max.x = (int) (i);
                    if(max.y < j) max.y = (int) (j);
                }
            }
        result[0] = new Rectangle();
        result[0].x = min.x;
        result[0].y = min.y;
        result[0].width = max.x - min.x;
        result[0].height = max.y - min.y;
        result[1] = new Rectangle(img.getWidth() - (result[0].x+result[0].width), result[0].y, result[0].width, result[0].height);
        System.err.println(result[0]);
        System.err.println(result[1]);
        return result;
        
    }
}