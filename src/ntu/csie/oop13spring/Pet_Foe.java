package ntu.csie.oop13spring;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Pet_Foe extends Arena_Pet{
	private ArrayList<Skills> petskill = new ArrayList<>();
    private BufferedImage[] images = new BufferedImage[4];
    
	public Pet_Foe(){
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
        
		petskill.add(new Attack());
		petskill.add(new ArcaneStorm());
        petskill.add(new ArcaneStorm());
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
        
		POOAction e = new POOAction();
		e.skill = new NULL_SKILL();
		e.dest = null;
		return e;
	}

    @Override
	protected POOCoordinate move(POOArena oldarena){
        Arena arena = (Arena)oldarena;
        ArrayList<Integer> keys = arena.petGetKey(this);
        int key = 0;
        try{
            key = keys.get(0);
            keys.remove(0);
            
            if(key > 0 && movestate.setState(key));
            else if(key < 0 && movestate.delState(-key));
        }catch(Exception e){
            ;
        }
        POOCoordinate coor = comp.getCoor();
        if(movestate.isDOWN())coor.x++;
        if(movestate.isUP())coor.x--;
        if(movestate.isRight())coor.y++;
        if(movestate.isLeft())coor.y--;
        comp.setCoor(coor);
		return null;
	}

    @Override
    protected void initComp(Container container) {
        comp = new MyComp(container, new JLabel(new ImageIcon(images[0])), container.getSize().height/2, container.getSize().width*3/4);
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

    @Override
    public int GetCurrentDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetCurrentDirection(int direction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
