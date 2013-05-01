package ntu.csie.oop13spring;

public abstract class Skills extends POOSkill{
    private String name, description;

    public Skills(String str, String des){
        name = str;
        description = des;
    }

	public final String getName(){
		return name;
	}
    
    public final String getDesString(){
        return description;
    }

	protected abstract boolean require(POOPet pet);
	protected abstract boolean checkrequire(POOPet pet);
}


class ArcaneStorm extends Skills{
	ArcaneStorm(){
		super("ArcaneStorm", "Cost 25 MP and Damage Foe 25 HP");
	}
    ArcaneStorm(String str, String Des){
		super(str, Des);
	}
    @Override
	protected boolean require(POOPet pet){
		return pet.setMP(pet.getMP()-25);
	}

    @Override
	protected boolean checkrequire(POOPet pet){
		return pet.getMP()>=25;
	}

    @Override
	public void act(POOPet pet){
		pet.setHP(pet.getHP()-25<0?0:pet.getHP()-25);
	}
}

class Attack extends Skills{
	Attack(){
		super("Attack", "Damage Foe 1 HP");
	}
    Attack(String str, String Des){
		super(str, Des);
	}

    @Override
	protected boolean require(POOPet pet){
		return true;
	}

    @Override
	protected boolean checkrequire(POOPet pet){
		return true;
	}

    @Override
	public void act(POOPet pet){
		pet.setHP(pet.getHP()-1);
	}
}

class Move extends Skills{
    Move(){
        super("Move Right", "Move right and restore 1 MP");
    }
    Move(String str, String Des){
		super(str, Des);
	}
    
    @Override
    protected  boolean require(POOPet pet){
        return true;
    }
    
    @Override
    protected  boolean checkrequire(POOPet pet){
        return true;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}

class NULL_SKILL extends Skills{
    NULL_SKILL(){
        super("Empty", "Empty Skill");
    }
    NULL_SKILL(String str, String Des){
		super(str, Des);
	}
    
    @Override
    protected  boolean require(POOPet pet){
        return true;
    }
    
    @Override
    protected  boolean checkrequire(POOPet pet){
        return true;
    }
    
    @Override
    public void act(POOPet pet){
       ;
    }
}