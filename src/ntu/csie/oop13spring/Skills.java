package ntu.csie.oop13spring;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public abstract class Skills extends POOSkill{
    private String name, description;

    public Skills(String str, String des){
        name = str;
        description = des;
    }
	public final String getName(){
		return name;
	}
    public final String getDesString(){
        return description;
    }
	protected abstract boolean require(POOPet pet);
    @Override
    public abstract void act(POOPet pet);
}

 abstract class TimerSkills extends Skills{
    protected int counter;
    public TimerSkills(String str, String des){
        super(str, des);
    }
    public abstract void startTimer(POOArena arena);
    public abstract int update(final POOPet pet, Arena arena);
}

class Attack extends TimerSkills{
    private int speed = 5;
    private Pet npet;
    private MyComp comp;
    private class Vector{
        Vector(double a, double b){
            x = a;
            y = b;
        }
        double x;
        double y;
    }
    private Vector Direction;
    
	Attack(){
		super("Attack", "Damage Foe 1 HP");
	}
    Attack(String str, String Des){
		super(str, Des);
	}
    @Override
	protected boolean require(POOPet pet){
        npet = (Pet)pet;
        if(npet.getTimer("Attack") < 10/npet.getAGI()) return false;
        
        npet.resetTimer("Attack");
        if(npet.getRegister("AttackDirection") == null){
            if( npet.GetCurrentDirection() == POOConstant.STAT_UP )Direction = new Vector(-1, 0);
            else if( npet.GetCurrentDirection() == POOConstant.STAT_DOWN )Direction = new Vector(1, 0);
            else if( npet.GetCurrentDirection() == POOConstant.STAT_RIGHT )Direction = new Vector(0, 1);
            else if( npet.GetCurrentDirection() == POOConstant.STAT_LEFT )Direction = new Vector(0, -1);
        }
        else{
            Coordinate d = ((Coordinate)npet.getRegister("AttackDirection"));
            Direction = new Vector(d.x/Math.sqrt(d.x*d.x+d.y*d.y), d.y/Math.sqrt(d.x*d.x+d.y*d.y));
            
            npet.clearRegister("AttackDirection");
        }
        return pet.setMP(pet.getMP()-1);
	}
    
    @Override
	public void act(POOPet pet){
		((Pet)pet).damage(pet.getHP()-1, POOSkillConstant.NORMAL);
	}
        
    @Override
    public void startTimer(final POOArena arena){
        Point objoffset = new Point(npet.comp.getCoor().x + npet.comp.getComp().getSize().height/2, npet.comp.getCoor().y + npet.comp.getComp().getSize().width/2);
        
        comp = new MyComp(npet.comp.getComp().getParent(), new JPanel(), objoffset.x, objoffset.y, 5, 5);
        comp.getComp().setBorder(new LineBorder(Color.black));
        comp.getComp().setBackground(Color.red);
        comp.getComp().setVisible(false);
        counter = 0;
    }
    
    @Override
    public int update(POOPet pet, Arena arena){       
        counter ++;
        comp.getComp().setVisible(true);
        
        POOCoordinate coorr = comp.getCoor();
        coorr.x += (int)(speed * Direction.x);
        coorr.y += (int)(speed * Direction.y);
        comp.setCoor(coorr);
        comp.draw();

        ArrayList<Pet> blocklist = new ArrayList<>();
        blocklist.add(npet);
        
        Pet tmp = null;
        int index = ((Arena)arena).searchBlockPosition(comp.getBounds(), blocklist);
        if(index >= 0) tmp = ((Arena)arena).allpets.get(index);
        
        if(tmp != null)
            act(tmp);
        
        if( tmp != null || counter == 60 ){
            comp.dispose();
            return -1;
        }
        return 0;
    }
}

class Move extends Skills{
    Move(){
        super("Move", "");
    }
    Move(String str, String Des){
		super(str, Des);
	}
    
    @Override
    protected  boolean require(POOPet pet){
        return true;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}


class Guard extends TimerSkills{
    Guard(){
        super("Guard", "Guard yourself");
    }
    Guard(String str, String Des){
		super(str, Des);
	}
    
