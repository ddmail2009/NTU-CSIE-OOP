package ntu.csie.oop13spring;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public abstract class Effects extends POOSkill{
    private String name, description;
    /**
     * pet who used the effect
     */
    protected Pet from;
    /**
     * the arena instance
     */
    protected Arena arena;
    /**
     * constructor of Effect
     * @param str The Name
     * @param des The Simple description
     */
    protected Effects(String str, String des){
        name = str;
        description = des;
    }
	/**
     * This effect's name
     * @return name
     */
    public final String getName(){
		return name;
	}
    /**
     * the effect description
     * @return description
     */
    public final String getDesString(){
        return description;
    }
    @Override
    public abstract void act(POOPet pet);
}

abstract class TimerEffects extends Effects{
    protected int counter;
    public TimerEffects(String str, String des){
        super(str, des);
    }
    public abstract boolean require(POOPet pet, Arena arena);
    public abstract void startTimer();
    public abstract int update(Arena arena);
}

class Move extends Effects{
    Move(){
        super("Move", "");
    }
    Move(String str, String Des){
		super(str, Des);
	}
    @Override
    public void act(POOPet pet){
       ;
    }
}

class Guard extends TimerEffects{
    private POOJComp comp;
    
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
    public void startTimer() {
        comp = new POOJComp(from.comp.getComp().getParent(), new JLabel(POOUtil.getIcon(POOUtil.getCWD()+"images/attack/shield.png")), 0, 0);
        comp.setCenter(from.comp.getCenter());
        counter = 0;
    }

    @Override
    public int update(Arena arena) {
        counter ++;
        comp.setCenter(from.comp.getCenter());
        if(counter % 4 == 0)from.setMP(from.getMP()-1);
        
        if( !arena.getKey().contains(from.actionkeys[5]) ){
            act(from);
            comp.dispose();
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet)pet;
        this.arena = arena;
        if(POOUtil.isStatus(((Pet)pet).getState(), POOConstant.STAT_GUARD) && pet.getMP() > 5)return false;
        ((Pet)pet).setState(POOUtil.setStatus( ((Pet)pet).getState(), POOConstant.STAT_LOCK | POOConstant.STAT_GUARD ));
        return true;
    }
}

class Jump extends TimerEffects{
    private double velocity = 3;
    
    public Jump(String str, String des) {
        super(str, des);
    }
    Jump(){
        super("Jump", "Jump to avoid attack");
    }

    @Override
    public void startTimer() {
        ;
    }

