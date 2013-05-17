package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Monster extends Pet{
    private BufferedImage images;
    
    Count_Task mp_regeneration;
     
	public Monster(){
        super();
		init(100, 100, 1, "Monster");
        this.mp_regeneration = new Count_Task(30/getAGI()) {
            @Override
            public int task() { 
                return (setMP(getMP()<100 ? getMP()+1 : 100) == true? 1 : 0 );
            }
        };
	}
    
    @Override
    protected void initImage(){
        images = POOUtil.getImage(POOUtil.getCWD() + "images/Volcarona.gif");
    }
    
    @Override
    protected void init(int HP, int MP, int AGI, String str){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
    }
    
    private Skills getSkills(int keycode){
        if(counter % 40 == 0)return new Attack();
        else return null;
    }

    private Skills getComboSkills(Integer []recentKey){
        return null;
    }
    
    @Override
	protected POOAction act(POOArena arena){
        mp_regeneration.run();
        
        POOAction e = new POOAction();
        e.skill = getSkills(0);
        e.dest = null;
        if(e.skill == null) return e;

        POOCoordinate coor = comp.getCenter();
        
        ArrayList<POOPet> block = new ArrayList<>();
        for (Pet pet : ((Arena)arena).allpets) {
            if(pet.getName().equals("Monster")) block.add(pet);
        }
        ArrayList<Pet> parr = ((Arena)arena).getAllPetsExcept(block);
        
        int min = 0;
        for(int i=0; i<parr.size(); i++)
            if( Math.pow(coor.x-parr.get(i).comp.getCoor().x, 2) + Math.pow(coor.y-parr.get(i).comp.getCoor().y, 2) < Math.pow(coor.x-parr.get(min).comp.getCoor().x, 2) + Math.pow(coor.y-parr.get(min).comp.getCoor().y, 2) )
                min = i;
        POOCoordinate t = parr.get(min).comp.getCenter();
        
        setRegister("AttackDirection", new Coordinate(t.x-coor.x, t.y - coor.y));
        if( e.skill != null && ((Skills)e.skill).require(this, (Arena) arena) ){
            return e;
            
        }
        else e.skill = null;
        return null;
	}

    private Point acceleration = new Point(0, 0);
    int counter = 0;
    @Override
	protected POOCoordinate move(POOArena oldarena){
        if(POOUtil.isStatus(State, POOConstant.STAT_LOCK)) return null;
        counter ++;
        POOCoordinate coor = comp.getCenter();
        if(counter % 10 == 0){
            ArrayList<POOPet> block = new ArrayList<>();
            for (Pet pet : ((Arena)oldarena).allpets) {
                if(pet.getName().equals("Monster")) block.add(pet);
            }
            ArrayList<Pet> parr = ((Arena)oldarena).getAllPetsExcept(block);

            int min = 0;
            for(int i=0; i<parr.size(); i++)
                if( Math.pow(coor.x-parr.get(i).comp.getCoor().x, 2) + Math.pow(coor.y-parr.get(i).comp.getCoor().y, 2) < Math.pow(coor.x-parr.get(min).comp.getCoor().x, 2) + Math.pow(coor.y-parr.get(min).comp.getCoor().y, 2) )
                    min = i;
            Pet target = parr.get(min);
            POOCoordinate t = target.comp.getCenter();
            if(t.x < coor.x) acceleration.x -= 1;
            else if(t.x > coor.x)acceleration.x += 1;
            if(t.y < coor.y) acceleration.y -= 1;
            else if(t.y > coor.y)acceleration.y += 1;
            acceleration.x = POOUtil.ABSLimit(acceleration.x, getAGI());
            acceleration.y = POOUtil.ABSLimit(acceleration.y, getAGI());
        }
        
        acceleration.x += (int)(Math.random()*3-1.5);
        acceleration.y += (int)(Math.random()*3-1.5);
        coor.x += acceleration.x;
        coor.y += acceleration.y;
        comp.setCenter(coor);

        if(acceleration.x < 0) SetCurrentDirection(POOConstant.STAT_UP);
        else if(acceleration.y > 0) SetCurrentDirection(POOConstant.STAT_RIGHT);
        else if(acceleration.x < 0) SetCurrentDirection(POOConstant.STAT_DOWN);
        else if(acceleration.y < 0) SetCurrentDirection(POOConstant.STAT_LEFT);
		return null;
	}
    
    @Override
    public int GetCurrentDirection(){
        return POOUtil.getDirection(State);
    }
    
    @Override
    public void SetCurrentDirection(int direction){
        if(!POOUtil.isStatus(State, direction)){
            State = POOUtil.delStatus(State, POOConstant.STAT_DOWN);
            State = POOUtil.delStatus(State, POOConstant.STAT_UP);
            State = POOUtil.delStatus(State, POOConstant.STAT_LEFT);
            State = POOUtil.delStatus(State, POOConstant.STAT_RIGHT);
            State = POOUtil.setStatus(State, direction);
        }
    }

    @Override
    protected void initComp(Container container) {
        JLabel label;
        try{
            label = new JLabel(new ImageIcon(new URL("file:/"+POOUtil.getCWD()+"images/Volcarona.gif")));
        }catch(Exception e){
            label = new JLabel(new ImageIcon(images));
        }
        
        comp = new MyComp(container, label, container.getSize().height/2 + (int)(Math.random()*10), (int)(container.getSize().width*Math.random()*3/4) + (int)(Math.random()*10));
        comp.setLimit(new Rectangle(0, 0, container.getSize().width, container.getSize().height));
    }

    @Override
    protected void initStatComp(Container container) {
        int height = container.getSize().height;
        int width = container.getSize().width;
        
        JLabel label;
        try{
            label = new JLabel(new ImageIcon(new URL("file:/"+POOUtil.getCWD()+"images/Volcarona.gif")));
        }catch(Exception e){
            label = new JLabel(new ImageIcon(images));
        }
        
        JLabel hp = new JLabel("HP");
        hp.setHorizontalAlignment(SwingConstants.CENTER);
        hp.setBorder(new LineBorder(Color.black));
        
        JPanel hp_back = new JPanel();
        hp_back.setBackground(Color.red);
        
        JLabel mp = new JLabel("MP");
        mp.setHorizontalAlignment(SwingConstants.CENTER);
        mp.setBorder(new LineBorder(Color.black));
        
        JPanel mp_back = new JPanel();
        mp_back.setBackground(Color.blue);
  
        statcomp.add(new MyComp(container, hp, height*1/5, width*7/20, width*6/10, height/5));
        statcomp.add(new MyComp(container, mp, height*3/5, width*7/20, width*6/10, height/5));
        statcomp.add(new MyComp(container, hp_back, height*1/5, width*7/20, width*6/10, height/5));
        statcomp.add(new MyComp(container, mp_back, height*3/5, width*7/20, width*6/10, height/5));
        statcomp.add(new MyComp(container, label, height/2-label.getIcon().getIconHeight()/2, width*3/20-label.getIcon().getIconWidth()/2));
    }

    @Override
    public void draw(Arena arena) {
//        if(POOUtil.isInside(0, POOConstant.Direction_MAP.get(GetCurrentDirection()), 4)) 
//            ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[POOConstant.Direction_MAP.get(GetCurrentDirection())]));
        comp.draw();
        
        for (Map.Entry<String, Integer> ti : timer.entrySet()) {
            String string = ti.getKey();
            Integer integer = ti.getValue();
            
            timer.put(string, integer+1);
        }
        
        statcomp.get(2).getComp().setSize(getHP()*statcomp.get(0).getComp().getSize().width/100, statcomp.get(2).getComp().getSize().height);
        statcomp.get(3).getComp().setSize(getMP()*statcomp.get(1).getComp().getSize().width/100, statcomp.get(3).getComp().getSize().height);
        
        for (MyComp myComp : statcomp)
            myComp.draw();
      
        
    }
}