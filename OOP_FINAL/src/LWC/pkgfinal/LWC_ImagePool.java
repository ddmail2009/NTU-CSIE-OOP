package LWC.pkgfinal;

import java.awt.image.*;
import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.*;


public class LWC_ImagePool {
    private HashMap<String, BufferedImage> images = new HashMap<>();
    private HashMap<String, BufferedImage[]> folderImage = new HashMap<>();
    
    public LWC_ImagePool() {
    }
    
    public BufferedImage getImage(String path){
        String tmp = "/"+path;
        if(images.get(tmp) == null) 
            images.put(tmp, loadImage(tmp));
        
        BufferedImage tmpp = null;
        if(images.get(tmp) != null)
            return images.get(tmp);
        return tmpp;
    }
    
    /**
     * Get the image specified by the path
     * @param path the path to the image file
     * @return The BufferedImage
     */
    private BufferedImage loadImage(String path){
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResource(path));
        } catch (Exception ex) {
            System.err.println("Error Loading Image: " + path);
            ex.printStackTrace();
        }
        return img;
    }
    
    public BufferedImage[] getFolderImages(String path){
        ArrayList<String> list = new ArrayList<>();
        URL dirURL = getClass().getClassLoader().getResource(path);
        String protocol = dirURL.getProtocol();
        BufferedImage []img = null;
        try {
            switch (protocol) {
                case "jar":
                    if(folderImage.containsKey(path)) return folderImage.get(path);
                    
                    CodeSource src = getClass().getProtectionDomain().getCodeSource();
                    URL jar = src.getLocation();

                    ZipInputStream zip = new ZipInputStream(jar.openStream());
                    ZipEntry ze = null;
                    while( (ze = zip.getNextEntry()) != null){
                        String entryName = ze.getName();
                        if(entryName.startsWith(path) && entryName.endsWith(".png")) 
                            list.add(entryName);
                    }
                    break;
                case "file":
                    if(!path.endsWith("/")) path = path + "/";
                    if(folderImage.containsKey(path)) return folderImage.get(path);
                    
                    File folder = new File(dirURL.toURI());
                    File[] listOfFiles = folder.listFiles();
                    for(int i=0; i<listOfFiles.length; i++)
                        if(listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png"))
                            list.add(path+listOfFiles[i].getName());
                    break; 
                default:
                    System.err.println("Unknown Protocal " + protocol);
                    System.exit(1);
            }
        }
        catch (Exception ex) {
            System.err.println("Error Loading Folder: " + path + "err: " + ex);
        }
        
        img = new BufferedImage[list.size()];
        for(int i=0; i<list.size(); i++)
            img[i] = getImage(list.get(i));
        
        folderImage.put(path, img);
        return img;
    }
}
