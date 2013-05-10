package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Pet_Isaac extends Arena_Pet{
    private BufferedImage[] images = new BufferedImage[4];
    private int CurrentDirection;
    private ArrayList<TimerSkills> skilllist = new ArrayList<>();
    Count_Task mp_regeneration = new Count_Task(30) {
        @Override
        public int task() {
            return (setMP(getMP()<100 ? getMP()+1 : 100) == true? 1 : 0 );
        }
    };
     
	public Pet_Isaac(){
        super();
		init(100, 100, 3, "Kerrigan", new MoveState(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D));
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
    }
    
    private Skills getSkills(int keycode){
        for(int i: movestate.keyDump())
            if(i == keycode || i == -keycode) return new Move();
        
        switch(keycode){
            case KeyEvent.VK_Q: return new Attack();
            case KeyEvent.VK_SPACE: return new Guard();
            case -KeyEvent.VK_SPACE: return new UnGuard();
        }
        
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
        HashSet<Integer> keys = arena.petGetKey(this);
        try{
            int key = 0;
            for (Integer integer : keys) {
                if( !(getSkills(integer) instanceof Move) )key = integer;
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
        HashSet<Integer> keys = arena.petGetKey(this);
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
            if(movestate.isDOWN(key)) SetCurrentDirection(MoveState.STATE_DOWN);
            else if(movestate.isUP(key)) SetCurrentDirection(MoveState.STATE_UP);
            else if(movestate.isRight(key)) SetCurrentDirection(MoveState.STATE_RIGHT);
            else if(movestate.isLeft(key)) SetCurrentDirection(MoveState.STATE_LEFT);
        }catch(Exception e){
            ;
        }
        
        
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
            ((Pet_Panel)comp.getComp()).set_image(direction);
        }
    }

    @Override
    protected void initComp(Container container) {
        Pet_Panel tmp = new Pet_Panel();
        comp = new MyComp(container, tmp, container.getSize().height/2, container.getSize().width/4);
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

class Pet_Panel extends JPanel{
    private BufferedImage []images = new BufferedImage[4];
    private BufferedImage current = images[0];
    
    public Pet_Panel(){
        init_images();
        set_image(MoveState.STATE_DOWN);
    }
    private void init_images(){
        String cwd = System.getProperty("user.dir")+"\\";
        images[0] = Image.ReadImage(cwd+"Kerrigan0.png");
        images[1] = Image.ReadImage(cwd+"Kerrigan1.png");
        images[2] = Image.ReadImage(cwd+"Kerrigan2.png");
        images[3] = Image.ReadImage(cwd+"Kerrigan3.png");
    }
    protected void set_image(int i){
        if(i == MoveState.STATE_DOWN) current = images[0];
        else if(i == MoveState.STATE_UP) current = images[2];
        else if(i == MoveState.STATE_LEFT) current = images[3];
        else if(i == MoveState.STATE_RIGHT) current = images[1];
        setPreferredSize(new Dimension(current.getWidth(), current.getHeight()));
    }
    
    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(current, 0, 0, null);
    }
}