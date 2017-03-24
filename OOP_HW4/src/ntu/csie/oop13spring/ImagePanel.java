package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ImagePanel extends JPanel {
    private BufferedImage img;
    
    public ImagePanel(String img){
        this(POOUtil.getImage(img), new Dimension(POOUtil.getImage(img).getWidth(), POOUtil.getImage(img).getHeight()));
    }
    
    public ImagePanel(String img, Dimension size){
        this(POOUtil.getImage(img), size);
    }
    
    public ImagePanel(BufferedImage img, Dimension size){
        this.img = img;
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }
}
