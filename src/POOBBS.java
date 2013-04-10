import java.util.*;

public class POOBBS{
	Scanner input;
	private POOArticle root, current;
	private int state;
	private int position = 0, length = 0;
	private static final int Directory = 0;
	private static final int Board = 1;
	private static final int Article = 2;
	private static final int Split = 3;

	private static final int Up = 0;
	private static final int Down = 1;
	private static final int Left = 2;
	private static final int Right = 3;

	public POOBBS(String str){
		input = new Scanner(System.in);
		root = new POODirectory(str);
		try{
			LoadBBS("BBS.save");
		}catch(Exception e){
			System.out.println(e);
			System.out.printf("Using Default Setting\n");
			POODirectory dir = new POODirectory("SCHOOL");
			dir.add(new POOBoard("Berkely"));
			POOBoard board = new POOBoard("CSIE");
			board.add(new POOArticle("ACP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
			board.add(new POOArticle("DIP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
			board.add(new POOArticle("OOP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
			board.add(new POOArticle("SP" , "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
			((POODirectory)root).add(board);
			((POODirectory)root).add(new POOBoard("EE"));
			((POODirectory)root).add(new POOBoard("NTU"));
			((POODirectory)root).add_split();
			((POODirectory)root).add(dir);
		}

		current = root;
		state = Directory;
	}

	protected void SaveBBS(String file) throws Exception{
		java.io.ObjectOutputStream fileoutput = new java.io.ObjectOutputStream(new java.io.FileOutputStream("BBS.save"));
		fileoutput.writeObject(root);
		fileoutput.close();
	}

	protected void LoadBBS(String file) throws Exception{
		java.io.ObjectInputStream fileinput = new java.io.ObjectInputStream(new java.io.FileInputStream(file));
		root = (POOArticle)fileinput.readObject();
		fileinput.close();
	}

	public void AnalyzeState(){
		if(current.getClass().getName().equals("POODirectory")){
			state = Directory;
			length = ((POODirectory)current).get_size();
		}
		else if(current.getClass().getName().equals("POOBoard")){
			state = Board;
			length = ((POOBoard)current).get_size();
		}
		else if(current.getClass().getName().equals("POOArticle")){
			state = Article;
			length = ((POOArticle)current).get_size();
		}
		else if(current.getClass().getName().equals("POOSplit")){
			state = Split;
			length = 0;
		}
	}

	public void PrintValidCommand(){
		System.out.printf("\nAction: <a>left <d>right <w>up <s>down ");
		if (state!=Split){
			System.out.printf("<i>insert ");
			if (state!=Article) System.out.printf("<m>move <k>delete ");
		}
		System.out.printf("<l>leave\n");
	}

	public void PrintCurrent(){
		System.out.printf("==================================%s==================================\n", current.get_name());
		if(state==Directory) ((POODirectory)current).show(position);
		else if(state==Board) ((POOBoard)current).show(position);
		else if(state==Article) ((POOArticle)current).show();
		System.out.printf("\n");
	}

	public void ParseCommand(String command){
		command = command.toLowerCase();
		if(command.equals("a"))ArrowKey(Left);
		else if(command.equals("d"))ArrowKey(Right);
		else if(command.equals("w"))ArrowKey(Up);
		else if(command.equals("s"))ArrowKey(Down);
		else if(command.equals("i"))AddCommand();
		else if(command.equals("l"))LeaveCommand();
		else if(state!=Article && state!=Split){
			if(command.equals("k"))DeleteCommand();
			else if(command.equals("m"))MoveCommand();
			else{
				try{
					position = Integer.parseInt(command);
					position = position<0 ? 0 : (position>=length ? length-1 : position);
				}catch(Exception e){
					;
				}
			}
		}
	}

	public void ArrowKey(int arrow){
		if(arrow==Up) position = position-1<0 ? (length-1>0 ? length-1:0): position-1;
		else if(arrow==Down) position = position+1>=length ? 0: position+1;
		else if(arrow==Left){
			if(current == root)LeaveCommand();
			else current = current.get_parent();
		}
		else if(arrow==Right){
			POOArticle tmp = ((POOBoard)current).get(position);
			if( !tmp.getClass().getName().equals("POOSplit") ){
				current = tmp;
				position = 0;
			}
		} 
	}

	public void DeleteCommand(){
		System.out.printf("Deleting position %d, Are You Sure? (y/N) ", position);
		String command = input.nextLine();
		if(!command.equals("N")){
			((POOBoard)current).del(position);
			position = position<current.get_size() ? position : current.get_size()-1;
		}
	}

	public void MoveCommand(){
		System.out.printf("Enter the position you want to move: ");
		int p = Integer.parseInt(input.nextLine());
		if(state==Directory) ((POODirectory)current).move(position, p);
		else if(state==Board) ((POOBoard)current).move(position, p);	
	}

	public void AddCommand(){
		if (state==Directory){
			System.out.printf("<b> add board <d> add directory <s> add split : ");
			String type  = input.nextLine();
			if( !type.equals("s")){
				System.out.printf("Name: ");
				String name = input.nextLine();
				if (type.equals("b")) ((POODirectory)current).add(new POOBoard(name));
				else if (type.equals("d")) ((POODirectory)current).add(new POODirectory(name));
			}
			else ((POODirectory)current).add_split();
		}
		else if (state==Board){
			System.out.printf("Article_title: ");
			String title = input.nextLine();
			System.out.printf("Article_Author: ");
			String author = input.nextLine();
			System.out.printf("Article_Content: ");
			String content = input.nextLine();
			((POOBoard)current).add(new POOArticle(title, author, content));
		}
		else if (state==Article){
			System.out.printf("<b> Boo <p> Push <a> Arrow : ");
			String type  = input.nextLine();
			System.out.printf("Content: ");
			String name = input.nextLine();
			if (type.equals("b")) current.boo(name);
			else if(type.equals("p")) current.push(name);
			else if(type.equals("a")) current.arrow(name);
		}
	}

	public void LeaveCommand(){
		System.out.printf("Leave And Save now? (y/N) ");
		if( input.nextLine().equals("y") ){
			try{
				SaveBBS("BBS.save");
				System.exit(1);
			}catch(Exception e){
				System.out.printf("Error Saving BBS, exit without Saving? (y/N) ");
				if (input.nextLine().equals("y"))System.exit(1);
			}
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		POOBBS BBS = new POOBBS("Favorite");
		
		int position = 0;
		while(true){
			BBS.AnalyzeState();
			BBS.PrintValidCommand();
			BBS.PrintCurrent();

			String command = input.nextLine();
			BBS.ParseCommand(command);
		}
	}

}