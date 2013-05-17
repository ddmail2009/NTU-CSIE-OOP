package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Pet_Electivire extends Pet{
    private BufferedImage []images;
    
    
    Count_Task mp_regeneration;
     
	public Pet_Electivire(){
        super();
        
		init(100, 100, 3, "Electivire");
        
        actionkeys[0] = KeyEvent.VK_UP;
        actionkeys[1] = KeyEvent.VK_RIGHT;
        actionkeys[2] = KeyEvent.VK_DOWN;
        actionkeys[3] = KeyEvent.VK_LEFT;
        actionkeys[4] = KeyEvent.VK_COMMA;
        actionkeys[5] = KeyEvent.VK_PERIOD;
        actionkeys[6] = KeyEvent.VK_SLASH;
        
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
    
    private Effects getSkills(int keycode){
        for(int i=0; i<4; i++)
            if(actionkeys[i] == keycode) return new Move();
        if(actionkeys[4] == keycode)return new Attack();
        if(actionkeys[5] == keycode)return new Guard();
        if(actionkeys[6] == keycode)return new Jump();
        return null;
    }

    private Effects getComboSkills(Integer []recentKey){
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
	protected POOAction act(POOArena arena){
        mp_regeneration.run();
        
        POOAction e = new POOAction();
        e.skill = null;
        e.dest = null;
        
        e.skill = getComboSkills(((Arena)arena).getRecentKey());
        if( e.skill instanceof TimerEffects && ((TimerEffects)e.skill).require(this, (Arena)arena) )
            return e;

        for (Integer integer : ((Arena)arena).getKey()){
            e.skill = getSkills(integer);
            if( e.skill != null && !(e.skill instanceof Move) )
                if(((TimerEffects)e.skill).require(this, (Arena)arena)) return e;
        }
        return null;
	}

    @Override
	protected POOCoordinate move(POOArena arena){
        HashSet<Integer> keys = ((Arena)arena).getKey();
        if(POOUtil.isStatus(State, POOConstant.STAT_LOCK)) return null;
        
        int key = 0;
        POOCoordinate coor = comp.getCoor();
        for (Iterator<Integer> it = keys.iterator(); it.hasNext();) {
            Integer integer = it.next();
            
            if(getSkills(integer) instanceof Move) {
                key = integer;
                if(key == actionkeys[0]) coor.x -= getAGI();
                if(key == actionkeys[1]) coor.y += getAGI();
                if(key == actionkeys[2]) coor.x += getAGI();
                if(key == actionkeys[3]) coor.y -= getAGI();
            }
        }

        if(key == actionkeys[0]) SetCurrentDirection(POOConstant.STAT_UP);
        else if(key == actionkeys[1]) SetCurrentDirection(POOConstant.STAT_RIGHT);
        else if(key == actionkeys[2]) SetCurrentDirection(POOConstant.STAT_DOWN);
        else if(key == actionkeys[3]) SetCurrentDirection(POOConstant.STAT_LEFT);
		return coor;
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
        comp = new POOJComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/4, container.getSize().width*3/4);
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
  
        statcomp.add(new POOJComp(container, hp, height*1/5, width*7/20, width*6/10, height/5));
        statcomp.add(new POOJComp(container, mp, height*3/5, width*7/20, width*6/10, height/5));
        statcomp.add(new POOJComp(container, hp_back, height*1/5, width*7/20, width*6/10, height/5));
        statcomp.add(new POOJComp(container, mp_back, height*3/5, width*7/20, width*6/10, height/5));
        statcomp.add(new POOJComp(container, label, height/2-label.getIcon().getIconHeight()/2, width*3/20-label.getIcon().getIconWidth()/2));
    }

    @Override
    public void draw(Arena arena) {
        if(POOUtil.isInside(0, POOConstant.Direction_MAP.get(GetCurrentDirection()), 4)) 
            ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[POOConstant.Direction_MAP.get(GetCurrentDirection())]));
        comp.draw();
        
        for (String string : timer.keySet()) 
            timer.put(string, timer.get(string)+1);
        
        statcomp.get(2).getComp().setSize(getHP()*statcomp.get(0).getComp().getSize().width/100, statcomp.get(2).getComp().getSize().height);
        statcomp.get(3).getComp().setSize(getMP()*statcomp.get(1).getComp().getSize().width/100, statcomp.get(3).getComp().getSize().height);
        
        for (POOJComp myComp : statcomp)
            myComp.draw();
    }
}