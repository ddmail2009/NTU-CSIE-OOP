import java.util.ArrayList;

public class POODirectory extends POOBoard{
	private ArrayList<POOBoard> board_list = new ArrayList<POOBoard>();

	public POODirectory(String name){
		super(name);
	}

	//warning, doesn't check max value 1024
	public void add(POOBoard board){
		board.parent = this;
		board_list.add(board);
	}

	//warning, doesn't check max value 1024
	public void add_split(){
		POOSplit tmp = new POOSplit();
		board_list.add(tmp);
	}

	public void board_move(int src, int dest){
		POOBoard tmp = board_list.get(src);
		board_list.set(src, board_list.get(dest));
		board_list.set(dest, tmp);
	}

	public int get_size(){
		return board_list.size();
	}

	public void show(){
		int size = get_size();
		for(int i=0; i<size; i++){
			POOBoard tmp = board_list.get(i);
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

			POOBoard tmp = board_list.get(i);
			Class cls = tmp.getClass();

			if( cls.getName().equals("POOBoard") )System.out.printf("[b]");
			else if( cls.getName().equals("POODirectory") )System.out.printf("[d]");
			tmp.list();
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}


	public POOBoard get( int i ){
		if( board_list.get(i).getClass().getName() == "POODirectory" ){
			return (POODirectory)board_list.get(i);	
		}
		else return (POOBoard)board_list.get(i);
	}

	public void list(){
		System.out.printf("%s", name);
	}

	public POOBoard get_parent(){
		return parent;
	}
}

class POOSplit extends POOBoard{
	public POOSplit(){
		super("==========");	
	}
}