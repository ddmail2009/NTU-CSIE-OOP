package ntu.csie.oop13spring;

import java.util.*;

public class POOPetKind1 extends POOPet{
	private ArrayList<Skills> petskill = new ArrayList<>();

	public POOPetKind1(){
		setHP(10);
		setMP(10);
		setAGI(10);
		setName("Kerrigan");

		petskill.add(new Attack());
		petskill.add(new ArcaneStorm());
	}

        @Override
	protected POOAction act(POOArena arena){
		Scanner in = new Scanner(System.in);
		System.out.printf("Pet[%s] HP:%d MP:%d AGI:%d\n", getName(), getHP(), getMP(), getAGI());

		POOPet[] allpet = arena.getAllPets();
		System.out.printf("%s Use ", getName());
		for (int i=0; i<petskill.size(); i++){
			if(petskill.get(i).checkrequire(this))
				System.out.printf("[%d]%s ", i, petskill.get(i).getName());
		}
		System.out.printf(": ");
		int skill = in.nextInt();

		System.out.printf("To Target ");
		for (int i=0; i<allpet.length; i++)
			System.out.printf("[%d]%s ", i, allpet[i].getName());
		System.out.printf(": ");
		int target = in.nextInt();

		
		POOAction e = new POOAction();
		e.skill = petskill.get(skill);
		e.dest = allpet[target];

		System.out.printf("%s use %s to %s\n", getName(), petskill.get(skill).getName(), e.dest.getName() );
		return e;
	}

        @Override
	protected POOCoordinate move(POOArena arena){
		return new POOCoordinateKind(1,2);
	}
}

class ArcaneStorm extends Skills{
	ArcaneStorm(){
		super("ArcaneStorm", "Cost 25 MP and Damage Foe 25 HP");
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