package ntu.csie.oop13spring;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Pet_Kerrigan extends Arena_Pet{
	private ArrayList<Skills> petskill = new ArrayList<>();
    private BufferedImage[] images = new BufferedImage[4];
    private int CurrentDirection;
    private HashMap<Integer, POOSkill> KeyMapping = new HashMap<>();
     
	public Pet_Kerrigan(){
        super();
		init(100, 100, 10, "Kerrigan", new MoveState(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D));
	}
    
    @Override
    protected void initImage(){
        String cwd = System.getProperty("user.dir")+"\\";
        images[0] = Image.ReadImage(cwd+"Kerrigan0.png");
        images[1] = Image.ReadImage(cwd+"Kerrigan1.png");
        images[2] = Image.ReadImage(cwd+"Kerrigan2.png");
        images[3] = Image.ReadImage(cwd+"Kerrigan3.png");
    }
    
    @Override
    protected void init(int HP, int MP, int AGI, String str, MoveState movestate){
        setHP(HP);
        setMP(MP);
        setAGI(AGI);
        setName(str);
        this.movestate = movestate;
        
        KeyMapping.put(KeyEvent.VK_Q, new Attack());
        KeyMapping.put(KeyEvent.VK_SPACE, new Guard());
        KeyMapping.put(-KeyEvent.VK_SPACE, new UnGuard());
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
                if(Actionlock.isFinished()){
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
        int key = 0;
        try{
            key = keys.get(0);
//            keys.remove(0);
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
    
    public int GetCurrentDirection(){
        return CurrentDirection;
    }
    
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
        comp = new MyComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/2, container.getSize().width/4);
        comp.setLimit(new Rectangle(container.getBounds()));
    }

    @Override
    protected void initStatComp(Container container) {
        JLabel label = new JLabel(new ImageIcon(images[0]));
        statcomp = new MyComp(container, label, container.getSize().height/2-label.getIcon().getIconHeight()/2, container.getSize().height/5-label.getIcon().getIconWidth()/2);
    }

    @Override
    public void draw() {
        comp.draw();
        statcomp.draw();
    }
}
