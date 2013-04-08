import java.util.*;

public class POOBBS{
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		POOBoard board = new POOBoard("NTU");
		board.add(new POOArticle("ACP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
		board.add(new POOArticle("DIP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
		board.add(new POOArticle("OOP", "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));
		board.add(new POOArticle("SP" , "Wang", " template Writed by Wang\n do not worry\n this is a simple BBS demo program written in JAVA\n JAVA is a powerful programming languange\n"));

		POODirectory root = new POODirectory("Favorite");
		((POODirectory)root).add(board);
		((POODirectory)root).add(new POOBoard("CSIE"));
		((POODirectory)root).add(new POODirectory("SCHOOL"));


		int position = 0, state = 0;
		POOArticle current = root;
		while(true){
			if(current.getClass().getName().equals("POODirectory"))	state = 0;
			else if(current.getClass().getName().equals("POOBoard")) state = 1;
			else if(current.getClass().getName().equals("POOArticle")) state = 2;

			System.out.printf("Action: <g>left <h>right <j>up <k>down <a>add");
			if (state!=2) System.out.printf("<m>move <d>delete");
			System.out.printf("\n");
			
			int size = 0;
			System.out.printf("==========================%s==========================\n", current.get_name());
			
			if(current.getClass().getName().equals("POODirectory")){
				size = ((POODirectory)current).get_size();
				((POODirectory)current).show(position);
				state = 0;
			}
			else if(current.getClass().getName().equals("POOBoard")){
				size = ((POOBoard)current).get_size();
				((POOBoard)current).show(position);
				state = 1;
			}
			else if(current.getClass().getName().equals("POOArticle")){
				size = ((POOArticle)current).get_size();
				((POOArticle)current).show();
				state = 2;
			}


			String command = input.nextLine();
			if (command.equals("g")) {
				if (current != root)
					current = current.get_parent();
			}
			else if (command.equals("h")) {
				if(size != 0 && state==0 ){
					current = ((POODirectory)current).get(position);
					position = 0;
				}
				else if(size != 0 && state==1 ){
					current = ((POOBoard)current).get_article(position);
					position = 0;
				}
			}
			else if (command.equals("j")) position = position-1<0 ? (size-1>0 ? size-1:0): position-1;
			else if (command.equals("k")) position = position+1>=size ? 0: position+1;
			else if (command.equals("a")) {
				if (state==0){
					System.out.printf("<b> add board <d> add directory : ");
					String type  = input.nextLine();
					System.out.printf("Name: ");
					String name = input.nextLine();
					if (type.equals("b")) ((POODirectory)current).add(new POOBoard(name));
					else if (type.equals("d")) ((POODirectory)current).add(new POODirectory(name));
				}
				else if (state==1){
					System.out.printf("Article_title: ");
					String title = input.nextLine();
					System.out.printf("Article_Author: ");
					String author = input.nextLine();
					System.out.printf("Article_Content: ");
					String content = input.nextLine();
					((POOBoard)current).add(new POOArticle(title, author, content));
				}
				else if (state==2){
					System.out.printf("<b> Boo <p> Push <a> Arrow : ");
					String type  = input.nextLine();
					System.out.printf("Content: ");
					String name = input.nextLine();
					if (type.equals("b")) current.boo(name);
					else if(type.equals("p")) current.push(name);
					else if(type.equals("a")) current.arrow(name);
				}
			}
			else if (command.equals("m")) {
				if (state==0){
					System.out.printf("Enter the position you want to move: ");
					int p = Integer.parseInt(input.nextLine());
					((POODirectory)current).move(position, p);
				}
				else if (state==1){
					System.out.printf("Enter the position you want to move: ");
					int p = Integer.parseInt(input.nextLine());
					((POOBoard)current).move(position, p);	
				}
			}
		}
	}

}