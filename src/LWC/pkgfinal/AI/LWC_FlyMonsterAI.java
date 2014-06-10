package LWC.pkgfinal.AI;

import LWC.pkgfinal.Effect.LWC_FlyPoison;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Skill.*;
import java.awt.Point;
import java.awt.Rectangle;


public class LWC_FlyMonsterAI extends LWC_MonsterAI {

    private Point oriPoint = new Point(-1, -1);

	public LWC_FlyMonsterAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
		super(rpg, obj);
		skill = new LWC_SkillFlyPoison(rpg);
        detect_radius = 400;
	}
	
    

    public void retreat(){
        Point p = new Point(oriPoint.x - rpg.getPosition(obj).x, oriPoint.y - rpg.getPosition(obj).y);
              p = LWC_Util.vectorNormalize(p, 5);
        if(p.x != 0 || p.y != 0){
            if(p.x > 0) obj.setRegister("Position", "right");
            else obj.setRegister("Position", "left");
            rpg.setPosition(obj, new Point(rpg.getPosition(obj).x + p.x, rpg.getPosition(obj).y + p.y/2));
        }
    }
	@Override
	public Point move() {
        if(oriPoint.x == -1 && oriPoint.y == -1) oriPoint = rpg.getPosition(obj);
        
        obj.clearRegister("Gravity");
        if(obj.getCounter("FlyPoison") != null && obj.getRegister("FlyPoison") != null){
            if((int)obj.getCounter("FlyPoison") < (int)obj.getRegister("FlyPoison")) return new Point();
            else{
                obj.clearCounter("FlyPoison");
                obj.clearRegister("FlyPoison");
            }
        }
        
        if(LWC_Util.distance(oriPoint, rpg.getPosition(obj)) > detect_radius*2)
            obj.setRegister("Back", true);
        
        if(obj.getRegister("Back") != null){
            if(LWC_Util.distance(oriPoint, rpg.getPosition(obj)) > 10){
                retreat();
                return new Point();
            }
            else obj.clearRegister("Back");
        }
        
		LWC_AbstractCharacter player = rpg.getPlayer();
        
		Point playerPoint = LWC_Util.getCenter(rpg, player);
        Point objPoint = LWC_Util.getCenter(rpg, obj);
        
        Rectangle rect = LWC_Util.getObjArea(rpg, obj);
		int xlength = (rect.width + (player.show()).getWidth())/2;
		int ylength = (rect.height + (player.show()).getHeight())/2;
		if(Math.random() > 0.95 && Math.abs(playerPoint.x - objPoint.x) < xlength*3/4 && Math.abs(playerPoint.y - objPoint.y) < ylength*3/4)
            touchHurt(player);
        
        obj.setRegister("run", "true");
		if(obj.getRegister("run").equals("true") && obj.getRegister("Animate") == null){
			if(LWC_Util.distance(playerPoint, objPoint) < detect_radius){
				Point p = new Point(playerPoint.x - objPoint.x, playerPoint.y - objPoint.y);
                      p = LWC_Util.vectorNormalize(p, 5);
                if(p.x != 0 || p.y != 0){
                    if(p.x > 0) obj.setRegister("Position", "right");
                    else obj.setRegister("Position", "left");
                    if((int)(Math.random() * 100) >= 90 && skill.require(obj) == true){	
                        if(LWC_Util.distance(playerPoint, objPoint) < 100){
                            obj.clearRegister("run");
                            skill.register(obj);
                            obj.setRegister("FlyPoison", 40);
                            obj.setCounter("FlyPoison", 0);
                        }
                    }
                    rpg.setPosition(obj, new Point(rpg.getPosition(obj).x + p.x, rpg.getPosition(obj).y + p.y/2));
                    return p;
                }
                else{
                    obj.clearRegister("run");
                    return new Point();				
                }
            }
			else{
                retreat();
				obj.clearRegister("run");
                return new Point();
            }
		}
		else
			return new Point();
	}

	@Override
	public boolean AIaction() {
		if(obj.getHP() > 0)return true;
		else return false;
	}
	
}