    @Override
    public int update(Arena arena) {
        POOCoordinate coor = from.comp.getCoor();
        coor.x -= velocity;
        from.comp.setCoorForce(coor);
        
        velocity -= 0.2;
        
        if(velocity < -4) {
            act(from);
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet)pet;
        this.arena = arena;
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

class MissleAttack extends TimerEffects{
    private ArrayList<POOJComp> comp = new ArrayList<>(4);
    private ArrayList<Point> acceleration = new ArrayList<>(4);
    private int speed = 2;

    public MissleAttack(String str, String des) {
        super(str, des);
    }
    
    public MissleAttack(){
        super("MissleAttck", "Launch your ultimate attack");
    }
    
    @Override
    public void startTimer() {       
        for (int i = 0; i < 4; i++) {             
            comp.add( new POOJComp(from.comp.getComp().getParent(), new JPanel(), from.comp.getCoor().x+from.comp.getComp().getHeight()/2, from.comp.getCoor().y+from.comp.getComp().getWidth()/2, 10, 10) );
            comp.get(i).getComp().setBorder(new LineBorder(Color.black));
            comp.get(i).getComp().setBackground(Color.red);
            comp.get(i).getComp().setVisible(false);
            acceleration.add( new Point(i/2-1, i%2-1) );
        }
    }

    @Override
    public int update(Arena arena) {
        counter ++;
        
        ArrayList<Pet> enemys = ((Arena)arena).getAllPetsExcept(from);
        for (int i=comp.size()-1; i>=0 ; i--) {
            POOJComp myComp = comp.get(i);
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
            blocklist.add(from);
            
            int index = ((Arena)arena).searchBlockPosition(myComp.getBounds(), blocklist);
            if(index >= 0) tmp = ((Arena)arena).allpets.get(index);
            if(tmp != null){
                act(tmp);
                myComp.dispose();
                comp.remove(myComp);
            }
        }
        
        
        if( counter == 80 || comp.size() == 0 ){
            for (POOJComp myComp : comp) myComp.dispose();
            from.clearRegister("MissleAttack");
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = ((Pet)pet);
        this.arena = arena;
        
        if(from.getRegister("MissleAttack") != null ) return false;
        from.setRegister("MissleAttack", new Object());
        return pet.setMP(pet.getMP()-30);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(arena, from, pet.getHP()-5, POOSkillConstant.NORMAL);
    }
}

class BodyBlink extends TimerEffects{
    public BodyBlink(String str, String des) {
        super(str, des);
    }
    public BodyBlink() {
        super("BodyBlink", "No Actual Effect");
    }

    @Override
    public void startTimer() {
        counter = 0;
    }

    @Override
    public int update(Arena arena) {
        counter ++;
        if(counter == 10) from.comp.getComp().setVisible(false);
        else if(counter == 20) from.comp.getComp().setVisible(true);
        
        if(counter >= 20) {
            act(from);
            return -1;
        }
        else return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet)pet;
        this.arena = arena;
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

class Bombs extends TimerEffects{
    private POOJComp comp;
    private ImageIcon []images;
    ArrayList<Pet> blocklist = new ArrayList<>();
            
    public Bombs(String str, String des) {
        super(str, des);
    }
    public Bombs(){
        super("Bombs", "Set Bombs, damage foe in certain area");
    }

    @Override
    public void startTimer() {
        images = new ImageIcon[5];
        images[0] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb1.png") );
        images[1] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb2.png") );
        images[2] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb3.png") );
        images[3] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/bomb4.png") );
        images[4] = new ImageIcon( POOUtil.getImage(POOUtil.getCWD() + "images/explode.png") );
        
        JLabel bomb = new JLabel(images[0]);
        comp = new POOJComp(from.comp.getComp().getParent(), bomb, 0, 0, bomb.getIcon().getIconHeight(), bomb.getIcon().getIconWidth());
        comp.setCoor(new Coordinate(from.comp.getCoor().x + from.comp.getComp().getSize().height/2 - bomb.getIcon().getIconHeight()/2 + 10, from.comp.getCoor().y + from.comp.getComp().getSize().width/2 - bomb.getIcon().getIconWidth()/2 + 10));
        comp.getComp().setVisible(false);
        counter = 0;
    }

    @Override
    public int update(Arena arena) {
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
            from.setState(POOUtil.delStatus(from.getState(), POOConstant.STAT_USERDEFINE1));
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet) pet;
        this.arena = arena;
        
        if(from.getTimer("Bomb") != null && from.getTimer("Bomb") < 60) return false;
        from.setTimer("Bomb", 0);
        return pet.setMP(pet.getMP() - 15);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(arena, from, pet.getHP()-30, POOSkillConstant.IGNORE_GUARD | POOSkillConstant.SELF_ATTACK);
    }
}

class ShockWave extends TimerEffects{
    private POOJComp comp;
    ArrayList<Pet> blocklist = new ArrayList<>();
    
    public ShockWave(String str, String des) {
        super(str, des);
    }
    public ShockWave(){
        super("ShockWave", "A PowerFul ShockWave across the screen");
    }

    @Override
    public void startTimer() {
        JPanel laser = new JPanel();
        comp = new POOJComp(from.comp.getComp().getParent(), laser, from.comp.getCoor().x + from.comp.getComp().getHeight()/2, from.comp.getCoor().y + from.comp.getComp().getWidth(), from.comp.getComp().getParent().getWidth(), 10);
        laser.setBackground(Color.red);
        comp.getComp().setVisible(false);
        blocklist.add(from);
        counter = 0;
    }

    @Override
    public int update(Arena arena) {
        this.arena = arena;
        comp.getComp().setVisible(true);
        comp.draw();
        counter ++;
        
        if(counter % 20 == 0){
            blocklist.clear();
            blocklist.add(from);
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
            from.setState(POOUtil.delStatus(from.getState(), POOConstant.STAT_USERDEFINE2));
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet) pet;
        this.arena = arena;
        
        if(POOUtil.isStatus(from.getState(), POOConstant.STAT_USERDEFINE2)) return false;
        from.setState(POOUtil.setStatus(from.getState(), POOConstant.STAT_USERDEFINE2));
        return pet.setMP(pet.getMP() - 10);
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).damage(arena, from, pet.getHP()-5, POOSkillConstant.IGNORE_GUARD);
    }
}

class Message extends TimerEffects{
    private POOJComp comp;
    private String message;
    private Color color = new Color(0, 0, 0);
    
    public Message(String str, String des) {
        super(str, des);
    }
    public Message(){
        super("Message", "Deliver Message to screen");
    }

    @Override
    public void startTimer() {
        comp = new POOJComp(from.comp.getComp().getParent(), new JLabel( message, JLabel.CENTER ), from.comp.getCoor().x-10, from.comp.getCoor().y-20, 20, 20);
        comp.getComp().setForeground(color);
        counter = 0;
    }

    @Override
    public int update(Arena arena) {
        counter ++;
        comp.getComp().setVisible(true);
        POOCoordinate tmp = comp.getCoor();
        if(counter %5 == 0) tmp.x -= 1;
        comp.setCoor(tmp);
        comp.draw();
        
        if(counter >= 20){
            comp.dispose();
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        from = (Pet)pet;
        this.arena = arena;
        
        message = (String)from.getClearRegister("Message");
        Color tmp = (Color)from.getClearRegister("MessageColor");
        if(tmp != null) color = tmp;
        if(message == null) return false;
        return true;
    }

    @Override
    public void act(POOPet pet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

