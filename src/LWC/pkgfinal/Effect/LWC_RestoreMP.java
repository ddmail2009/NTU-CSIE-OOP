package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Object.LWC_AbstractObject;

public class LWC_RestoreMP extends LWC_AbstractEffect implements LWC_Effect2Object{
	private LWC_AbstractObject obj;
	
	public LWC_RestoreMP(LWC_RPG rpg) {
		super(rpg, "restoreMP");
	}

	@Override
	public void act() {
		if(obj instanceof LWC_AbstractCharacter)
			((LWC_AbstractCharacter)obj).setMP(((LWC_AbstractCharacter)obj).getMP() + 10);
		else
			System.err.println("Can't restore MP on Obj which isn't LWC_Character");
	}

	@Override
	public void setObj(LWC_AbstractObject obj) {
		this.obj = obj;
	}
	
}
