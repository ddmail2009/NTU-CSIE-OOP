package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Arena extends POOArena implements KeyListener{
    private HashMap<POOPet, ArrayList<Integer>> petkey = new HashMap<>();
	private boolean first_run = true;
    
    private MainWindow window = new MainWindow("POOArena");

    private java.util.Timer CLI_Timer = new java.util.Timer();
    private java.util.Timer Win_Timer = new java.util.Timer();

	public Arena(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
	}
    
	private void init(){   
        POOPet[] oldparr = getAllPets();
        final Arena_Pet[] parr = new Arena_Pet[oldparr.length];
        for (int i = 0; i < oldparr.length; i++)
            parr[i] = (Arena_Pet)oldparr[i];
        
        for (Arena_Pet pet : parr)
            pet.initImage();
		for (int i=0; i<parr.length; i++){
            parr[i].initComp(window.Combat_Panel);
            parr[i].initStatComp(window.getStatPanels(i+1));
            
            petkey.put(parr[i], new ArrayList<Integer>());
        }
       
        show();
		first_run = false;
        
        window.setVisible(true);
        window.addKeyListener(this);
        CLI_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (POOPet pet : getAllPets())
                    System.out.println(pet);
            }
        }, 0, 1000);
	}

    @Override
	public boolean fight(){
		if (first_run == true)init();

		for (POOPet e: getAllPets()){
			if(e.getHP()>0){
				POOAction act = e.act(this);
                if(act != null && act.skill != null && act.dest != null) act.skill.act(act.dest);
			}
		}
        int alive = 0;
        for (POOPet e: getAllPets())
            if(e.getHP()==0) alive++;

        if(alive == 1){
            window.dispose();
            CLI_Timer.cancel();
            new AlertFrame("Pet survived!!!");
            return false;
        }
        else return true;
	}
    
    @Override
	public void show(){
        for (POOPet pOOPet : getAllPets()) {
            Arena_Pet pet = (Arena_Pet)pOOPet;
            pet.move(this);
            pet.draw(this);
        }
	}
    
    @Override
	public POOCoordinate getPosition(POOPet p){
		return ((Arena_Pet)p).getComp().getActualCoor();
	}
    protected ArrayList<Integer> petGetKey(POOPet pet){
        return petkey.get(pet);
    }
    public int searchPosition(POOCoordinate t){
		POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++)
			if (t.equals(getPosition(parr[i]))) return i;
		return -1;
	}
    public int searchBlockPosition(Rectangle t, ArrayList<Arena_Pet> blockList){
        POOPet[] parr = getAllPets();
        for(int i=0; i<t.height; i++)
            for(int j=0; j<t.width; j++){
                Component comp = window.Combat_Panel.getComponentAt(t.x+i, t.y+j);
                for (int k=0; k<parr.length; k++) {
                    Arena_Pet pet = (Arena_Pet)parr[k];
                    if(blockList.indexOf(pet) >= 0)continue;
                    if(pet.comp.getComp() == comp) return k;
                }
            }
        return -1;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        try{
            ;
        }catch(Exception err){
            System.out.println(err);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        for (POOPet pet : getAllPets()) {
            petkey.get(pet).add(e.getKeyCode());
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        for (POOPet pet : getAllPets()) {
            petkey.get(pet).add(-e.getKeyCode());
        }
    }
}

class Image{
    static public BufferedImage ReadImage(String path){
        try{
            return ImageIO.read(new File(path));
        }catch(Exception e){
            System.out.println("Image "+path+" Can not be found!!");
            e.printStackTrace();
            return null;
        }
    }
}

class MyComp{
    public final static int ratio = 800;
    private Container contain;
    private JComponent comp;
    private POOCoordinate coor;
    private Rectangle limit = null;
    
    public MyComp(Container contain, JComponent comp, int x, int y){
        init(contain, comp, x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
    }
    public MyComp(Container contain, JComponent comp, int x, int y, int width, int height){
        init(contain, comp, x, y, width, height);
    }
    private void init(Container contain, JComponent comp, int x, int y, int width, int height){
        this.contain = contain;
        this.contain.add(comp);
        this.comp = comp;
        this.comp.setSize(new Dimension(width, height));
        this.coor = new Coordinate(x*ratio, y*ratio);
    }
    public void setLimit(Rectangle rec){
        limit = (Rectangle)(rec.clone());
    }
    public JComponent getComp(){
        return comp;
    }
    public POOCoordinate getCoor(){
        return new Coordinate(coor);
    }
    public POOCoordinate getActualCoor(){
        return new Coordinate(coor.x/ratio, coor.y/ratio);
    }
    public void setActualCoor(POOCoordinate coor){
        Coordinate t = new Coordinate(coor);
        t.x *= ratio;
        t.y *= ratio;
        setCoor(t);
    }
    protected void setCoor(POOCoordinate coor){  
        Insets insets = contain.getInsets();
        if(limit != null && isInside(limit.y, coor.y/ratio+insets.left, limit.width-this.comp.getSize().width))this.coor.y = coor.y;
        else if(limit == null)this.coor.y = coor.y;
        if(limit != null && isInside(limit.x, coor.x/ratio+insets.top, limit.height-this.comp.getSize().height))this.coor.x = coor.x;
        else if(limit == null)this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain.getInsets();
        comp.setBounds(coor.y/ratio+insets.left, coor.x/ratio+insets.top, this.comp.getSize().width, this.comp.getSize().height);
    }
    private boolean isInside(int left, int middle, int right){
        return left<=middle && middle<right;
    }
}