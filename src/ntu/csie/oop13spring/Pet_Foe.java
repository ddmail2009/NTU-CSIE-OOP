package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Pet_Foe extends Arena_Pet{
    private BufferedImage []images;
    
    Count_Task mp_regeneration = new Count_Task(10) {
        @Override
        public int task() {
            return (setMP(getMP()<100 ? getMP()+1 : 100) == true? 1 : 0 );
        }
    };
     
	public Pet_Foe(){
        super();
		init(100, 100, 3, "Foe", new MoveState());
	}
    
    @Override
    protected void initImage(){
        images = new BufferedImage[3];
        for(int i=0; i<3; i++){
            String a = String.format("%sFoe%d.png", POOUtil.getCWD()+"images/", i+1);
            images[i] = POOUtil.getImage(a);
        }
    }
    
    @Override
    protected void init(int HP, int MP, int AGI, String str, MoveState movestate){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
        this.movestate = movestate;
    }
    
    private Skills getSkills(int keycode){
        for(int i: movestate.keyDump())
            if(i == keycode || i == -keycode) return new Move();
        switch(keycode){
            case KeyEvent.VK_P: return new Attack();
            case KeyEvent.VK_O: return new Guard();
            case KeyEvent.VK_SHIFT: return new Jump();
            case KeyEvent.VK_0: return new MissleAttack();
        }
        
        return null;
    }

    private Skills getComboSkills(ArrayList<Integer> recentKey){
//        int []a = {KeyEvent.VK_SPACE, KeyEvent.VK_D, KeyEvent.VK_SPACE};
//        for(int i=0; i<recentKey.size(); i++){
//            int match = 0;
//            for(int j=0; j<3; j++){
//                if(recentKey.get(i) == a[i]) match += 1;
//            }
//            if(match == 3) return new MissleAttack();
//        }
        return null;
    }
    
    @Override
    public void setMovestate(MoveState movestate){
        this.movestate = movestate;
    }
    
    @Override
	protected POOAction act(POOArena oldarena){
        mp_regeneration.run();
        
        Arena arena = (Arena)oldarena;
        HashSet<Integer> keys = arena.getKey();
        try{
            int key = 0;
            for (Integer integer : keys) {
                if( getSkills(integer) != null && !(getSkills(integer) instanceof Move) )key = integer;
            }
            
            POOAction e = new POOAction();
            e.skill = getSkills(key);
            e.dest = null;
            
            if( ((Skills)e.skill).require(this) ){;
                if(e.skill instanceof TimerSkills){
                     skilllist.add(((TimerSkills)e.skill));
                     ((TimerSkills)e.skill).startTimer(arena);
                }
            }
            else e.skill = null;
            return e;
        }catch(Exception e){}
        return null;
	}

    @Override
	protected POOCoordinate move(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        HashSet<Integer> keys = arena.getKey();
        if(POOUtil.isStatus(State, POOConstant.STAT_LOCK)) return null;
        
        try{
            int key = 0;
            for (Integer integer : keys) {
                if(getSkills(integer) instanceof Move) {
                    key = integer;
                    
                    POOCoordinate coor = comp.getCoor();
                    if(movestate.isDOWN(key)) coor.x += getAGI();
                    if(movestate.isUP(key)) coor.x -= getAGI();
                    if(movestate.isRight(key)) coor.y += getAGI();
                    if(movestate.isLeft(key)) coor.y -= getAGI();
                    comp.setCoor(coor);
                }
            }
            
            if(movestate.isDOWN(key)) SetCurrentDirection(POOConstant.STAT_DOWN);
            else if(movestate.isUP(key)) SetCurrentDirection(POOConstant.STAT_UP);
            else if(movestate.isRight(key)) SetCurrentDirection(POOConstant.STAT_RIGHT);
            else if(movestate.isLeft(key)) SetCurrentDirection(POOConstant.STAT_LEFT);
        }catch(Exception e){
            ;
        }
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
        comp = new MyComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/2, container.getSize().width/4);
        comp.setLimit(new Rectangle(new Point(0, 0), container.getSize()));
    }

    @Override
    protected void initStatComp(Container container) {
        int height = container.getSize().height;
        int width = container.getSize().width;
        
        JLabel label = new JLabel(new ImageIcon(images[0]));
        
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

    int image_count = 0;
    @Override
    public void draw(Arena arena) {
        image_count = (image_count+1)%(images.length*10);
        ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[image_count/10]));
        comp.draw();
        
        statcomp.get(2).getComp().setSize(getHP()*statcomp.get(0).getComp().getSize().width/100, statcomp.get(2).getComp().getSize().height);
        statcomp.get(3).getComp().setSize(getMP()*statcomp.get(1).getComp().getSize().width/100, statcomp.get(3).getComp().getSize().height);
        
        for (MyComp myComp : statcomp)
            myComp.draw();
        
        ArrayList<TimerSkills> remove_todo = new ArrayList<>();
        for (TimerSkills timerSkills : skilllist) 
            if(timerSkills.update(this, arena) == -1) remove_todo.add(timerSkills);
        for (TimerSkills timerSkills : remove_todo) {
            skilllist.remove(timerSkills);
        }
    }
}