import java.util.ArrayList;

public class POODirectory extends POOBoard{
	public POODirectory(String name){
		super(name);
	}

	public boolean add_split(){
		if( arr_list.size() >= MAXEVAL ) return false;
		POOSplit tmp = new POOSplit();
		tmp.parent = this;
		arr_list.add(tmp);
		return true;
	}

	public void show(){
		int size = get_size();
		for(int i=0; i<size; i++){
			POOArticle tmp = arr_list.get(i);
			Class cls = tmp.getClass();

			if( cls.getName().equals("POOBoard") )System.out.printf("[b]");
			else if( cls.getName().equals("POODirectory") )System.out.printf("[d]");
			tmp.list();
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}

	public void show(int current){
		int size = get_size();
		for(int i=0; i<size; i++){
			if(i==current) System.out.printf("*  ");
			else System.out.printf("   ");

			POOArticle tmp = arr_list.get(i);
			Class cls = tmp.getClass();

			if( cls.getName().equals("POOBoard") )System.out.printf("%d [b]", i);
			else if( cls.getName().equals("POODirectory") )System.out.printf("%d [d]", i);

			System.out.printf("%s\n", tmp.get_name());
		}
		System.out.printf("\n");
	}
}

class POOSplit extends POOBoard{
	public POOSplit(){
		super("==================");	
	}
}