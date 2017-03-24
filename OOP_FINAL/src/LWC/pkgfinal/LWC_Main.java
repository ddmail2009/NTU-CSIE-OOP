package LWC.pkgfinal;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Loader.*;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Overlay.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * The JFrame class of LWC_World
 */
public class LWC_Main extends JFrame {
	private LWC_RPG rpg;
    private LWC_RPG_Panel panel;
    protected LWC_ImagePool imgPool = new LWC_ImagePool();
    protected LWC_MP3Pool mp3Pool = new LWC_MP3Pool();
    protected final Object lock = new Object();
		
	/**
	 * initialization of LWC_World
	 */
	public LWC_Main(){
        super("LWC_World");
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		panel = new LWC_RPG_Panel(this);
		add(panel);
        pack();
        
        
        panel.setViewPoint(new Point());
        rpg = new LWC_RPG(this);
        panel.setRPG(rpg);
		addKeyListener(rpg.getKeys());
		
        
        System.err.println(panel.getViewArea());
        
        rpg.addPauseInterface(new LWC_StartMenu(rpg));
        setVisible(true);
	}
    
    public void RPG_init(){       
        rpg = new LWC_RPG(this);
		panel.setRPG(rpg);
		addKeyListener(rpg.getKeys());
        pack();
        
        System.out.println(System.getProperty("file.encoding"));
        
        LWC_Player p = new LWC_Player("SteveChen", "Player", rpg);
		rpg.setPlayer(p);
        LWC_Gravity gravity = new LWC_Gravity(rpg, p);
		rpg.addEffect(gravity);
        
        MapLoader loader;
        loader = new MapLoader("config/AnimeMap.txt");
        rpg.setMap(loader);
        
        LWC_MP3Player background = rpg.mp3Pool().replacePlayer("background", "music/background.mp3");
        background.setLoop(true);
        background.play();
    }
	
	/**
	 * Running the LWC_RPG's update
	 * @return false if LWC_RPG is in its end state
	 */
	public boolean fight(){
		return rpg.update();
	}
	    
    public void RPG_Save(String outputfile){
        File file = new File(outputfile+".lwcSave");
        try {
            if(!file.exists()) file.createNewFile();
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(rpg);
            output.close();
            System.err.println("Save to file: " + file + " success");
        } catch (Exception e) {
            System.err.println("Save to file: " + file + " failed");
            e.printStackTrace();
        }
        
        File infofile = new File(outputfile+".lwcInfo");
        try{
            if(!infofile.exists()) infofile.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(infofile));
            LWC_Player player = rpg.getPlayer();
            output.write(String.format("Player: %s, lv: %d, hp: %d/%d", player.getName(), player.getLv(), player.getHP(), player.getRegister("MaxHP")));
            output.close();
            System.err.println("Save to info file: " + infofile + " success");
        }catch (Exception e){
            System.err.println("Save to info file: " + infofile + " failed");
            e.printStackTrace();
        }
    }
    
    public void RPG_Load(String inputfile){
        File file = new File(inputfile);
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            rpg = (LWC_RPG) input.readObject();
            input.close();
            System.err.println("load from file: " + inputfile + " success");
        } catch (Exception e) {
            System.err.println("load from file: " + inputfile + " failed");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR OCCURED WHEN LOADING " + inputfile);
            System.exit(1);
        }
        panel.setRPG(rpg);
        addKeyListener(rpg.getKeys());
        pack();
    }
	
	/**
	 * Game Start and loops
	 * @param args system argument
	 */
	public static void main(String[] args) {
		LWC_Main main = new LWC_Main();
		
		while(main.fight()){
			try {
				Thread.sleep(16);
			} catch (Exception e) {
				e.printStackTrace();
			}
            main.repaint();
		}
        System.exit(0);
	}
}