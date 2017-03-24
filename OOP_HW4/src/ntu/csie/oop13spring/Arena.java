package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Arena class, control Pet and record key and item and skill
 * @author Dan
 */
public class Arena extends POOArena implements KeyListener{
    private int counter = 0;
	private boolean first_run = true;
    private HashSet<Integer> key = new HashSet<>();
    private ArrayList<Integer> recentKey = new ArrayList<>();
    private MainWindow window = new MainWindow("POOArena");
    private java.util.Timer CLI_Timer = new java.util.Timer();
    private ArrayList<Item> ItemList = new ArrayList<>();
    
    /**
     *  all pets whose still alive
     */
    protected ArrayList<Pet> allpets = new ArrayList<>(0);
    
    /**
     * Currently active skillList
     */
    protected ArrayList<TimerEffects> skillList = new ArrayList<>();

	/**
     *  Arena constructor, show Arena information in CLI
     */
    public Arena(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
	}
    
	private void init(){   
        POOPet[] parr = getAllPets();
        for (int i = 0; i < parr.length; i++)
            allpets.add((Pet)parr[i]);
        
        int choice = JOptionPane.showConfirmDialog(window, "1P(Y) 2P(N) Leave(Cancel)?");
        if(choice == JOptionPane.CANCEL_OPTION) JOptionPane.showMessageDialog(window, "GoodBye, see you later");
        else if(choice == JOptionPane.YES_OPTION) allpets.remove(1);
        
        String control = "";
        for (Pet pet : allpets)
            if(!(pet instanceof Monster)) control += String.format("<h2>Pet: %s</h2><br/> %s <br/>", pet.getName(), pet.getActionKeyString());
        String c = String.format("<html><h1>Control Settings</h1><br/> %s <style=\"color:red;\"><h2> Find Secret Combo skill & kill all other pets!!!</html>", control);
        JOptionPane.showMessageDialog(window, c, null, JOptionPane.INFORMATION_MESSAGE);    
        
        for(int i=0; i<allpets.size(); i++){
            allpets.get(i).initImage();
            allpets.get(i).initComp(window.Combat_Panel);
            allpets.get(i).initStatComp(window.getStatPanels(i+1));
        }
        
        show();
		first_run = false;
        
        window.setVisible(true);
        window.addKeyListener(this);
        
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

    /**
     * let all pet decide their action and movement, random generate item and update current skill
     * @return arena finished or not
     */
    @Override
	public boolean fight(){
		if (first_run == true) init();
        counter++;

		for (Pet e: allpets){
			if(e.getHP()>0){
				POOAction act = e.act(this);
                if(act != null && act.skill != null && act.skill instanceof TimerEffects){
                    skillList.add(((TimerEffects)act.skill));
                    ((TimerEffects)act.skill).startTimer();
                }
                else if(act != null && act.skill != null && act.dest != null) act.skill.act(act.dest);
			}
            
            POOCoordinate coor = e.move(this);
            if(coor != null) e.comp.setCoor(coor);
            
            if( counter%60 == 0 && Math.random()*100 < 5){
                Coordinate t = new Coordinate((int)(Math.random()*window.Combat_Panel.getSize().height), (int)(Math.random()*window.Combat_Panel.getSize().width));
                int r = (int) (Math.random()*10);
                if(r > 4) ItemList.add(new Meat(window.Combat_Panel, t));
                else if(r > 1) ItemList.add(new Soap(window.Combat_Panel, t));
                else ItemList.add(new MushRoom(window.Combat_Panel, t));
            }
            for(int i=ItemList.size()-1; i>=0; i--){
                if(POOUtil.isBlockInside(e.comp.getBounds(), ItemList.get(i).comp.getBounds())){
                    ItemList.get(i).act(e);
                    ItemList.get(i).dispose();
                    ItemList.remove(i);
                }
            }
		}
        
        for (int i=skillList.size()-1; i>=0; i--)
            if(skillList.get(i).update(this) == -1)
                skillList.remove(i);
        
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
    
    /**
     * update the whole map, call each pet's draw method.
     */
    @Override
	public void show(){
        try {
            Thread.sleep(16);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        
        for (Pet pet : allpets)
            pet.draw(this);
        
        for(int i=allpets.size()-1; i>=0; i--){
            if(allpets.get(i).getHP() <= 0){
                allpets.get(i).comp.dispose();
                allpets.remove(i);
            }
        }
	}
    
    /**
     * Get the pet's position.
     * @param p Pet to be searched
     * @return the coordinate
     */
    @Override
	public POOCoordinate getPosition(POOPet p){
		return ((Pet)p).comp.getCoor();
	}
    /**
     * Get all pets except certain pet
     * @param blocklist pet which want to exclude from search
     * @return pet which match the query
     */
    public ArrayList<Pet> getAllPetsExcept(ArrayList<POOPet> blocklist){
        ArrayList<Pet> tmp = (ArrayList<Pet>) allpets.clone();
        for(int i=tmp.size()-1; i>=0; i--)
            if(blocklist.indexOf(tmp.get(i)) >= 0) tmp.remove(i);
        return tmp;
    }
    /**
     * Get all pets except certain pet
     * @param pet pet which want to exclude from search
     * @return pet which match the query
     */
    public ArrayList<Pet> getAllPetsExcept(Pet pet){
        ArrayList<Pet> tmp = (ArrayList<Pet>) allpets.clone();
        tmp.remove(pet);
        return tmp;
    }
    /**
     * Search area for Pet
     * @param t area you want to search
     * @param blockList element you don't want to be returned
     * @return the index of Pet which is in the area
     */
    public int searchBlockPosition(Rectangle t, ArrayList<Pet> blockList){        
        ArrayList<Pet> parr = allpets;
        for (int i = 0; i < parr.size(); i++) {
            Pet pet = parr.get(i);
            
            if(blockList.indexOf(pet) >= 0) continue;
            Rectangle tmp = new Rectangle(pet.comp.getCoor().x, pet.comp.getCoor().y, pet.comp.getComp().getSize().width, pet.comp.getComp().getSize().height);
            if(POOUtil.isBlockInside(t, tmp))return i;
        }
        return -1;
    }
    
    /**
     * get the current pressed key
     * @return current pressed key
     */
    protected HashSet<Integer> getKey(){
        return (HashSet<Integer>) key.clone();
    }
    /**
     * Get recent released key
     * @return recent released key
     */
    protected Integer[] getRecentKey(){
        Integer []a = new Integer[0];
        return recentKey.toArray(a);
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

