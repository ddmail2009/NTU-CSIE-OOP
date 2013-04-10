import java.util.ArrayList;

public class POOBoard extends POOArticle{
	protected ArrayList<POOArticle> arr_list = new ArrayList<POOArticle>();

	public POOBoard(String title){
		super();
		parent = null;
		this.title = title;
	}

	public boolean add(POOArticle article){
		if( arr_list.size() >= MAXEVAL ) return false;
		arr_list.add(article);
		article.parent = this;
		return true;
	}

	public boolean del(int pos){
		if( pos >= MAXEVAL ) return false;
		arr_list.remove(pos);
		return true;
	}
	
	public void move(int src, int dest){
		src = src<0 ? 0 : (src>=get_size() ? get_size() : src);
		dest = dest<0 ? 0 : (dest>=get_size() ? get_size(): dest);
		POOArticle tmp = arr_list.get(src);
		arr_list.set(src, arr_list.get(dest));
		arr_list.set(dest, tmp);
	}

	public void show(){
		int count = 0;
		for (POOArticle i : arr_list) {
			count++;
			System.out.printf("[%d]", count);
			i.list();
		}
	}
	
	public void show(int position){
		int count = 0;
		for (POOArticle i : arr_list) {
			if(count==position) System.out.printf("*  ");
			else System.out.printf("   ");
			i.list();
			System.out.printf("\n");
			count++;
		}
	}

	public int get_size(){
		return arr_list.size();
	}

	public POOArticle get(int i){
		if(i<get_size()) return arr_list.get(i);
		else return this;
	}

}