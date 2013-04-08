import java.util.*;

public class POOBBS{
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		POOBoard board = new POOBoard("NTU");
		board.add(new POOArticle("english is good", "eee", "wang"));
		board.add(new POOArticle("english is best", "eee", "wang"));
		board.add(new POOArticle("english is well", "eee", "wang"));
		board.add(new POOArticle("english is worst", "eee", "wang"));

		POODirectory root = new POODirectory("Favorite");
		((POODirectory)root).add(board);
		((POODirectory)root).add(new POOBoard("CSIE"));
		((POODirectory)root).add(new POODirectory("SCHOOL"));
		// ((POODirectory)root).show();

		int position = 0, state = 0;
		POOArticle current = root;
		while(true){
			System.out.printf("Action: <g>left <h>right <j>up <k>down <a>add <m>move\n");
			
			int size = 0;
			System.out.printf("=================");
			current.list();
			System.out.printf("=================\n");
			
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


			String command = input.next();
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
					String type  = input.next();
					System.out.printf("Name: ");
					String name = input.next();
					if (type.equals("b")) ((POODirectory)current).add(new POOBoard(name));
					else if (type.equals("d")) ((POODirectory)current).add(new POODirectory(name));
				}
				else if (state==1){
					;
				}
			}
		}
	}

}