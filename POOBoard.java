public class POOBoard{
	protected static final int MAXEVAL = 1024;
	protected String name;
	protected java.util.ArrayList<POOArticle> board;

	public POOBoard(String name){
		this.name = name;
		newList();
	}

	protected void newList(){
		System.out.printf("Board new List\n");
		board = new java.util.ArrayList<POOArticle>();
	}

	protected java.util.ArrayList getList(){
		return (java.util.ArrayList<POOArticle>)board;
	}

	//warning, doesn't check max value 1024
	public void add(POOArticle article){
		getList().add(article);
	}

	//warning, doesn't check max value 1024
	public void del(int pos){
		getList().remove(pos);
	}
	
	public void move(int src, int dest){
		POOArticle tmp = (POOArticle)getList().get(src);
		getList().set(src, getList().get(dest));
		getList().set(dest, tmp);
	}

	public int length(){
		return getList().size();
	}

	//warning, may not be right
	public void show(){
		System.out.printf("show.size = %d\n",getList().size());
		int size = length();
		for(int i=0; i<size; i++)
			System.out.printf("%s\n", ((POOArticle)getList().get(i)).get_name());
		System.out.printf("show.finish\n");
	}

	public String get_name(){
		return name;
	}
}