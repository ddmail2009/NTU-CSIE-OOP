package ntu.csie.oop13spring;

import java.awt.Color;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Status extends TimerEffects{
    protected ImageIcon images = POOUtil.getIcon(POOUtil.getCWD()+"images/stat/poison2.png");
    protected Point offset = new Point(0, 0);
    protected MyComp comp;
    protected String secret = "Status";
    protected int lastTime = 100;
    protected int actInterval = 30;
    
    
    public Status(String str, String des) {
        super(str, des);
    }

    @Override
    public void startTimer() {
        comp = new MyComp(from.comp.getComp().getParent(), new JLabel(images), from.comp.getCoor().x+from.comp.getBounds().height+offset.x, from.comp.getCoor().y+from.comp.getBounds().width+offset.y);
    }

    @Override
    public int update(Arena arena) {
        comp.setCoor(new Coordinate(from.comp.getCoor().x+from.comp.getBounds().height+offset.x, from.comp.getCoor().y+from.comp.getBounds().width+offset.y));
        comp.draw();
        
        if(from.getTimer(secret) % actInterval == 0) act(from);
        if(from.getHP() <= 0 || from.getTimer(secret) > lastTime){
            comp.dispose();
            from.clearTimer(secret);
            return -1;
        }
        return 0;
    }

    @Override
    public boolean require(POOPet pet, Arena arena) {
        this.arena = arena;
        from = (Pet)pet;
        
        if(from.getTimer(secret) != null){
            from.setTimer(secret, from.getTimer(secret)-lastTime);
            return false;
        }
        else from.setTimer(secret, 0);
        return true;
    }

    @Override
    public void act(POOPet pet) {
        ;
    }
    
}
class Poison extends Status{   
    public Poison(String str, String des) {
        super(str, des);
        secret = "Poisin";
    }
    public Poison(){
        super("Poison", "Poison status");
        secret = "Poisin";
    }
    
    private void init(){
        images = POOUtil.getIcon(POOUtil.getCWD()+"images/stat/poison2.png");
        secret = "Poison";
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).setRegister("MessageColor", Color.green);
        ((Pet)pet).damage(arena, (Pet) pet, pet.getHP()-5, POOSkillConstant.IGNORE_GUARD | POOSkillConstant.SELF_ATTACK);
    }
}

class Burn extends Status{
    public Burn(String str, String des) {
        super(str, des);
        init();
    }
    public Burn(){
        super("Burn", "Burn status");
        init();
    }
    private void init(){
        images = POOUtil.getIcon(POOUtil.getCWD()+"images/stat/burn2.png");
        secret = "Burn";
        offset = new Point(0, -images.getIconWidth());
    }

    @Override
    public void act(POOPet pet) {
        ((Pet)pet).setRegister("MessageColor", new Color(255, 0, 0));
        ((Pet)pet).damage(arena, (Pet) pet, pet.getHP()-5, POOSkillConstant.IGNORE_GUARD | POOSkillConstant.SELF_ATTACK);
    }
}