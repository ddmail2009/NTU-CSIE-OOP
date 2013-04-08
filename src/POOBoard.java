import java.util.ArrayList;

public class POOBoard extends POOArticle{
	protected static final int MAXEVAL = 1024;
	protected String name;
	private ArrayList<POOArticle> article_list = new ArrayList<POOArticle>();

	public POOBoard(String name){
		super();
		parent = null;
		this.name = name;
	}

	public boolean add(POOArticle article){
		if( article_list.size() >= MAXEVAL ) return false;
		article_list.add(article);
		article.parent = this;
		return true;
	}

	public boolean del(int pos){
		if( pos >= MAXEVAL ) return false;
		article_list.remove(pos);
		return true;
	}
	
	public void move(int src, int dest){
		POOArticle tmp = article_list.get(src);
		article_list.set(src, article_list.get(dest));
		article_list.set(dest, tmp);
	}

	public void show(){
		int count = 0;
		for (POOArticle i : article_list) {
			count++;
			System.out.printf("[%d]", count);
			i.list();
		}
	}
	
	public void show(int position){
		int count = 0;
		for (POOArticle i : article_list) {
			if(count==position) System.out.printf("*  ");
			else System.out.printf("   ");
			i.list();
			System.out.printf("\n");
			count++;
		}
	}

	public String get_name(){
		return name;
	}

	public int get_size(){
		return article_list.size();
	}

	public POOArticle get(int i){
		return article_list.get(i);
	}

}