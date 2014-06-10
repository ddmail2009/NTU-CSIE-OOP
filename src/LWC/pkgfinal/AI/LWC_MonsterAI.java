package LWC.pkgfinal.AI;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Skill.*;
import java.awt.Point;


public class LWC_MonsterAI extends LWC_AbstractAI {
	protected int detect_radius = 200;
	private int time = 0;
	protected LWC_AbstractSkill skill;
	public LWC_MonsterAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
		super(rpg, obj);
		skill = new LWC_SkillPoison(rpg);
	}

    public void touchHurt(LWC_AbstractCharacter player){
        Point p = new Point();
        p.y -= 5;
        if(obj.getRegister("Position").equals("right")) p.x += 10;
        else p.x -= 10;

        rpg.movePosition(player, p, true);
        player.damage(4, obj);
    }    
    
	@Override
	public Point move() {
		LWC_AbstractCharacter player = rpg.getPlayer();
		LWC_AbstractCharacter target = null;
		int random = 0;
		if(time == 0) {
			random = (int)(Math.random() * 10.0);
			if(random <= 7)
				obj.setRegister("run", "true");
			else
				obj.clearRegister("run");
		}
		Point playerPoint = LWC_Util.getCenter(rpg, player);
		Point objPoint = LWC_Util.getCenter(rpg, obj);
		int xlength = ((obj.show()).getWidth() + (player.show()).getWidth())/2;
		int ylength = ((obj.show()).getHeight() + (player.show()).getHeight())/2;
		xlength = (xlength * 3) / 4;
		ylength = (ylength * 3) / 4;
		if(time == 0 && Math.abs(playerPoint.x - objPoint.x) < xlength && Math.abs(playerPoint.y - objPoint.y) < ylength)
		{
			touchHurt(player);
		}
		if(obj.getRegister("run") == "true")
		{
			if(LWC_Util.distance(rpg.getPosition(player), rpg.getPosition(obj)) < detect_radius)
				target = player;
			if(target != null)
			{
				Point p = LWC_Util.getVector(rpg.getPosition(obj), rpg.getPosition(target));
					  p = LWC_Util.vectorNormalize(p, 5);
				if(p.x != 0)
				{
					time++;
					if(p.x > 0)
						obj.setRegister("Position", "right");
					else
						obj.setRegister("Position", "left");
					if((int)(Math.random() * 100) >= 90 && skill.require(obj) == true)
					{	
						if(LWC_Util.distance(rpg.getPosition(player), rpg.getPosition(obj)) < 100)
						{
							obj.clearRegister("run");
							skill.register(obj);
						}
					}
					if(time > 60)  
						time = 0;
					return p;
				}
				else
				{
					obj.clearRegister("run");
					time++;
					if(time > 40)
						time = 0;
					return new Point();				
				}
			}
			else
			{
				obj.clearRegister("run");
				time++;
				if(time > 40)
					time = 0;
				return new Point();				
			}
		}
		else
		{
			time++;
			if(time > 40)
				time = 0;
			return new Point();
		}
	}

	@Override
	public boolean AIaction() {
		if(obj.getHP() > 0)return true;
		else return false;
	}
	
}
