package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Arena extends POOArena implements KeyListener{
	private boolean first_run = true;
    private HashSet<Integer> key = new HashSet<>();
    private ArrayList<Integer> recentKey = new ArrayList<>();
    private MainWindow window = new MainWindow("POOArena");
    private java.util.Timer CLI_Timer = new java.util.Timer();

	public Arena(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
	}
    
	private void init(){   
        POOPet[] oldparr = getAllPets();
        final Pet[] parr = new Pet[oldparr.length];
        for (int i = 0; i < oldparr.length; i++)
            parr[i] = (Pet)oldparr[i];
        
        for (Pet pet : parr)
            pet.initImage();
		for (int i=0; i<parr.length; i++){
            parr[i].initComp(window.Combat_Panel);
            parr[i].initStatComp(window.getStatPanels(i+1));
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
                if(recentKey.size() > 0){
                    for (Integer integer : recentKey)
                        System.out.print(integer + " ");
                    System.out.println();
                    recentKey.remove(0);
                }
            }
        }, 0, 400);
	}

    @Override
	public boolean fight(){
		if (first_run == true)init();

		for (POOPet e: getAllPets()){
			if(e.getHP()>0){
				POOAction act = e.act(this);
                if(act != null && act.skill != null && act.dest != null) act.skill.act(act.dest);
			}
            e.move(this);
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
        try {
            Thread.sleep(16);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        
        for (POOPet pOOPet : getAllPets()) {
            Pet pet = (Pet)pOOPet;
            pet.draw(this);
        }
	}
    
    @Override
	public POOCoordinate getPosition(POOPet p){
		return ((Pet)p).getComp().getCoor();
	}
    protected HashSet<Integer> getKey(){
        return key;
    }
    protected Integer[] getRecentKey(){
        Integer []a = new Integer[0];
        return recentKey.toArray(a);
    }
    public int searchPosition(POOCoordinate t){
		POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++)
			if (t.equals(getPosition(parr[i]))) return i;
		return -1;
	}
    
    public int searchBlockPosition(Rectangle t, ArrayList<Pet> blockList){
        POOPet[] parr = getAllPets();
        for (int i = 0; i < parr.length; i++) {
            Pet pet = (Pet) parr[i];
            
            if(blockList.indexOf(pet) >= 0) continue;
            Rectangle tmp = new Rectangle(pet.comp.getCoor().x, pet.comp.getCoor().y, pet.comp.getComp().getSize().height, pet.comp.getComp().getSize().width);
            for(int j=0; j<t.height; j++)
                for(int k=0; k<t.width; k++)
                    if(POOUtil.isInside(tmp.x, t.x+j, tmp.x+tmp.height) && POOUtil.isInside(tmp.y, t.y+k, tmp.y+tmp.width)) 
                        return i;
        }
        return -1;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        key.add(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.print("Current key: ");
        for (Integer integer : key)
            System.out.print(integer + " ");
        System.out.println(", release " + e.getKeyCode() + " key");
        
        if(key.remove(e.getKeyCode())){
            if(recentKey.indexOf(e.getKeyCode()) < 0)
                recentKey.add(e.getKeyCode());
        }
    }
}

class MyComp{
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
        this.coor = new Coordinate(x, y);
        // this.comp.setBorder(new LineBorder(Color.yellow));
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
        Insets insets = contain.getInsets();
        if(limit != null && POOUtil.isInside(limit.y, coor.y+insets.left, limit.width-this.comp.getSize().width))this.coor.y = coor.y;
        else if(limit == null)this.coor.y = coor.y;
        if(limit != null && POOUtil.isInside(limit.x, coor.x+insets.top, limit.height-this.comp.getSize().height))this.coor.x = coor.x;
        else if(limit == null)this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain.getInsets();
        comp.setBounds(coor.y+insets.left, coor.x+insets.top, this.comp.getSize().width, this.comp.getSize().height);
    }
    public void dispose(){
        comp.setVisible(false);
        comp.getParent().remove(comp);
    }
    public Rectangle getBounds(){
        Rectangle s = new Rectangle(comp.getBounds());
        int tmp = s.x;
        s.x = s.y;
        s.y = tmp;
        return s;
    }
}