package ntu.csie.oop13spring;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

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
    private int Direction;
    private Arena_Pet npet;
    private MyComp comp;
    
	Attack(){
		super("Attack", "Damage Foe 1 HP");
	}
    Attack(String str, String Des){
		super(str, Des);
	}
    @Override
	protected boolean require(POOPet pet){
        npet = (Arena_Pet)pet;
        Direction = ((Arena_Pet)pet).GetCurrentDirection();
        return pet.setMP(pet.getMP()-1);
	}
    
    @Override
	public void act(POOPet pet){
        System.out.printf("hit pet %s\n", pet.getName());
		((Arena_Pet)pet).damage(pet.getHP()-1);
	}
        
    @Override
    public void startTimer(final POOArena arena){
        Dimension size = npet.comp.getComp().getSize();
        Point objoffset = new Point(npet.comp.getCoor().x + size.height, npet.comp.getCoor().y + size.width/2);
        Dimension objSize = new Dimension(10, 5);
        
        if(Direction == POOConstant.STAT_UP) objoffset = new Point(npet.comp.getCoor().x, npet.comp.getCoor().y + size.width/2);
        else if(Direction == POOConstant.STAT_LEFT) objoffset = new Point(npet.comp.getCoor().x + size.height/2, npet.comp.getCoor().y);
        else if(Direction == POOConstant.STAT_RIGHT)objoffset = new Point(npet.comp.getCoor().x + size.height/2, npet.comp.getCoor().y + size.width);
        else objoffset = new Point(npet.comp.getCoor().x + size.height, npet.comp.getCoor().y + size.width/2);
            
        if(Direction == POOConstant.STAT_LEFT || Direction == POOConstant.STAT_RIGHT) objSize = new Dimension(5, 10);
        System.out.println(npet.comp.getCoor().x);
        System.out.println(npet.comp.getCoor().y);
        
        comp = new MyComp(npet.comp.getComp().getParent(), new JPanel(), objoffset.x, objoffset.y, objSize.height, objSize.width);
        comp.getComp().setBorder(new LineBorder(Color.black));
        comp.getComp().setBackground(Color.red);
        comp.getComp().setVisible(true);
        counter = 0;
    }
    
    @Override
    public int update(POOPet pet, Arena arena){       
        counter ++;
        
        POOCoordinate coorr = comp.getCoor();
        if(Direction == POOConstant.STAT_RIGHT) coorr.y += 5;
        else if(Direction == POOConstant.STAT_UP) coorr.x -= 5;
        else if(Direction == POOConstant.STAT_LEFT) coorr.y -= 5;
        else coorr.x += 5;
        comp.setCoor(coorr);
        comp.draw();

        ArrayList<Arena_Pet> blocklist = new ArrayList<>();
        blocklist.add(npet);
        
        POOPet tmp = null;
        int index = ((Arena)arena).searchBlockPosition(comp.getComp().getBounds(), blocklist);
        if(index >= 0) tmp = arena.getAllPets()[index];
        
        if(tmp != null){
            System.out.printf("%s attacked %s\n", npet.getName(), tmp.getName());
            act(tmp);
        }
        
        
        if( tmp != null || counter == 60 ){
            comp.getComp().setVisible(false);
            comp.getComp().getParent().remove(comp.getComp());
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
       ((Arena_Pet)pet).setState(POOUtil.delStatus( ((Arena_Pet)pet).getState(), POOConstant.STAT_LOCK | POOConstant.STAT_GUARD ));
    }

    @Override
    public void startTimer(POOArena arena) {
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        if(counter % 4 == 0)pet.setMP(pet.getMP()-1);
        
        if(counter > 10 ){
            act(pet);
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        if(POOUtil.isStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_GUARD) && pet.getMP() > 5)return false;
        ((Arena_Pet)pet).setState(POOUtil.setStatus( ((Arena_Pet)pet).getState(), POOConstant.STAT_LOCK | POOConstant.STAT_GUARD ));
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
        POOCoordinate coor = ((Arena_Pet)pet).getComp().getCoor();
        coor.x -= velocity;
        ((Arena_Pet)pet).getComp().setCoor(coor);
        
        velocity -= 0.2;
        
        if(velocity < -4) {
            act(pet);
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        int state = ((Arena_Pet)pet).getState();
        if(POOUtil.isStatus(state, POOConstant.STAT_JUMP)) return false;
        
        ((Arena_Pet)pet).setState( POOUtil.setStatus(state, POOConstant.STAT_JUMP) );
        return true;
    }

    @Override
    public void act(POOPet pet) {
        ((Arena_Pet)pet).setState( POOUtil.delStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_JUMP) );
        System.out.println(POOUtil.isStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_JUMP));
    }
}

