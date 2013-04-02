import java.util.ArrayList;

public class POOBoard{
	protected static final int MAXEVAL = 1024;
	protected String name;
	private ArrayList<POOArticle> board = new ArrayList<POOArticle>();

	public POOBoard(String name){
		this.name = name;
	}

	protected ArrayList getList(){
		return (ArrayList<POOArticle>)board;
	}

	public boolean add(POOArticle article){
		if( getList().size() >= MAXEVAL ) return false;
		getList().add(article);
		return true;
	}

	public boolean del(int pos){
		if( pos >= MAXEVAL ) return false;
		getList().remove(pos);
		return true;
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
		int count = 0;
		for (POOArticle i : (ArrayList<POOArticle>)getList()) {
			count++;
			System.out.printf("[%d] %s\n", count ,i.get_name());
		}
		// int size = length();
		// for(int i=0; i<size; i++)
			
	}

	public String get_name(){
		return name;
	}
}