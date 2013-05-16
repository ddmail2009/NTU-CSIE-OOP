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
    private int Direction;
    private Pet npet;
    private MyComp comp;
    
	Attack(){
		super("Attack", "Damage Foe 1 HP");
	}
    Attack(String str, String Des){
		super(str, Des);
	}
    @Override
	protected boolean require(POOPet pet){
        npet = (Pet)pet;
        Direction = ((Pet)pet).GetCurrentDirection();
        return pet.setMP(pet.getMP()-1);
	}
    
    @Override
	public void act(POOPet pet){
		((Pet)pet).damage(pet.getHP()-1<0?0:pet.getHP()-1, POOSkillConstant.NORMAL);
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
        
        comp = new MyComp(npet.comp.getComp().getParent(), new JPanel(), objoffset.x, objoffset.y, objSize.height, objSize.width);
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
        if(Direction == POOConstant.STAT_RIGHT) coorr.y += 5;
        else if(Direction == POOConstant.STAT_UP) coorr.x -= 5;
        else if(Direction == POOConstant.STAT_LEFT) coorr.y -= 5;
        else coorr.x += 5;
        comp.setCoor(coorr);
        comp.draw();

        ArrayList<Pet> blocklist = new ArrayList<>();
        blocklist.add(npet);
        
        POOPet tmp = null;
        int index = ((Arena)arena).searchBlockPosition(comp.getBounds(), blocklist);
        if(index >= 0) tmp = arena.getAllPets()[index];
        
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
        
        if(counter > 10 ){
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
        POOCoordinate coor = ((Pet)pet).getComp().getCoor();
        coor.x -= velocity;
        ((Pet)pet).getComp().setCoorForce(coor);
        
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
    private ArrayList<Point> Direction = new ArrayList<>(4);
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
            Direction.add( new Point(i/2-1, i%2-1) );
        }
    }

    @Override
    public int update(POOPet pet, Arena arena) {
        counter ++;
        
        POOPet []nets = arena.getAllPets();
        Pet enemy = null;
        for (int i = 0; i < nets.length; i++) {
            POOPet pOOPet = nets[i];
            if(pOOPet != pet) enemy = (Pet)pOOPet;
        }
        
        ArrayList<MyComp> removetmp = new ArrayList<>();
        for (int i = 0; i < comp.size(); i++) {
            MyComp myComp = comp.get(i);
            myComp.getComp().setVisible(true);
            POOCoordinate coor = myComp.getCoor();
            coor.x += speed * Direction.get(i).x;
            coor.y += speed * Direction.get(i).y;
            myComp.setCoor(coor);
            myComp.draw();
            
            int x = enemy.comp.getCoor().x + enemy.comp.getComp().getSize().height/2 - myComp.getCoor().x;
            int y = enemy.comp.getCoor().y + enemy.comp.getComp().getSize().width/2 - myComp.getCoor().y;
            if(x==0) Direction.get(i).x = 0;
            if(y==0) Direction.get(i).y = 0;

            if( counter > 0 && counter % 10 == 0){
                int offset = 1;
                if(x > 0) Direction.get(i).x = Direction.get(i).x + offset > 2 ? 2 : Direction.get(i).x + offset;
                else if(x < 0)Direction.get(i).x = Direction.get(i).x - offset < -2 ? -2 : Direction.get(i).x - offset;
                if(y > 0) Direction.get(i).y = Direction.get(i).y + offset > 2 ? 2 : Direction.get(i).y + offset;
                else if(y < 0)Direction.get(i).y = Direction.get(i).y - offset < -2 ? -2 : Direction.get(i).y - offset;
            }
            
            POOPet tmp = null;
            ArrayList<Pet> blocklist = new ArrayList<>();
            blocklist.add(npet);
            
            int index = ((Arena)arena).searchBlockPosition(myComp.getBounds(), blocklist);
            if(index >= 0) tmp = arena.getAllPets()[index];
            if(tmp != null){
                act(tmp);
                myComp.dispose();
                removetmp.add(myComp);
            }
        }
        for (MyComp myComp : removetmp) 
            comp.remove(myComp);
        
        
        if( counter == 80 || comp.size() == 0 ){
            for (MyComp myComp : comp)
                myComp.dispose();
            npet.setState(POOUtil.delStatus(npet.getState(), POOConstant.STAT_USERDEFINE3));
            return -1;
        }
        return 0;
    }

    @Override
    protected boolean require(POOPet pet) {
        npet = ((Pet)pet);
        if(POOUtil.isStatus(npet.getState(), POOConstant.STAT_USERDEFINE3)) return false;
        npet.setState(POOUtil.setStatus(npet.getState(), POOConstant.STAT_USERDEFINE3));
        return pet.setMP(pet.getMP()-30);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(pet.getHP()-2<0?0:pet.getHP()-2, POOSkillConstant.NORMAL);
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
        if(counter%5 == 0)((Pet)pet).comp.getComp().setVisible(false);
        else ((Pet)pet).comp.getComp().setVisible(true);
        
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
                    POOPet tmp = arena.getAllPets()[index];
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
        ((Pet)pet).damage(pet.getHP()-30<0?0:pet.getHP()-30, POOSkillConstant.IGNORE_GUARD);
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
        System.err.println("Used the shockwave");
        JPanel laser = new JPanel();
        comp = new MyComp(npet.comp.getComp().getParent(), laser, npet.comp.getCoor().x + npet.comp.getComp().getHeight()/2, npet.getComp().getCoor().y + npet.comp.getComp().getWidth(), npet.comp.getComp().getParent().getWidth(), 10);
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
                System.err.println(index);
                if(index >= 0){
                    POOPet tmp = arena.getAllPets()[index];
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
        ((Pet)pet).damage(pet.getHP()-30<0?0:pet.getHP()-5, POOSkillConstant.IGNORE_GUARD);
    }
    
}