package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Pet_Foe extends Arena_Pet{
    private BufferedImage[] images = new BufferedImage[4];
    private int CurrentDirection;
    private HashMap<Integer, POOSkill> KeyMapping = new HashMap<>();
     
	public Pet_Foe(){
        super();
		init(100, 100, 10, "Foe", new MoveState());
	}
    
    @Override
    protected void initImage(){
        String cwd = System.getProperty("user.dir")+"\\";
        images[0] = Image.ReadImage(cwd+"Foe0.png");
        images[1] = Image.ReadImage(cwd+"Foe1.png");
        images[2] = Image.ReadImage(cwd+"Foe2.png");
        images[3] = Image.ReadImage(cwd+"Foe3.png");
    }
    
    @Override
    protected void init(int HP, int MP, int AGI, String str, MoveState movestate){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
        this.movestate = movestate;
        
        KeyMapping.put(KeyEvent.VK_NUMPAD4, new Attack());
        KeyMapping.put(KeyEvent.VK_NUMPAD5, new Guard());
        KeyMapping.put(-KeyEvent.VK_NUMPAD5, new UnGuard());
        for (int i : movestate.keyDump()) {
            KeyMapping.put(i, new Move());
            KeyMapping.put(-i, new Move());
        }
    }

    @Override
    public void setMovestate(MoveState movestate){
        this.movestate = movestate;
    }
    
    @Override
	protected POOAction act(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        ArrayList<Integer> keys = arena.petGetKey(this);
        try{
            int key = keys.get(0);
            if(KeyMapping.get(key) == null){
                keys.remove(0);
                return null;
            }
            else if(!(KeyMapping.get(key) instanceof Move)){
                keys.remove(0);
                if(Actionlock.isLock() == false){
                    POOAction e = new POOAction();
                    e.skill = new NULL_SKILL();
                    e.dest = null;

                    e.skill = KeyMapping.get(key);
                    e.dest = null;

                    ((Skills)e.skill).require(this);
                    if(e.skill instanceof TimerSkills){
                         Actionlock = (TimerSkills)e.skill;
                         ((TimerSkills)e.skill).startTimer(arena);
                    }
                    return e;
                }
                else return null;
            }
        }catch(Exception e){}
        return null;
	}

    @Override
	protected POOCoordinate move(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        ArrayList<Integer> keys = arena.petGetKey(this);
        try{
            int key = keys.get(0);
            if(KeyMapping.get(key) == null) keys.remove(0);
            else if(KeyMapping.get(key) instanceof Move){
                keys.remove(0);
                if(key > 0 && movestate.setState(key));
                else if(key < 0 && movestate.delState(-key));
            }
        }catch(Exception e){
            ;
        }
        
        POOCoordinate coor = comp.getCoor();
        if(movestate.isDOWN()) coor.x++;
        if(movestate.isUP()) coor.x--;
        if(movestate.isRight()) coor.y++;
        if(movestate.isLeft()) coor.y--;
        comp.setCoor(coor);
        
        if(movestate.isDOWN()) SetCurrentDirection(MoveState.STATE_DOWN);
        else if(movestate.isUP()) SetCurrentDirection(MoveState.STATE_UP);
        else if(movestate.isRight()) SetCurrentDirection(MoveState.STATE_RIGHT);
        else if(movestate.isLeft()) SetCurrentDirection(MoveState.STATE_LEFT);
		return null;
	}
    
    @Override
    public int GetCurrentDirection(){
        return CurrentDirection;
    }
    
    @Override
    public void SetCurrentDirection(int direction){
        if(CurrentDirection != direction){
            CurrentDirection = direction;
            if(direction == MoveState.STATE_LEFT) ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[3]));
            else if(direction == MoveState.STATE_RIGHT) ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[1]));
            else if(direction == MoveState.STATE_UP) ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[2]));
            else if(direction == MoveState.STATE_DOWN) ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[0]));
            else ((JLabel)comp.getComp()).setIcon(new ImageIcon(images[0]));
        }
    }

    @Override
    protected void initComp(Container container) {
        comp = new MyComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/2, container.getSize().width*3/4);
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
        statcomp.add(new MyComp(container, new JLabel(new ImageIcon(images[0])), height/2-label.getIcon().getIconHeight()/2, width*3/20-label.getIcon().getIconWidth()/2));
    }

    @Override
    public void draw(Arena arena) {
        comp.draw();
        statcomp.get(2).getComp().setSize(getHP()*statcomp.get(0).getComp().getSize().width/100, statcomp.get(2).getComp().getSize().height);
        statcomp.get(3).getComp().setSize(getMP()*statcomp.get(1).getComp().getSize().width/100, statcomp.get(3).getComp().getSize().height);
        for (MyComp myComp : statcomp) {
            myComp.draw();
        }
    }
}