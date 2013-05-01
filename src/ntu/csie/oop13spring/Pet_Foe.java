package ntu.csie.oop13spring;

import java.awt.event.KeyEvent;
import java.util.*;

public class Pet_Foe extends Pet_Object{
	private ArrayList<Skills> petskill = new ArrayList<>();
    private MoveState movestate;

	public Pet_Foe(){
		init(100, 100, 10, "Foe", new MoveState(), "Foe");
	}
    
    @Override
    public void init(int HP, int MP, int AGI, String str, MoveState movestate, String image){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
        this.movestate = movestate;
        this.SetImagePath(image);
		petskill.add(new Attack());
		petskill.add(new ArcaneStorm());
    }

    @Override
    public void setMovestate(MoveState movestate){
        this.movestate = movestate;
    }
    
    @Override
	protected POOAction act(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        
		POOPet[] allpet = arena.getAllPets();
        ArrayList<Integer> keys = arena.petGetKey(this);
        try{
            int key = keys.remove(0);
            
            if(key > 0 && movestate.setState(key));
            else if(key < 0 && movestate.delState(-key));
            
        }catch(Exception e){
            ;
        }
        
        POOCoordinate coor = arena.getPosition(this);
        if(movestate.isDOWN())coor.x++;
        if(movestate.isUP())coor.x--;
        if(movestate.isRight())coor.y++;
        if(movestate.isLeft())coor.y--;
        arena.setPosition(this, coor);
        
		POOAction e = new POOAction();
		e.skill = new NULL_SKILL();
		e.dest = null;
        
//		System.out.printf("%s use %s to %s\n", getName(), petskill.get(0).getName(), e.dest == null ? "null" : e.dest.getName() );
		return e;
	}

    @Override
	protected POOCoordinate move(POOArena arena){
		return new Coordinate(1,2);
	}

    @Override
    public Skills[] getSkillList() {
        return petskill.toArray(new Skills[0]);
    }
}