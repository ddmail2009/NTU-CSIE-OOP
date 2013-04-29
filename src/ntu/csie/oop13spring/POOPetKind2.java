package ntu.csie.oop13spring;


public class POOPetKind2 extends POOPet{
	public POOPetKind2(){
		setHP(30);
		setMP(30);
		setAGI(30);
		setName("WangLi");
	}

        @Override
	protected POOAction act(POOArena arena){
		POOPet[] allpet = arena.getAllPets();
		POOAction e = new POOAction();
		e.skill = new POOTinyAttackSkill();
		e.dest = allpet[0];

		System.out.printf("%s use %s to %s\n", getName(), "tinyAttackSkill", e.dest.getName() );
		return e;
	}

        @Override
	protected POOCoordinate move(POOArena arena){
		return new POOCoordinateKind(1,2);
	}
}