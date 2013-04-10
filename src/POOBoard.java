import java.util.ArrayList;

/** Class POOBoard extends POOArticle **/
public class POOBoard extends POOArticle{
	protected ArrayList<POOArticle> arr_list = new ArrayList<POOArticle>();

	/** Constructor of POOBoard
		@param title the title of board **/
	public POOBoard(String title){
		super();
		parent = null;
		this.title = title;
	}

	/** Add object to list
		@param article object to show in list 
		@return success or not **/
	public boolean add(POOArticle article){
		if( arr_list.size() >= MAXEVAL ) return false;
		arr_list.add(article);
		article.parent = this;
		return true;
	}
	/** Del object in list
		@param pos the index of the object
		@return success or not **/
	public boolean del(int pos){
		if( pos >= MAXEVAL ) return false;
		arr_list.remove(pos);
		return true;
	}
	/** Move object from src to dest
		@param src source index
		@param dest destination index **/
	public void move(int src, int dest){
		src = src<0 ? 0 : (src>=length() ? length() : src);
		dest = dest<0 ? 0 : (dest>=length() ? length(): dest);
		POOArticle tmp = arr_list.get(src);
		arr_list.set(src, arr_list.get(dest));
		arr_list.set(dest, tmp);
	}
	/** Show the Entire Board without point **/
	public void show(){
		int count = 0;
		for (POOArticle i : arr_list) {
			count++;
			System.out.printf("[%d]", count);
			i.list();
		}
	}
	/** Show the Entire Board with point
		@param position the index of the point position **/
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
	/** The length of Object list
		@return the length of Object list **/
	public int length(){
		return arr_list.size();
	}
	/** Get the Object in index i
		@param i index of the Object
		@return The Object in index i **/
	public POOArticle get(int i){
		if(i<length()) return arr_list.get(i);
		else return this;
	}
}