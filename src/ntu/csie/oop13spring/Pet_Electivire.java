package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Pet_Electivire extends Pet{
    private BufferedImage []images;
    
    
    Count_Task mp_regeneration;
     
	public Pet_Electivire(){
        super();
        
		init(100, 100, 3, "Electivire");
        
        actionkeys[0] = KeyEvent.VK_I;
        actionkeys[1] = KeyEvent.VK_L;
        actionkeys[2] = KeyEvent.VK_K;
        actionkeys[3] = KeyEvent.VK_J;
        actionkeys[4] = KeyEvent.VK_U;
        actionkeys[5] = KeyEvent.VK_O;
        actionkeys[6] = KeyEvent.VK_P;
        
        this.mp_regeneration = new Count_Task(30/getAGI()) {
            @Override
            public int task() { 
                return (setMP(getMP()<100 ? getMP()+1 : 100) == true? 1 : 0 );
            }
        };
	}
    
    
    @Override
    protected void initImage(){
        images = new BufferedImage[4];
        for(int i=0; i<images.length; i++){
            String a = String.format("%sElectivire%d.png", POOUtil.getCWD()+"images/", i);
            images[i] = POOUtil.getImage(a);
        }
    }
    
    @Override
    protected void init(int HP, int MP, int AGI, String str){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
    }
    
    private Skills getSkills(int keycode){
        for(int i=0; i<4; i++)
            if(actionkeys[i] == keycode) return new Move();
        if(actionkeys[4] == keycode)return new Attack();
        if(actionkeys[5] == keycode)return new Guard();
        if(actionkeys[6] == keycode)return new Jump();
        return null;
    }

    private Skills getComboSkills(Integer []recentKey){
        int [][]a = {
            {actionkeys[5], actionkeys[0], actionkeys[4]},
            {actionkeys[5], actionkeys[1], actionkeys[6]},
            {actionkeys[5], actionkeys[3], actionkeys[6]},
            {actionkeys[5], actionkeys[2], actionkeys[4]}
        };
        
        ArrayList<Integer> recent = new ArrayList<>();
        recent.addAll(Arrays.asList(recentKey));
        for (int i=recent.size()-1; i>=0; i--){
            int match = 0;
            for (int j = 0; j < actionkeys.length; j++)
                if(recent.get(i) == actionkeys[j])match ++;
            if(match == 0) recent.remove(i);
        }
        
        for(int i=0; i<recent.size(); i++){
            for(int j=0; j<a.length; j++){
                int match = 0;
                for(int k=0; k<a[j].length; k++)
                    if(i+k < recent.size() && recent.get(i+k) == a[j][k]) match += 1;
                if(match == a[j].length){
                    switch(j){
                        case 0: return new MissleAttack();
                        case 1: return new Bombs();
                        case 2: return new Bombs();
                        case 3: return new ShockWave();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
	protected POOAction act(POOArena oldarena){
        mp_regeneration.run();
        
        POOAction e = new POOAction();
        e.skill = null;
        e.dest = null;
        
        Arena arena = (Arena)oldarena;
        e.skill = getComboSkills(arena.getRecentKey());
        if(e.skill != null){
            if( ((Skills)e.skill).require(this, arena) ){
                return e;
            }
        }
        
        
        HashSet<Integer> keys = arena.getKey();
        int key = 0;
        for (Integer integer : keys) 
            if( getSkills(integer) != null && !(getSkills(integer) instanceof Move) )key = integer;
        
        e.skill = getSkills(key);
        e.dest = null;

        if( e.skill != null && ((Skills)e.skill).require(this, arena) ){
            return e;
        }
        else e.skill = null;
        return null;
	}

    @Override
	protected POOCoordinate move(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        HashSet<Integer> keys = arena.getKey();
        if(POOUtil.isStatus(State, POOConstant.STAT_LOCK)) return null;
        
        int key = 0;
        for (Integer integer : keys) {
            if(getSkills(integer) instanceof Move) {
                key = integer;

                POOCoordinate coor = comp.getCoor();
                if(key == actionkeys[0]) coor.x -= getAGI();
                if(key == actionkeys[1]) coor.y += getAGI();
                if(key == actionkeys[2]) coor.x += getAGI();
                if(key == actionkeys[3]) coor.y -= getAGI();
                comp.setCoor(coor);
            }
        }

        if(key == actionkeys[0]) SetCurrentDirection(POOConstant.STAT_UP);
        else if(key == actionkeys[1]) SetCurrentDirection(POOConstant.STAT_RIGHT);
        else if(key == actionkeys[2]) SetCurrentDirection(POOConstant.STAT_DOWN);
        else if(key == actionkeys[3]) SetCurrentDirection(POOConstant.STAT_LEFT);
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
        comp = new MyComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/4, container.getSize().width*3/4);
        comp.setLimit(new Rectangle(0, 0, container.getSize().width, container.getSize().height));
    }

    @Override
    protected void initStatComp(Container container) {
        int height = container.getSize().height;
        int width = container.getSize().width;
        
        JLabel label = new JLabel(new ImageIcon(images[2]));
        
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
        if(POOUtil.isInside(0, POOConstant.Direction_MAP.get(GetCurrentDirection()), 4)) 
            ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[POOConstant.Direction_MAP.get(GetCurrentDirection())]));
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