    @Override
    public void act(POOPet pet){
       ((Pet)pet).setState(POOUtil.delStatus( ((Pet)pet).getState(), POOConstant.STAT_LOCK | POOConstant.STAT_GUARD ));
    }

    @Override
    public void startTimer(POOArena arena) {
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        if(counter % 4 == 0)pet.setMP(pet.getMP()-1);
        
        if( !arena.getKey().contains(((Pet)pet).actionkeys[5]) ){
            act(pet);
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        if(POOUtil.isStatus(((Pet)pet).getState(), POOConstant.STAT_GUARD) && pet.getMP() > 5)return false;
        ((Pet)pet).setState(POOUtil.setStatus( ((Pet)pet).getState(), POOConstant.STAT_LOCK | POOConstant.STAT_GUARD ));
        return true;
    }
}

class Jump extends TimerSkills{
    private double velocity = 3;
    
    public Jump(String str, String des) {
        super(str, des);
    }
    Jump(){
        super("Jump", "Jump to avoid attack");
    }

    @Override
    public void startTimer(POOArena arena) {
        ;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        POOCoordinate coor = ((Pet)pet).comp.getCoor();
        coor.x -= velocity;
        ((Pet)pet).comp.setCoorForce(coor);
        
        velocity -= 0.2;
        
        if(velocity < -4) {
            act(pet);
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        int state = ((Pet)pet).getState();
        if(POOUtil.isStatus(state, POOConstant.STAT_JUMP)) return false;
        
        ((Pet)pet).setState( POOUtil.setStatus(state, POOConstant.STAT_JUMP) );
        return true;
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).setState( POOUtil.delStatus(((Pet)pet).getState(), POOConstant.STAT_JUMP) );
        System.out.println(POOUtil.isStatus(((Pet)pet).getState(), POOConstant.STAT_JUMP));
    }
}

class MissleAttack extends TimerSkills{
    private ArrayList<MyComp> comp = new ArrayList<>(4);
    private ArrayList<Point> acceleration = new ArrayList<>(4);
    private Pet npet;
    private int speed = 2;

    public MissleAttack(String str, String des) {
        super(str, des);
    }
    
    public MissleAttack(){
        super("MissleAttck", "Launch your ultimate attack");
    }
    
