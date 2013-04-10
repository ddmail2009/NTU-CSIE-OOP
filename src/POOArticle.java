import java.util.ArrayList;

public class POOArticle implements java.io.Serializable {
	private static int ID_count = 0;
	private Integer ID;
	protected String title;
	private String author;
	private String content;
	private Integer evaluation_count = 0;
	private java.util.ArrayList<String> arr_list;

	protected static final int MAXEVAL = 1024;

	protected POOBoard parent;
	POOArticle(){
		ID = -1;
		title = author = content = "null";
		parent = null;
		arr_list = new java.util.ArrayList<String>();
	}

	POOArticle( String title, String author, String content ){
		ID = (ID_count++)%100;
		this.title = title;
		this.author = author;
		this.content = content;
		parent = null;
		arr_list = new java.util.ArrayList<String>();
	}

	public boolean boo(String str){
		evaluation_count--;
		return arrow(str);
	}

	public boolean push(String str){
		evaluation_count++;
		return arrow(str);
	}

	public boolean arrow(String str){
		if (arr_list.size() == MAXEVAL )return false;
		arr_list.add(str);
		return true;
	}

	public void show(){
		list();
		System.out.printf("\n%s\n", content);
		for (int i=0; i<length(); i++)
			System.out.printf("->%s\n", arr_list.get(i));
	}

	public void list(){
		System.out.printf("[%+5d]\tID:%2d\ttitle:%20s\tauthor:%20s", evaluation_count, ID, title, author);
	}

	public POOBoard get_parent(){
		return parent;
	}

	public String get_name(){
		return title;
	}

	public int length(){
		return arr_list.size();
	}
}