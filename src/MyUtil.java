import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * General Utility Used in this project
 */
public class MyUtil{
    private MyUtil(){}
    
    /**
     * Get the BufferedImage
     * @param file the relative path to the file
     * @return the BufferedImage
     */
    public static BufferedImage getImage(String file){
        BufferedImage img = null;
        try {
            img = ImageIO.read(MyUtil.class.getResource(file));
        } catch (IOException ex) {
            System.err.println(file);
            ex.printStackTrace();
        }   
        return img;
    }
}