    @Override
    public void startTimer(POOArena arena) {       
        for (int i = 0; i < 4; i++) {             
            comp.add( new MyComp(npet.comp.getComp().getParent(), new JPanel(), npet.comp.getCoor().x+npet.comp.getComp().getHeight()/2, npet.comp.getCoor().y+npet.comp.getComp().getWidth()/2, 10, 10) );
            comp.get(i).getComp().setBorder(new LineBorder(Color.black));
            comp.get(i).getComp().setBackground(Color.red);
            comp.get(i).getComp().setVisible(false);
            acceleration.add( new Point(i/2-1, i%2-1) );
        }
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        
        ArrayList<Pet> enemys = ((Arena)arena).getAllPetsExcept(npet);
        for (int i=comp.size()-1; i>=0 ; i--) {
            MyComp myComp = comp.get(i);
            myComp.getComp().setVisible(true);
            
            POOCoordinate coor = myComp.getCenter();
            coor.x += speed * acceleration.get(i).x;
            coor.y += speed * acceleration.get(i).y;
            myComp.setCenter(coor);
            myComp.draw();
            
            int min = 0;
            for(int j=0; j<enemys.size(); j++)
                if( Math.pow(coor.x-enemys.get(j).comp.getCoor().x, 2) + Math.pow(coor.y-enemys.get(j).comp.getCoor().y, 2) < Math.pow(coor.x-enemys.get(min).comp.getCoor().x, 2) + Math.pow(coor.y-enemys.get(min).comp.getCoor().y, 2) )
                    min = j;
            Pet enemy = enemys.get(min);
            
            int x = enemy.comp.getCenter().x - myComp.getCenter().x;
            int y = enemy.comp.getCenter().y - myComp.getCenter().y;
            if(x==0) acceleration.get(i).x = 0;
            if(y==0) acceleration.get(i).y = 0;

            if( counter > 0 && counter % 10 == 0){
                int offset = 1;
                if(x > 0) acceleration.get(i).x = acceleration.get(i).x + offset > 2 ? 2 : acceleration.get(i).x + offset;
                else if(x < 0)acceleration.get(i).x = acceleration.get(i).x - offset < -2 ? -2 : acceleration.get(i).x - offset;
                if(y > 0) acceleration.get(i).y = acceleration.get(i).y + offset > 2 ? 2 : acceleration.get(i).y + offset;
                else if(y < 0)acceleration.get(i).y = acceleration.get(i).y - offset < -2 ? -2 : acceleration.get(i).y - offset;
            }
            
            POOPet tmp = null;
            ArrayList<Pet> blocklist = new ArrayList<>();
            blocklist.add(npet);
            
            int index = ((Arena)arena).searchBlockPosition(myComp.getBounds(), blocklist);
            if(index >= 0) tmp = ((Arena)arena).allpets.get(index);
            if(tmp != null){
                act(tmp);
                myComp.dispose();
                comp.remove(myComp);
            }
        }
        
        
        if( counter == 80 || comp.size() == 0 ){
            for (MyComp myComp : comp) myComp.dispose();
            npet.clearRegister("MissleAttack");
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = ((Pet)pet);
        if(npet.getRegister("MissleAttack") != null ) return false;
        npet.setRegister("MissleAttack", new Object());
        return pet.setMP(pet.getMP()-30);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(pet.getHP()-5, POOSkillConstant.NORMAL);
    }
}

class BodyBlink extends TimerSkills{
    public BodyBlink(String str, String des) {
        super(str, des);
    }
    public BodyBlink() {
        super("BodyBlink", "No Actual Effect");
    }

    @Override
    public void startTimer(POOArena arena) {
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        if(counter%5 == 0){
            if(counter%10 != 0)((Pet)pet).comp.getComp().setVisible(false);
            else ((Pet)pet).comp.getComp().setVisible(true);
        }
        
        if(counter >= 30) {
            act(pet);
            return -1;
        }
        else return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        if(POOUtil.isStatus(((Pet)pet).getState(), POOConstant.STAT_BLINK)) return false;
        else ((Pet)pet).setState( POOUtil.setStatus(((Pet)pet).getState(), POOConstant.STAT_BLINK) );
        return true;
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).comp.getComp().setVisible(true);
        ((Pet)pet).setState( POOUtil.delStatus(((Pet)pet).getState(), POOConstant.STAT_BLINK) );
    }
}

class Bombs extends TimerSkills{
    private Pet npet;
    private MyComp comp;
    private ImageIcon []images;
    ArrayList<Pet> blocklist = new ArrayList<>();
            
    public Bombs(String str, String des) {
        super(str, des);
    }
    public Bombs(){
        super("Bombs", "Set Bombs, damage foe in certain area");
    }

