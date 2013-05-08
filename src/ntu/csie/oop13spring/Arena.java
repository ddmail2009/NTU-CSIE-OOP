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
	private int alive = 0;
    
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
		for (int i=0; i<parr.length; i++)
            if(parr[i].getHP() > 0)alive ++;
        
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
                
//				if(e.getHP()>0 && act.dest!=null && act.dest.getHP()<=0) {
//					System.out.printf("%s killed %s\n", e.getName(), act.dest.getName());
//					alive--;
//				}
			}
		}

        if(alive == 1){
            window.dispose();
            return false;
        }
        else return true;
	}
    
    @Override
	public void show(){
        for (POOPet pOOPet : getAllPets()) {
            Arena_Pet pet = (Arena_Pet)pOOPet;
            pet.move(this);
            pet.draw();
        }
	}
    
    @Override
	public POOCoordinate getPosition(POOPet p){
		return ((Arena_Pet)p).getComp().getCoor();
	}
    protected ArrayList<Integer> petGetKey(POOPet pet){
        return petkey.get(pet);
    }
    public int searchPosition(POOCoordinate t){
		POOPet[] parr = getAllPets();
        if(t.y == getPosition(parr[1]).y)System.out.printf("searching coor %d, %d\n", t.x, t.y);
		for (int i=0; i<parr.length; i++)
			if (t.equals(getPosition(parr[i]))) return i;
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
    public final static int ratio = 2000;
    private Container contain;
    private JComponent comp;
    private Dimension size;
    private POOCoordinate coor;
    private Rectangle limit;
    
    public MyComp(Container contain, JComponent comp, int x, int y){
        init(contain, comp, x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
    }
    public MyComp(Container contain, JComponent comp, int x, int y, int width, int height){
        init(contain, comp, x, y, width, height);
    }
    private void init(Container contain, JComponent comp, int x, int y, int width, int height){
        this.contain = contain;
        this.contain.add(comp);
        setLimit(contain.getBounds());
        this.comp = comp;
        setSize(new Dimension(width, height));
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
    public Dimension getSize(){
        return new Dimension(size.width, size.height);
    }
    public void setSize(Dimension size){
        this.size = new Dimension(size.width, size.height);
    }
    protected void setCoor(POOCoordinate coor){  
        Insets insets = contain.getInsets();
        if(limit != null && isInside(limit.y, coor.y/ratio+insets.left, limit.width-size.width))this.coor.y = coor.y;
        else this.coor.y = coor.y;
        if(limit != null && isInside(limit.x, coor.x/ratio+insets.top, limit.height-size.height))this.coor.x = coor.x;
        else this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain.getInsets();
        comp.setBounds(coor.y/ratio+insets.left, coor.x/ratio+insets.top, size.width, size.height);
    }
    private boolean isInside(int left, int middle, int right){
        return left<=middle && middle<right;
    }
}