package LWC.pkgfinal.Effect;
import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_Gravity extends LWC_AbstractStatus{
    public static final int gravity = 1;
    protected int speed = 0;
    protected int error = 0;
    protected boolean stop = false;

    public LWC_Gravity(LWC_RPG rpg, LWC_AbstractObject who) {
        super(rpg, "Gravity", who);
    }

    @Override
    protected boolean status_update() {
        if(owner.getRegister("Gravity") != null && owner.getRegister("Gravity") == this && rpg.getPosition(owner) != null){
            if(stop == false){
                speed += gravity;

                boolean end = false;
                while(true){
                    Point relative = new Point(0, speed);
                    Point tmp = rpg.movePosition(owner, relative, true);
                    if(tmp.y != relative.y){
                        if(speed > 0) speed -= 1;
                        else if(speed < 0) speed = 0;
                        end = true;
                        error += 1;
                    }
                    else{
                        if(end != true) error = 0;
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    public void stop(boolean s){
        this.stop = s;
        speed = 0;
    }
}
