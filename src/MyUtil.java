import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * General Utility Used in this project
 * @author daniel
 */
public class MyUtil{
    private MyUtil(){}
    
    /**
     * Get the BufferedImage
     * @param codebase the working directory
     * @param file the relative path to the file
     * @return the BufferedImage
     */
    public static BufferedImage getImage(URL codebase, String file){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new URL(codebase, file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return img;
    }
    
    public static BufferedImage getImage(URL url){
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return img;
    }
}