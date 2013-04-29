package ntu.csie.oop13spring;

public abstract class Skills extends POOSkill{
        private String name, description;

        public Skills(String str, String des){
                name = str;
                description = des;
        }

	public String getName(){
		return name;
	}

	protected abstract boolean require(POOPet pet);
	protected abstract boolean checkrequire(POOPet pet);
        @Override
	public abstract void act(POOPet pet);
}