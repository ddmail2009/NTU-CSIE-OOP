public class POODirectory extends POOBoard{
	private java.util.ArrayList<POOBoard> list;

	public POODirectory(String name){
		super(name);
	}

	protected void newList(){
		// System.out.printf("In new List\n");
		list = new java.util.ArrayList<POOBoard>();
	}

	protected java.util.ArrayList getList(){
		// System.out.printf("In get List\n");
		return (java.util.ArrayList<POOBoard>)list;
	}

	//warning, doesn't check max value 1024
	public void add(POOBoard board){
		getList().add(board);
		System.out.printf("list.size=%d\n", getList().size());
	}
	//warning, doesn't check max value 1024
	public void add(POODirectory dir){
		getList().add(dir);
	}
	//warning, doesn't check max value 1024
	public void add_split(){
		POOSplit tmp = new POOSplit();
		getList().add(tmp);
	}

	public void move(int src, int dest){
		POOBoard tmp = (POOBoard)getList().get(src);
		getList().set(src, getList().get(dest));
		getList().set(dest, tmp);
	}

	public void show(){
		System.out.printf("show.size = %d\n===================\n",getList().size());
		int size = length();
		for(int i=0; i<size; i++)
			System.out.printf("%s\n", ((POOBoard)getList().get(i)).get_name());
		System.out.printf("===================\nshow.finish\n");
	}

}

class POOSplit extends POOBoard{
	public POOSplit(){
		super("==========");	
	}
}