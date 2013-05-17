package ntu.csie.oop13spring;

import java.awt.*;
import javax.swing.*;

public class POOJComp{
    private Container contain;
    private JComponent comp;
    private POOCoordinate coor;
    private Rectangle limit = null;
    
    public POOJComp(Container contain, JComponent comp, int x, int y){
        init(contain, comp, x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
    }
    public POOJComp(Container contain, JComponent comp, int x, int y, int width, int height){
        init(contain, comp, x, y, width, height);
    }
    private void init(Container contain, JComponent comp, int x, int y, int width, int height){
        this.contain = contain;
        if(this.contain != null)this.contain.add(comp);
        this.comp = comp;
        this.comp.setSize(new Dimension(width, height));
        this.coor = new Coordinate(x, y);
        draw();
    }
    public void setLimit(Rectangle rec){
        limit = (Rectangle)(rec.clone());
    }
    public JComponent getComp(){
        return comp;
    }
    public POOCoordinate getCoor(){
        return new Coordinate(coor.x, coor.y);
    }
    protected void setCoorForce(POOCoordinate coor){
        this.coor.x = coor.x;
        this.coor.y = coor.y;
    }
    protected void setCoor(POOCoordinate coor){  
        Insets insets = contain != null ? contain.getInsets() : new Insets(0, 0, 0, 0);
        if(limit != null && POOUtil.isInside(limit.y, coor.y+insets.left, limit.width-this.comp.getSize().width))this.coor.y = coor.y;
        else if(limit == null)this.coor.y = coor.y;
        if(limit != null && POOUtil.isInside(limit.x, coor.x+insets.top, limit.height-this.comp.getSize().height))this.coor.x = coor.x;
        else if(limit == null)this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain != null ? contain.getInsets() : new Insets(0, 0, 0, 0);
        comp.setBounds(coor.y+insets.left, coor.x+insets.top, comp.getSize().width, comp.getSize().height);
    }
    public void dispose(){
        comp.setVisible(false);
        if(contain != null) contain.remove(comp);
    }
    public Rectangle getBounds(){
        Rectangle s = new Rectangle(comp.getBounds());
        int tmp = s.x;
        s.x = s.y;
        s.y = tmp;
        return s;
    }
    public POOCoordinate getCenter(){
        Rectangle t = getBounds();
        return new Coordinate(t.x + t.height/2, t.y + t.width/2);
    }
    public void setCenter(POOCoordinate t){
        setCoor(new Coordinate(t.x - getBounds().height/2, t.y - getBounds().width/2));
        draw();
    }
}