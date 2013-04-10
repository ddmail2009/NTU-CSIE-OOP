import java.util.ArrayList;


/** Class POOArticle **/
public class POOArticle implements java.io.Serializable {
	/** ID_count for different article ID **/
	private static int ID_count = 0;
	/** ID for identification **/
	private Integer ID;
	/** title, the name **/
	protected String title;
	/** author, the author name **/
	private String author;
	/** content, the article content **/
	private String content;
	/** evaluation_count, push_count-poo_count **/
	private Integer evaluation_count = 0;
	/** arr_list, list to store evaluation_messages **/
	private java.util.ArrayList<String> arr_list;
	/** Max evaluation limit **/
	protected static final int MAXEVAL = 1024;
	/** Indicate Whose the parent node **/
	protected POOBoard parent;

	/** Empty Constructor of POOArticle, initial ID=-1, title=author=content='null', parent=null **/
	protected POOArticle(){
		ID = -1;
		title = author = content = "null";
		parent = null;
		arr_list = new java.util.ArrayList<String>();
	}

	/** Default Constructor of POOArticle
		@param title title
		@param author author
		@param content content **/
	public POOArticle( String title, String author, String content ){
		ID = (ID_count++)%100;
		this.title = title;
		this.author = author;
		this.content = content;
		parent = null;
		arr_list = new java.util.ArrayList<String>();
	}
	/** Boo method, cause evaluation_count-1
		@param str evaluation message
		@return success or not **/
	public boolean boo(String str){
		evaluation_count--;
		return arrow(str);
	}
	/** Push method, cause evaluation_count+1
		@param str evaluation message
		@return success or not **/
	public boolean push(String str){
		evaluation_count++;
		return arrow(str);
	}
	/** Arrow method, no change to evaluation_count
		@param str evaluation message
		@return success or not **/
	public boolean arrow(String str){
		if (arr_list.size() == MAXEVAL )return false;
		arr_list.add(str);
		return true;
	}
	/** show method, Show Entire article and evaluation messages **/
	public void show(){
		list();
		System.out.printf("\n%s\n", content);
		for (int i=0; i<length(); i++)
			System.out.printf("->%s\n", arr_list.get(i));
	}
	/** list method, Show only evaluation_count, ID, title, and author **/
	public void list(){
		System.out.printf("[%+5d]\tID:%2d\ttitle:%20s\tauthor:%20s", evaluation_count, ID, title, author);
	}
	/** Get the parent Node **/
	public POOBoard get_parent(){
		return parent;
	}
	/** Get the title parameter **/
	public String get_name(){
		return title;
	}
	/** Show the number of evaluation messages **/
	public int length(){
		return arr_list.size();
	}
}