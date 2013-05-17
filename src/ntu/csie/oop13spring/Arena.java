package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Arena extends POOArena implements KeyListener{
	private boolean first_run = true;
    private HashSet<Integer> key = new HashSet<>();
    private ArrayList<Integer> recentKey = new ArrayList<>();
    private MainWindow window = new MainWindow("POOArena");
    private java.util.Timer CLI_Timer = new java.util.Timer();
    protected ArrayList<Pet> allpets = new ArrayList<>(0);
    protected ArrayList<TimerSkills> skilllist = new ArrayList<>();

	public Arena(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
	}
    
	private void init(){   
        POOPet[] parr = getAllPets();
        for (int i = 0; i < parr.length; i++)
            allpets.add((Pet)parr[i]);
        
        for(int i=0; i<allpets.size(); i++){
            allpets.get(i).initImage();
            allpets.get(i).initComp(window.Combat_Panel);
            allpets.get(i).initStatComp(window.getStatPanels(i+1));
        }
        
        show();
		first_run = false;
        
        window.setVisible(true);
        window.addKeyListener(this);
        
        String a = String.format("<h2>P1: %s</h2><br/> UP: %s LEFT: %s DOWN: %s RIGHT: %s ATTACK: %s GUARD: %s JUMP: %s", allpets.get(0).getName(), "W", "D", "S", "A", "Q", "E", "F");
        String b = String.format("<h2>P2: %s</h2><br/> UP: %s LEFT: %s DOWN: %s RIGHT: %s ATTACK: %s GUARD: %s JUMP: %s", allpets.get(1).getName(), "I", "L", "K", "J", "U", "O", "P");
        String c = String.format("<html><h1>Control Settings</h1><br/> %s <br/> %s <br/><style=\"color:red;\"><h2> defeat the monster and each other!!!</html>", a, b);
        JOptionPane.showMessageDialog(window, c, null, JOptionPane.INFORMATION_MESSAGE);    
        
        CLI_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
//               for (Pet pet : allpets)
//                   System.out.println(pet);
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

		for (Pet e: allpets){
			if(e.getHP()>0){
				POOAction act = e.act(this);
                if(act != null && act.skill != null && act.skill instanceof TimerSkills){
                    skilllist.add(((TimerSkills)act.skill));
                    ((TimerSkills)act.skill).startTimer();
                }
                else if(act != null && act.skill != null && act.dest != null) act.skill.act(act.dest);
			}
            e.move(this);
		}
        
        for (int i=skilllist.size()-1; i>=0; i--)
            if(skilllist.get(i).update(this) == -1)
                skilllist.remove(i);
        
        
        // check live or not
        for(int i=allpets.size()-1; i>=0; i--){
            if(allpets.get(i).getHP() <= 0){
                allpets.get(i).comp.dispose();
                allpets.remove(i);
            }
        }
        HashSet<String> alive = new HashSet<>();
        for (Pet pet : allpets) {
            alive.add(pet.getName());
        }

        if(alive.size() == 1){
            window.dispose();
            CLI_Timer.cancel();
            JOptionPane.showMessageDialog(window, String.format("<html><h1>%s</h1> Wins, Game over", allpets.get(0).getName()));
            System.exit(0);
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
        
        for (Pet pOOPet : allpets) {
            Pet pet = (Pet)pOOPet;
            pet.draw(this);
        }
	}
    
    @Override
	public POOCoordinate getPosition(POOPet p){
		return ((Pet)p).comp.getCoor();
	}
    protected HashSet<Integer> getKey(){
        return key;
    }
    protected Integer[] getRecentKey(){
        Integer []a = new Integer[0];
        return recentKey.toArray(a);
    }
    public int searchPosition(POOCoordinate t){
		ArrayList<Pet> parr = allpets;
		for (int i=0; i<parr.size(); i++)
			if (t.equals(getPosition(parr.get(i)))) return i;
		return -1;
	}
    public ArrayList<Pet> getAllPetsExcept(ArrayList<POOPet> blocklist){
        ArrayList<Pet> tmp = (ArrayList<Pet>) allpets.clone();
        for(int i=tmp.size()-1; i>=0; i--)
            if(blocklist.indexOf(tmp.get(i)) >= 0) tmp.remove(i);
        return tmp;
    }
    public ArrayList<Pet> getAllPetsExcept(Pet pet){
        ArrayList<Pet> tmp = (ArrayList<Pet>) allpets.clone();
        tmp.remove(pet);
        return tmp;
    }
    public int searchBlockPosition(Rectangle t, ArrayList<Pet> blockList){
        ArrayList<Pet> parr = allpets;
        for (int i = 0; i < parr.size(); i++) {
            Pet pet = parr.get(i);
            
            if(blockList.indexOf(pet) >= 0) continue;
            Rectangle tmp = new Rectangle(pet.comp.getCoor().x, pet.comp.getCoor().y, pet.comp.getComp().getSize().width, pet.comp.getComp().getSize().height);
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
        Insets insets = contain.getInsets();
        if(limit != null && POOUtil.isInside(limit.y, coor.y+insets.left, limit.width-this.comp.getSize().width))this.coor.y = coor.y;
        else if(limit == null)this.coor.y = coor.y;
        if(limit != null && POOUtil.isInside(limit.x, coor.x+insets.top, limit.height-this.comp.getSize().height))this.coor.x = coor.x;
        else if(limit == null)this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain.getInsets();
        comp.setBounds(coor.y+insets.left, coor.x+insets.top, comp.getSize().width, comp.getSize().height);
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
    
    public POOCoordinate getCenter(){
        Rectangle t = getBounds();
        return new Coordinate(t.x + t.height/2, t.y + t.width/2);
    }
    public void setCenter(POOCoordinate t){
        setCoor(new Coordinate(t.x - getBounds().height/2, t.y - getBounds().width/2));
        draw();
    }
}