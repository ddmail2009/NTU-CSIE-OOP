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
	protected abstract POOPet targetcheck(POOArena arena);
    @Override
    public abstract void act(POOPet pet);
    public boolean INSIDE(int l, int m, int h){
        return (l<=m && m<h) || (h<=m && m<l);
    }
}

 abstract class TimerSkills extends Skills{
    protected boolean lock;
    
    public void setLock(boolean lock){
        this.lock = lock;
    }
    public TimerSkills(String str, String des){
        super(str, des);
    }
    public boolean isLock(){
        return lock;
    }
    public abstract void startTimer(POOArena arena);
    public abstract int update(final POOPet pet, Arena arena);
}

class ArcaneStorm extends Skills{
	ArcaneStorm(){
		super("ArcaneStorm", "Cost 25 MP and Damage Foe 25 HP");
	}
    ArcaneStorm(String str, String Des){
		super(str, Des);
	}
    @Override
	protected boolean require(POOPet pet){
		return pet.setMP(pet.getMP()-25);
	}
    @Override
	protected POOPet targetcheck(POOArena arena){
		return null;
	}
    @Override
	public void act(POOPet pet){
		pet.setHP(pet.getHP()-25<0?0:pet.getHP()-25);
	}
}

class Attack extends TimerSkills{
    private int Direction;
    private Arena_Pet npet;
    private MyComp comp;
    private boolean hit = false;
    private Count_Task count = new Count_Task(20) {
        @Override
        public int task() {
            comp.getComp().setVisible(false);
            comp.getComp().getParent().remove(comp.getComp());
            return 20;
        }
    };
    
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
		pet.setHP(pet.getHP()-1);
	}
    
    protected void obj_init(final POOArena arena){
        Dimension size = npet.comp.getComp().getSize();
        Point objoffset = new Point();
        Dimension objSize = new Dimension(10, 5);
        
        if(Direction == MoveState.STATE_DOWN) objoffset = new Point(npet.comp.getCoor().x + size.height, npet.comp.getCoor().y + size.width/2);
        else if(Direction == MoveState.STATE_UP) objoffset = new Point(npet.comp.getCoor().x, npet.comp.getCoor().y + size.width/2);
        else if(Direction == MoveState.STATE_LEFT) objoffset = new Point(npet.comp.getCoor().x + size.height/2, npet.comp.getCoor().y);
        else if(Direction == MoveState.STATE_RIGHT)objoffset = new Point(npet.comp.getCoor().x + size.height/2, npet.comp.getCoor().y + size.width);
            
        if(Direction == MoveState.STATE_LEFT || Direction == MoveState.STATE_RIGHT) objSize = new Dimension(5, 10);
        
        comp = new MyComp(npet.comp.getComp().getParent(), new JPanel(), objoffset.x, objoffset.y, objSize.height, objSize.width);
        comp.getComp().setBorder(new LineBorder(Color.black));
        comp.getComp().setBackground(Color.red);
    }
    
    public void startTimer(final POOArena arena){
        obj_init(arena);
        comp.getComp().setVisible(false);
    }
    
    public int update(POOPet pet, Arena arena){       
        POOCoordinate coorr = comp.getCoor();
        if(Direction == MoveState.STATE_DOWN) coorr.x += 5;
        else if(Direction == MoveState.STATE_UP) coorr.x -= 5;
        else if(Direction == MoveState.STATE_LEFT) coorr.y -= 5;
        else coorr.y += 5;
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
            comp.getComp().setVisible(false);
            comp.getComp().getParent().remove(comp.getComp());
        }
        
        
        if( tmp != null || count.run() == 20 ){
            return -1;
        }
        return 0;
    }
    @Override
    protected POOPet targetcheck(POOArena arena) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    protected  POOPet targetcheck(POOArena arena){
        return null;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}

class NULL_SKILL extends Skills{
    NULL_SKILL(){
        super("Empty", "Empty Skill");
    }
    NULL_SKILL(String str, String Des){
		super(str, Des);
	}
    @Override
    protected  boolean require(POOPet pet){
        return true;
    }
    
    @Override
    protected  POOPet targetcheck(POOArena arena){
        return null;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}

class Guard extends Skills{
    Guard(){
        super("Guard", "Guard yourself");
    }
    Guard(String str, String Des){
		super(str, Des);
	}
    @Override
    protected  boolean require(POOPet pet){
        ((Arena_Pet)pet).movestate.setLock(true);
        return true;
    }
    
    @Override
    protected  POOPet targetcheck(POOArena arena){
        return null;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}

class UnGuard extends Skills{
    UnGuard(){
        super("UnGuard", "UnGuard yourself");
    }
    UnGuard(String str, String Des){
		super(str, Des);
	}
    @Override
    protected  boolean require(POOPet pet){
        ((Arena_Pet)pet).movestate.setLock(false);
        return true;
    }
    
    @Override
    protected  POOPet targetcheck(POOArena arena){
        return null;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}