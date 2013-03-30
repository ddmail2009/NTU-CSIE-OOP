import java.util.*;

public class POOBBS{
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		POODirectory root = new POODirectory("favorite");
		// Class tmpp = root.getClass();
		// tmpp.show();


		System.out.printf("class=%s\n",root.getClass().getName());
		POODirectory tmp = root;
		while(true){
			String classname = tmp.getClass().getName();

			if( classname.equals("POODirectory") ){
				System.out.printf("0: nothing, 1: add Board, 2: add Directory, 3: add devider, 4: select board/directory? ");
				String choice = input.nextLine();
				if( choice.equals("0") );
				else if( choice.equals("1") ){
					System.out.printf("Board name: ");
					tmp.add( new POOBoard(input.nextLine()));
				}
				else if( choice.equals("2") ){
					System.out.printf("Directory name: ");
					tmp.add( new POODirectory(input.nextLine()) );
				}
				else if( choice.equals("3") ){
					tmp.add_split();
				}
				else if( choice.equals("4") ){
					System.out.printf("Choose id: ");
					tmp = (POOBoard)tmp.get(Integer.parseInt(input.nextLine()));
				}
			}
			if( classname.equals("POOBoard") ){
				System.out.printf("Board!!!\n");
			}
			System.out.printf("===[%s]===\n", tmp.get_name());
			tmp.show();
		}
	}
}