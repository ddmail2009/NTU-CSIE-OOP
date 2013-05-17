package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class Attack extends TimerEffects{
    protected int speed = 6;
    protected MyComp comp;
    protected Line2D.Double Direction;
    protected BufferedImage pic = POOUtil.getImage(POOUtil.getCWD()+"images/attack/attack6.png");
    protected int damage = 1;
    
	Attack(){
		super("Attack", "Damage Foe 1 HP");
	}
    Attack(String str, String Des){
		super(str, Des);
	}
    
    @Override
    public void startTimer(){
        JLabel label = new JLabel(new ImageIcon(pic));
        
        Point objoffset = new Point(from.comp.getCoor().x + from.comp.getComp().getSize().height/2 - label.getIcon().getIconHeight()/2, from.comp.getCoor().y + from.comp.getComp().getSize().width/2 - label.getIcon().getIconWidth()/2);
        comp = new MyComp(from.comp.getComp().getParent(), label, objoffset.x, objoffset.y);
        comp.getComp().setVisible(false);
        counter = 0;
    }
    
    @Override
    public int update(Arena arena){       
        counter ++;
        comp.getComp().setVisible(true);
        
        POOCoordinate coorr = comp.getCoor();
        coorr.x += (int)(speed * Direction.x2);
        coorr.y += (int)(speed * Direction.y2);
        comp.setCoor(coorr);
        comp.draw();

        ArrayList<Pet> blocklist = new ArrayList<>();
        for (Pet pet1 : ((Arena)arena).allpets) {
            if(pet1.getName().equals(from.getName()))blocklist.add(pet1);
        }
        
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
    
    @Override
	public boolean require(POOPet pet, Arena arena){
        this.arena = arena;
        from = (Pet)pet;
        
        if(from.getTimer("Attack") != null && from.getTimer("Attack") < 30/from.getAGI()) return false;
        
        from.setTimer("Attack", 0);
        if(from.getRegister("AttackDirection") == null){
            if( from.GetCurrentDirection() == POOConstant.STAT_UP )Direction = new Line2D.Double(0, 0, -1, 0);
            else if( from.GetCurrentDirection() == POOConstant.STAT_DOWN )Direction = new Line2D.Double(0, 0, 1, 0);
            else if( from.GetCurrentDirection() == POOConstant.STAT_RIGHT )Direction = new Line2D.Double(0, 0, 0, 1);
            else if( from.GetCurrentDirection() == POOConstant.STAT_LEFT )Direction = new Line2D.Double(0, 0, 0, -1);
        }
        else{
            Coordinate d = ((Coordinate)from.getRegister("AttackDirection"));
            Direction = new Line2D.Double(0, 0, d.x/Math.sqrt(d.x*d.x+d.y*d.y), d.y/Math.sqrt(d.x*d.x+d.y*d.y));
            
            from.clearRegister("AttackDirection");
        }
        return pet.setMP(pet.getMP()-1);
	}
   
    @Override
	public void act(POOPet pet){
        int hp = pet.getHP()-damage;
		if(hp == ((Pet)pet).damage(arena, from, hp, POOSkillConstant.NORMAL))
            status_Attack(pet);
	}
    
    public void status_Attack(POOPet pet){
        ;
    }
}


class PoisonAttack extends Attack{
    public PoisonAttack() {
        super("Poison Attack", "Poison Attack, Poison Foe");
        init();
    }
    public PoisonAttack(String str, String Des){
        super(str, Des);
        init();
    }
    private void init(){
        speed = 4;
        pic = POOUtil.getImage(POOUtil.getCWD()+"images/attack/attack7.png");
    }
    
    @Override
    public void status_Attack(POOPet pet) {
        TimerEffects tmp = new Poison();
        if(tmp.require(pet, arena)){
            tmp.startTimer();
            arena.skillList.add(tmp);
        }
    }
}

class FireAttack extends Attack{
    public FireAttack() {
        super("Fire Attack", "Fire Attack, Burn Foe");
        init();
    }
    public FireAttack(String str, String Des){
        super(str, Des);
        init();
    }
    private void init(){
        speed = 4;
        pic = POOUtil.getImage(POOUtil.getCWD()+"images/attack/attack8.png");
    }

    @Override
    public void status_Attack(POOPet pet) {
        TimerEffects tmp = new Burn();
        if(tmp.require(pet, arena)){
            tmp.startTimer();
            arena.skillList.add(tmp);
        }
    }
}