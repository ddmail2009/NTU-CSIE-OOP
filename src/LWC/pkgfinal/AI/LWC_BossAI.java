package LWC.pkgfinal.AI;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Skill.*;
import java.awt.Point;
import java.awt.Rectangle;


public class LWC_BossAI extends LWC_MonsterAI {
	public LWC_BossAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
		super(rpg, obj);
		skill = new LWC_SkillBossSwing(rpg, obj);
        detect_radius = 800;
	}

 
	@Override
	public Point move() {
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
			if(LWC_Util.distance(playerPoint, objPoint) < detect_radius && player != null){
				Point p = new Point(playerPoint.x - objPoint.x, playerPoint.y - objPoint.y);
                      p = LWC_Util.vectorNormalize(p, 5);
                if(p.x != 0 || p.y != 0){
                    if(p.x > 0) obj.setRegister("Position", "right");
                    else obj.setRegister("Position", "left");
                    if((int)(Math.random() * 100) >= 90 && skill.require(obj) == true){	
                        System.err.println("Distance: " + LWC_Util.distance(playerPoint, objPoint));
                        if(LWC_Util.distance(playerPoint, objPoint) < 200){
                            obj.clearRegister("run");
                            skill.register(obj);
                        }
                    }
                    rpg.setPosition(obj, new Point(rpg.getPosition(obj).x + p.x, rpg.getPosition(obj).y + p.y));
                    return p;
                }
                else{
                    obj.clearRegister("run");
                    return new Point();				
                }
            }
			else{
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