class MissleAttack extends TimerSkills{
    private static boolean lock = false;
    private ArrayList<MyComp> comp = new ArrayList<>(9);
    private ArrayList<Point> Direction = new ArrayList<>(9);
    private Arena_Pet npet;
    private int speed = 2;

    public MissleAttack(String str, String des) {
        super(str, des);
    }
    
    public MissleAttack(){
        super("MissleAttck", "Launch your ultimate attack");
    }
    
    @Override
    public void startTimer(POOArena arena) {
        lock = true;
        
        for (int i = 0; i < 9; i++) {             
            comp.add( new MyComp(npet.comp.getComp().getParent(), new JPanel(), npet.comp.getCoor().x+npet.comp.getComp().getHeight()/2, npet.comp.getCoor().y+npet.comp.getComp().getWidth()/2, 10, 10) );
            comp.get(i).getComp().setBorder(new LineBorder(Color.black));
            comp.get(i).getComp().setBackground(Color.red);
            
            Direction.add( new Point(i/3-1, i%3-1) );
        }
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        
        POOPet []nets = arena.getAllPets();
        Arena_Pet enemy = null;
        for (int i = 0; i < nets.length; i++) {
            POOPet pOOPet = nets[i];
            if(pOOPet != pet) enemy = (Arena_Pet)pOOPet;
        }
        
        ArrayList<MyComp> removetmp = new ArrayList<>();
        for (int i = 0; i < comp.size(); i++) {
            MyComp myComp = comp.get(i);
            POOCoordinate coor = myComp.getCoor();
            coor.x += speed*Direction.get(i).x;
            coor.y += speed*Direction.get(i).y;
            myComp.setCoor(coor);
            myComp.draw();
            
            int x = enemy.comp.getCoor().x - myComp.getCoor().x;
            int y = enemy.comp.getCoor().y - myComp.getCoor().y;
            int offset = 1;
            if( counter > 0 && counter % 10 == 0){
                if(x > 0) Direction.get(i).x = Direction.get(i).x + offset > 2 ? 2 : Direction.get(i).x + offset;
                else Direction.get(i).x = Direction.get(i).x - offset < -2 ? -2 : Direction.get(i).x - offset;
                if(y > 0) Direction.get(i).y = Direction.get(i).y + offset > 2 ? 2 : Direction.get(i).y + offset;
                else Direction.get(i).y = Direction.get(i).y - offset < -2 ? -2 : Direction.get(i).y - offset;
            }
            
            POOPet tmp = null;
            ArrayList<Arena_Pet> blocklist = new ArrayList<>();
            blocklist.add(npet);
            
            int index = ((Arena)arena).searchBlockPosition(myComp.getComp().getBounds(), blocklist);
            if(index >= 0) tmp = arena.getAllPets()[index];
            if(tmp != null){
                act(tmp);
                myComp.getComp().setVisible(false);
                myComp.getComp().getParent().remove(myComp.getComp());
                removetmp.add(myComp);
            }
        }
        for (MyComp myComp : removetmp) 
            comp.remove(myComp);
        
        
        if( counter == 100 || comp.size() == 0 ){
            lock = false;
            for (MyComp myComp : comp) {
                myComp.getComp().setVisible(false);
                myComp.getComp().getParent().remove(myComp.getComp());
            }
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = ((Arena_Pet)pet);
        if(lock == true) return false;
        return pet.setMP(pet.getMP()-30);
    }

    @Override
    public void act(POOPet pet) {
        ((Arena_Pet)pet).damage(pet.getHP()-2);
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
        if(counter%5 == 0)((Arena_Pet)pet).comp.getComp().setVisible(false);
        else ((Arena_Pet)pet).comp.getComp().setVisible(true);
        
        if(counter >= 30) {
            act(pet);
            return -1;
        }
        else return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        if(POOUtil.isStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_BLINK)) return false;
        else ((Arena_Pet)pet).setState( POOUtil.setStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_BLINK) );
        return true;
    }

    @Override
    public void act(POOPet pet) {
        ((Arena_Pet)pet).comp.getComp().setVisible(true);
        ((Arena_Pet)pet).setState( POOUtil.delStatus(((Arena_Pet)pet).getState(), POOConstant.STAT_BLINK) );
    }
}

class Bombs extends TimerSkills{
    public Bombs(String str, String des) {
        super(str, des);
    }
    public Bombs(){
        super("Bombs", "Set Bombs, damage foe in certain area");
    }

    @Override
    public void startTimer(POOArena arena) {
        counter = 0;
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean require(POOPet pet) {
        return pet.setMP(pet.getMP() - 10);
    }

    @Override
    public void act(POOPet pet) {
        ((Arena_Pet)pet).damage(pet.getHP()-10);
    }
}