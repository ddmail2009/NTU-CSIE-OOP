package ntu.csie.oop13spring;
import java.util.*;

public class POOArenaKind extends POOArena{
	private HashMap<POOPet, POOCoordinate> petscoor = new HashMap<>(0);
	private boolean first_run = true;
	private int alive = 0;
	private int x_limit, y_limit;

	public POOArenaKind(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
		x_limit = 3;
		y_limit = 3;
	}

	private void init(){
		POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++)
			petscoor.put(parr[i], new POOCoordinateKind(i/3, i%3));
		for (int i=0; i<parr.length; i++)
			if(parr[i].getHP() > 0)alive ++;
                show();
		first_run = false;
	}

        @Override
	public boolean fight(){
		if (first_run == true)init();

		for (POOPet e: getAllPets()){
			if(e.getHP()>0){
				POOAction act = e.act(this);
				act.skill.act(act.dest);
				if(e.getHP()>0 && act.dest.getHP()<=0) {
					System.out.printf("%s killed %s\n", e.getName(), act.dest.getName());
					alive--;
				}
			}
		}
		System.out.printf("alive=%d\n",alive);
		return alive>0;
	}

        @Override
	public void show(){
		POOPet[] parr = getAllPets();
		for(int i=0; i<x_limit; i++){
			for(int j=0; j<y_limit; j++){
				int t = searchPosition(new POOCoordinateKind(i, j));
				if (t >= 0)System.out.printf("|[%2d]\t%8s\tHP:%2d\t", t+1, parr[t].getName(), parr[t].getHP());
				else System.out.printf("|%4s\t%8s\t%5s\t", "", "", "");
			}
			System.out.printf("\n");
		}
	}

	public int searchPosition(POOCoordinate t){
		POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++)
			if (t.equals(getPosition(parr[i]))) return i;
		return -1;
	}

        @Override
	public POOCoordinate getPosition(POOPet p){
		return petscoor.get(p);
	}
}