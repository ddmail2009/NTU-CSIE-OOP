public class POOArticle implements java.io.Serializable {
	private static int ID_count = 0;
	private Integer ID;
	private String title;
	private String author;
	private String content;
	private Integer evaluation_count = 0;
	private java.util.ArrayList<String> evaluation_messages;
	private static final int MAXEVAL = 1024;

	protected POOBoard parent;
	POOArticle(){
		ID = -1;
		title = author = content = "null";
		parent = null;
		evaluation_messages = new java.util.ArrayList<String>();
	}

	POOArticle( String title, String author, String content ){
		ID = (ID_count++)%100;
		this.title = title;
		this.author = author;
		this.content = content;
		parent = null;
		evaluation_messages = new java.util.ArrayList<String>();
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
		if (evaluation_messages.size() == MAXEVAL )return false;
		evaluation_messages.add(str);
		return true;
	}

	public void show(){
		list();
		System.out.printf("\n%s\n", content);
		for (int i=0; i<evaluation_messages.size(); i++)
			System.out.printf("->%s\n", evaluation_messages.get(i));
	}

	public void list(){
		System.out.printf("[%+d]\tID:%2d\ttitle:%20s\tauthor:%20s", evaluation_count, ID, title, author);
	}

	public POOBoard get_parent(){
		return parent;
	}

	public String get_name(){
		return title;
	}

	public int get_size(){
		return 0;
	}
}