    @Override
    public void startTimer(POOArena arena) {
        images = new ImageIcon[5];
        images[0] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb1.png") );
        images[1] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb2.png") );
        images[2] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb3.png") );
        images[3] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb4.png") );
        images[4] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/explode.png") );
        
        JLabel bomb = new JLabel(images[0]);
        comp = new MyComp(npet.comp.getComp().getParent(), bomb, 0, 0, bomb.getIcon().getIconHeight(), bomb.getIcon().getIconWidth());
        comp.setCoor(new Coordinate(npet.comp.getCoor().x + npet.comp.getComp().getSize().height/2 - bomb.getIcon().getIconHeight()/2 + 10, npet.comp.getCoor().y + npet.comp.getComp().getSize().width/2 - bomb.getIcon().getIconWidth()/2 + 10));
        comp.getComp().setVisible(false);
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter++;
        comp.getComp().setVisible(true);
        
        comp.draw();
        if(counter%30 == 0){
            ((JLabel)comp.getComp()).setIcon(images[counter/30]);
            comp.getComp().setSize(images[counter/30].getIconHeight(), images[counter/30].getIconWidth());
        }
        if(counter == 125){
            while(true){
                Rectangle t = comp.getBounds();
                t.width += 10;
                t.height += 10;
                t.x -= 5;
                t.y -= 5;
                int index = arena.searchBlockPosition(t, blocklist);
                if(index >= 0){
                    POOPet tmp = ((Arena)arena).allpets.get(index);
                    act(tmp);
                    blocklist.add((Pet)tmp);
                }
                else break;
            }
        }
                
        if(counter == 140){
            comp.dispose();
            npet.setState(POOUtil.delStatus(npet.getState(), POOConstant.STAT_USERDEFINE1));
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = (Pet) pet;
        
        if(POOUtil.isStatus(npet.getState(), POOConstant.STAT_USERDEFINE1)) return false;
        npet.setState(POOUtil.setStatus(npet.getState(), POOConstant.STAT_USERDEFINE1));
        return pet.setMP(pet.getMP() - 15);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(pet.getHP()-30, POOSkillConstant.IGNORE_GUARD);
    }
}

class ShockWave extends TimerSkills{
    private Pet npet;
    private MyComp comp;
    ArrayList<Pet> blocklist = new ArrayList<>();
    
    public ShockWave(String str, String des) {
        super(str, des);
    }
    public ShockWave(){
        super("ShockWave", "A PowerFul ShockWave across the screen");
    }

    @Override
    public void startTimer(POOArena arena) {
        JPanel laser = new JPanel();
        comp = new MyComp(npet.comp.getComp().getParent(), laser, npet.comp.getCoor().x + npet.comp.getComp().getHeight()/2, npet.comp.getCoor().y + npet.comp.getComp().getWidth(), npet.comp.getComp().getParent().getWidth(), 10);
        laser.setBackground(Color.red);
        comp.getComp().setVisible(false);
        blocklist.add(npet);
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        comp.getComp().setVisible(true);
        comp.draw();
        counter ++;
        
        if(counter % 20 == 0){
            blocklist.clear();
            blocklist.add(npet);
            while(true){
                Rectangle t = comp.getBounds();
                int index = arena.searchBlockPosition(t, blocklist);
                if(index >= 0){
                    Pet tmp = ((Arena)arena).allpets.get(index);
                    act(tmp);
                    blocklist.add((Pet)tmp);
                }
                else break;
            }
        }
        
        if(counter > 100){
            comp.dispose();
            npet.setState(POOUtil.delStatus(npet.getState(), POOConstant.STAT_USERDEFINE2));
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = (Pet) pet;
        
        if(POOUtil.isStatus(npet.getState(), POOConstant.STAT_USERDEFINE2)) return false;
        npet.setState(POOUtil.setStatus(npet.getState(), POOConstant.STAT_USERDEFINE2));
        return pet.setMP(pet.getMP() - 10);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(pet.getHP()-5, POOSkillConstant.IGNORE_GUARD);
    }
}

class Message extends TimerSkills{
    private Pet npet;
    private MyComp comp;
    private String message;
    
    public Message(String str, String des) {
        super(str, des);
    }
    public Message(){
        super("Message", "Deliver Message to screen");
    }

    @Override
    public void startTimer(POOArena arena) {
        comp = new MyComp(npet.comp.getComp().getParent(), new JLabel( message, JLabel.CENTER ), npet.comp.getCoor().x-10, npet.comp.getCoor().y-20, 20, 20);
        comp.getComp().setForeground(Color.red);
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        comp.getComp().setVisible(true);
        POOCoordinate tmp = comp.getCoor();
        if(counter %5 == 0) tmp.x -= 1;
        comp.setCoor(tmp);
        comp.draw();
        
        if(counter >= 20){
            npet.clearRegister("Damage");
            comp.dispose();
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = (Pet)pet;
        message = (String)npet.getRegister("Damage");
        npet.clearRegister("Damage");
        return true;
    }

    @Override
    public void act(POOPet pet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}