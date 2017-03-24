import java.util.ArrayList;

/** Class POODirectory extends POOBoard **/
public class POODirectory extends POOBoard{
	/** Constructor of POODirectory
		@param title the title of directory **/
	public POODirectory(String title){
		super(title);
	}
	/** Add Split line in list 
		@return success or not **/
	public boolean add_split(){
		if( arr_list.size() >= MAXEVAL ) return false;
		POOSplit tmp = new POOSplit();
		tmp.parent = this;
		arr_list.add(tmp);
		return true;
	}
	/** Show the Entire List without point **/
	public void show(){
		int size = length();
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
	/** Show the Entire List with point 
		@param position the point's index **/
	public void show(int position){
		int size = length();
		for(int i=0; i<size; i++){
			if(i==position) System.out.printf("*  ");
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