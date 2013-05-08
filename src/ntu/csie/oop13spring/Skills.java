package ntu.csie.oop13spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit.*;

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
    protected Object lock;
    public TimerSkills(String str, String des){
        super(str, des);
    }
    public boolean isFinished(){
        return lock == null;
    }
    public abstract void startTimer(POOArena arena);
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
    private POOCoordinate coor;
    private Arena_Pet npet;
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    
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
        coor = npet.comp.getCoor();
		return true;
	}
    @Override
	protected POOPet targetcheck(POOArena arena){
        Coordinate tmp = new Coordinate(coor);
        if(Direction == MoveState.STATE_DOWN) tmp.x += 50*MyComp.ratio;
        else if(Direction == MoveState.STATE_UP) tmp.x -= 50*MyComp.ratio;
        else if(Direction == MoveState.STATE_LEFT) tmp.y -= 50*MyComp.ratio;
        else tmp.y += 50*MyComp.ratio;
        
        POOPet []parr = arena.getAllPets();
        for (int i = 0; i < parr.length; i++) {
            POOCoordinate t = arena.getPosition(parr[i]);
            if(parr[i]!=npet){
                if( coor.x != tmp.x && INSIDE(tmp.x, t.x, coor.x) )return parr[i];
                else if(coor.y != tmp.y && INSIDE(tmp.y, t.y, coor.y)) return parr[i];
            }
        }
        return null;
	}
    
    
    @Override
	public void act(POOPet pet){
		pet.setHP(pet.getHP()-1);
	}
    @Override
    public void startTimer(final POOArena arena) {
        System.out.println("StartTimer\n");
        lock = new Object();
        final Runnable beeper = new Runnable() {
            @Override
            public void run() {
                POOPet tmp = targetcheck(arena);
                if(tmp != null){
                    System.out.printf("%s attacked %s\n", npet.getName(), tmp.getName());
                    act(tmp);
                }
            }
        };
        final ScheduledFuture beeperHandler = timer.scheduleAtFixedRate(beeper, 0, 500, java.util.concurrent.TimeUnit.MILLISECONDS);
        timer.schedule(new Runnable() {
            @Override
            public void run() {
                beeperHandler.cancel(true);
                lock = null;
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
    }
}

class Move extends Skills{
    Move(){
        super("Move Right", "Move right and restore 1 MP");
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