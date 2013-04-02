import java.util.*;
// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;

// class CloseWindow extends WindowAdapter implements ActionListener {
//     private Window target;
//     private boolean exit;
//     public CloseWindow(Window target, boolean exit) {
//         this.target = target;
//         this.exit = exit;
//     }
//     public CloseWindow(Window target) {
//         this.target = target;
//     }
//     public void windowClosing(WindowEvent e) {
//         target.dispose();
//         if (exit) System.exit(0);
//     }
//     public void actionPerformed(ActionEvent e) {
//         target.dispose();
//         if (exit) System.exit(0);
//     }
// }

// class BBS_VIEW extends Component implements KeyListener {
//     public BBS_VIEW(JFrame p) {
//         p.addKeyListener(this);
//     }

//     // The following 5 functions implement the MouseListener interface
//     private int CursorY=5;
//     public void keyPressed(KeyEvent e) {
//     	int key = e.getKeyCode();
//         System.out.println(key);
//         if( key == KeyEvent.VK_UP ){
//         	CursorY = CursorY - 10 > 0 ? CursorY - 10 : CursorY;
//         }
//         else if( key == KeyEvent.VK_DOWN ){
//         	CursorY = CursorY + 10 < 200-5 ? CursorY + 10 : CursorY;
//         }
//         repaint();
//     }
//     public void keyReleased(KeyEvent e) {}
//     public void keyTyped(KeyEvent e) {}
  
//     public void paint(Graphics g) {
//     	((Graphics2D)g).setBackground(Color.black);
//     	g.setColor(Color.white);
//     	g.fillOval(5,CursorY,8,8);
//     }

//     public void Favorite() {
//         repaint();
//     }

//     public Dimension getPreferredSize() {
//         return new Dimension(300,200);
//     }
// }

public class POOBBS/* extends JFrame implements ActionListener */{
	// private BBS_VIEW Board;
	// private POOBBS(){
	// 	super("POOBBS");
 //        JMenu m = new JMenu("POOBBS");
 //        JMenuBar mb = new JMenuBar();

 //        setJMenuBar(mb);
 //        mb.add(m).add(new JMenuItem("Favorite")).addActionListener(this);

 //        Board = new BBS_VIEW(this);
 //        this.getContentPane().add(Board);
 //        CloseWindow close = new CloseWindow(this, true);
 //        m.add(new JMenuItem("Leave")).addActionListener(close);
 //        this.addWindowListener(close);
 //        pack();
 //        setVisible(true);
	// }

	// public void actionPerformed(ActionEvent e) {
 //        String command = e.getActionCommand();
 //        if (command.equals("Favorite")) {
 //        	Board.Favorite();
 //        } else if (command.equals("New Game")) {
 //        	System.out.printf("ASD");
 //        }
 //    }

	public static void main(String[] args) {
		// POOBBS s = new POOBBS();
		POOBoard board = new POOBoard("NTU");
		board.add(new POOArticle("english is good", "eee", "wang"));
		board.add(new POOArticle("english is best", "eee", "wang"));
		board.add(new POOArticle("english is well", "eee", "wang"));
		board.add(new POOArticle("english is worst", "eee", "wang"));

		board.show();

		board.del(2);

		board.show();

		
		// POODirectory root = new POODirectory("favorite");
		// // Class tmpp = root.getClass();
		// // tmpp.show();


		// System.out.printf("class=%s\n",root.getClass().getName());
		// POODirectory tmp = root;
		// while(true){
		// 	String classname = tmp.getClass().getName();

		// 	// if( classname.equals("POODirectory") ){
		// 	// 	System.out.printf("0: nothing, 1: add Board, 2: add Directory, 3: add devider, 4: select board/directory? ");
		// 	// 	String choice = input.nextLine();
		// 	// 	if( choice.equals("0") );
		// 	// 	else if( choice.equals("1") ){
		// 	// 		System.out.printf("Board name: ");
		// 	// 		tmp.add( new POOBoard(input.nextLine()));
		// 	// 	}
		// 	// 	else if( choice.equals("2") ){
		// 	// 		System.out.printf("Directory name: ");
		// 	// 		tmp.add( new POODirectory(input.nextLine()) );
		// 	// 	}
		// 	// 	else if( choice.equals("3") ){
		// 	// 		tmp.add_split();
		// 	// 	}
		// 	// 	else if( choice.equals("4") ){
		// 	// 		System.out.printf("Choose id: ");
		// 	// 		tmp = (POOBoard)tmp.get(Integer.parseInt(input.nextLine()));
		// 	// 	}
		// 	// }
		// 	// if( classname.equals("POOBoard") ){
		// 	// 	System.out.printf("Board!!!\n");
		// 	// }
		// 	System.out.printf("===[%s]===\n", tmp.get_name());
		// 	tmp.show();
		// }
	}
}