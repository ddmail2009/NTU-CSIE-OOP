package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;



public class Arena extends POOArena implements KeyListener{
    private Window win;
    private HashMap<POOPet, MyComp> petlabel = new HashMap<>();
    private HashMap<POOPet, ArrayList<Integer>> petkey = new HashMap<>();
    private HashMap<POOPet, ArrayList<ArrayList<MyComp>>> petDescription = new HashMap<>();
    private HashMap<POOPet, ArrayList> petStat = new HashMap<>();
	private boolean first_run = true;
	private int alive = 0;

    
    private java.util.Timer CLI_Timer = new java.util.Timer();
    private java.util.Timer Win_Timer = new java.util.Timer();

	public Arena(){
		System.out.printf("Arena Established\nWritten by B99902006 Po-Hsien\n\n");
	}
	private void init(){
        win = new Window("POOArena", this, getAllPets());
		
        final POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++){
            petlabel.put(parr[i], new MyComp(win.panel, new JLabel(new ImageIcon(win.getImage(parr[i]))), 200 , i%2==0 ? (i+1)*100 : 800-(i+1)*100));
            win.addComp(petlabel.get(parr[i]));
            petlabel.get(parr[i]).setLimit(new Rectangle(0, 0, 800, 400));
            
            
            ArrayList<ArrayList<MyComp>> petdes = new ArrayList<>();
            Skills[] petskills = ((Pet_Object)parr[i]).getSkillList();
            for(int j=0; j<petskills.length; j++){
                ArrayList<MyComp> list = new ArrayList<>();
                list.add(new MyComp(win.panel, new JLabel(petskills[j].getName()),      460+j*20, i%2==0 ? 20+i/2*100 : 420+i/2*100));
                list.add(new MyComp(win.panel, new JLabel(petskills[j].getDesString()), 460+j*20, i%2==0 ? 170+i/2*100 : 570+i/2*100));
                win.addComp(list.get(0));
                win.addComp(list.get(1));
                petdes.add(list);
            }
            petDescription.put(parr[i], petdes);
            
            ArrayList<ArrayList<MyComp>> petstat = new ArrayList<>();
            ArrayList<MyComp> list = new ArrayList<>();
            list.add(new MyComp(win.panel, new JLabel("HP"), 420, i%2==0 ? 20 : 420, 350, 13));
            list.get(0).getComp().setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            ((JLabel)list.get(0).getComp()).setHorizontalAlignment(SwingConstants.CENTER);
            ((JLabel)list.get(0).getComp()).setBackground(Color.red);
            ((JLabel)list.get(0).getComp()).setOpaque(true);
            list.add(new MyComp(win.panel, new JLabel("HP"), 440, i%2==0 ? 20 : 420, 350, 13));
            list.get(1).getComp().setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            ((JLabel)list.get(1).getComp()).setHorizontalAlignment(SwingConstants.CENTER);
            ((JLabel)list.get(1).getComp()).setBackground(Color.cyan);
            ((JLabel)list.get(1).getComp()).setOpaque(true);
            win.addComp(list.get(0));
            win.addComp(list.get(1));
            petStat.put(parr[i], list);
            
            petkey.put(parr[i], new ArrayList<Integer>(0));
        }
		for (int i=0; i<parr.length; i++)
            if(parr[i].getHP() > 0)alive ++;
        
        show();
		first_run = false;
        
