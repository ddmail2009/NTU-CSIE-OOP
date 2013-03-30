public class POOArticle {
	private static int ID_count = 0;
	private Integer ID;
	private String title;
	private String author;
	private String content;
	private Integer evaluation_count = 0;
	private java.util.ArrayList<String> evaluation_messages;
	private static final int MAXEVAL = 1024;

	POOArticle( String title, String author, String content ){
		ID = (ID_count++)%100;
		this.title = title;
		this.author = author;
		this.content = content;

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
		System.out.printf("%s\n", content);
		for (int i=0; i<evaluation_messages.size(); i++)
			System.out.printf("%s\n", evaluation_messages.get(i));
	}

	public void list(){
		System.out.printf("[%+d] ID:%d, title:%s, author:%s\n", evaluation_count, ID, title, author);
	}

	public String get_name(){
		return title;
	}
}