        CLI_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (POOPet pet : getAllPets())
                    System.out.println(pet);
            }
        }, 0, 1000);
	}

    @Override
	public boolean fight(){
		if (first_run == true)init();

		for (POOPet e: getAllPets()){
			if(e.getHP()>0){
				POOAction act = e.act(this);
				act.skill.act(act.dest);
				if(e.getHP()>0 && act.dest!=null && act.dest.getHP()<=0) {
					System.out.printf("%s killed %s\n", e.getName(), act.dest.getName());
					alive--;
				}
			}
		}

        if(alive == 1){
            win.dispose();
            return false;
        }
        else return true;
	}
    @Override
	public void show(){
        for (POOPet pet : getAllPets()) {
            Skills[] petskill = ((Pet_Object)pet).getSkillList();
            ArrayList<ArrayList<MyComp>> des = petDescription.get(pet);
            
            for(int i=0; i<des.size(); i++){
                ((JLabel)des.get(i).get(0).getComp()).setText(petskill[i].getName());
                ((JLabel)des.get(i).get(1).getComp()).setText(petskill[i].getDesString());
            }
        }
        win.draw();
	}
    @Override
	public POOCoordinate getPosition(POOPet p){
		return petlabel.get(p).getCoor();
	}
    
    protected void setPosition(POOPet p, POOCoordinate cor){
        petlabel.get(p).setCoor(cor);
    }
    protected ArrayList<Integer> petGetKey(POOPet pet){
        return petkey.get(pet);
    }
    public int searchPosition(POOCoordinate t){
		POOPet[] parr = getAllPets();
		for (int i=0; i<parr.length; i++)
			if (t.equals(getPosition(parr[i]))) return i;
		return -1;
	}   
    
    @Override
    public void keyTyped(KeyEvent e) {
        try{
//            throw new UnsupportedOperationException("Not supported yet.");
        }catch(Exception err){
            System.out.println(err);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (POOPet pet : getAllPets()) {
            petkey.get(pet).add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (POOPet pet : getAllPets()) {
            petkey.get(pet).add(-e.getKeyCode());
        }
    }
}


class Window extends JFrame{
    private Arena arena;
    private ArrayList<MyComp> element = new ArrayList<>();
    private Container container;
    private BufferedImage panel_background;
    private HashMap<POOPet, BufferedImage[]> pet_image = new HashMap<>();
    
    public JPanel panel;
    private String Directory = "C:\\Users\\Dan\\Dropbox\\Dan\\CSIE\\101-Object_Oriented_Programming\\OOP-hw4\\";
    public Window(String name, Arena arena, POOPet[] pets){
        readImage(pets);
        
        this.arena = arena;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        addKeyListener(arena);

        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                ((Graphics2D)g).setPaint(new TexturePaint(panel_background, new Rectangle(0,0,300,300)));
                ((Graphics2D)g).fill(new Rectangle(0,0,800,600));
            }
        };
        add(panel);
        
        MyComp separator1 = new MyComp(panel, new JSeparator(), 400, 0, 800, 2);
        separator1.getComp().setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        
        MyComp separator2 = new MyComp(panel, new JSeparator(), 400, 400, 2, 200);
        separator2.getComp().setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        addComp(separator1);
        addComp(separator2);
        
        Insets insets = getInsets();
        setSize(800+insets.left, 600+insets.top+insets.bottom);
        setVisible(true);
    }
    
    private void readImage(POOPet[] pOOPets){
        Pet_Object []pets = new Pet_Object[pOOPets.length];
        for (int i = 0; i < pets.length; i++) pets[i] = (Pet_Object)pOOPets[i];
        panel_background = Image.ReadImage(Directory+"test.jpg");
        for (int i = 0; i < pets.length; i++) {
            BufferedImage []images = new BufferedImage[4];
            String[] image_paths = pets[i].GetAllPath();
            for (int j = 0; j < 4; j++) {
                images[j] = Image.ReadImage(Directory+image_paths[i]);
            }
            pet_image.put(pets[i], images);
        }
    }
    protected void addComp(MyComp comp){
        element.add(comp);
    }
    public void draw(){
        for (MyComp myComp : element) {
            myComp.draw();
        }
    }

    BufferedImage getImage(POOPet pooPet) {
        Pet_Object pet = (Pet_Object)pooPet;
        return pet_image.get(pet)[pet.GetStatus()];
    }
}

class Image{
    static public BufferedImage ReadImage(String path){
        try{
            return ImageIO.read(new File(path));
            
        }catch(Exception e){
            System.out.println("Image "+path+" Can not be found!!");
            e.printStackTrace();
            return null;
        }
    }
}

class MyComp{
    private int ratio = 2000;
    private Container contain;
    private JComponent comp;
    private Dimension size;
    private POOCoordinate coor;
    private Rectangle limit;
    
    public MyComp(Container contain, JComponent comp, int x, int y){
        init(contain, comp, x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
    }
    public MyComp(Container contain, JComponent comp, int x, int y, int width, int height){
        init(contain, comp, x, y, width, height);
    }
    private void init(Container contain, JComponent comp, int x, int y, int width, int height){
        this.contain = contain;
        this.contain.add(comp);
        setLimit(contain.getBounds());
        this.comp = comp;
        setSize(new Dimension(width, height));
        this.coor = new Coordinate(x*ratio, y*ratio);
    }
    
    public void setLimit(Rectangle rec){
        limit = (Rectangle)(rec.clone());
    }
    public JComponent getComp(){
        return comp;
    }
    public POOCoordinate getCoor(){
        return new Coordinate(coor);
    }
    public Dimension getSize(){
        return new Dimension(size.width, size.height);
    }
    public void setSize(Dimension size){
        this.size = new Dimension(size.width, size.height);
    }
    protected void setCoor(POOCoordinate coor){  
        Insets insets = contain.getInsets();
        if(isInside(limit.y, coor.y/ratio+insets.left, limit.width-size.width))this.coor.y = coor.y;
        if(isInside(limit.x, coor.x/ratio+insets.top, limit.height-size.height))this.coor.x = coor.x;
    }
    public void draw(){
        Insets insets = contain.getInsets();
        comp.setBounds(coor.y/ratio+insets.left, coor.x/ratio+insets.top, size.width, size.height);
    }
    private boolean isInside(int left, int middle, int right){
        return left<=middle && middle<right;
    }
}