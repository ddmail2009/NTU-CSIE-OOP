
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;


public class CarSimulationJFrame extends JFrame{
    CarSimulationJApplet applet;
    
    public CarSimulationJFrame() throws HeadlessException {
        applet = new CarSimulationJApplet();
        applet.setSize(new Dimension(800,600));
        
        applet.init();
        add(applet);
        
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    
    public void start(){
        applet.start();
    }
    
    public static void main(String []argv){
        CarSimulationJFrame frame = new CarSimulationJFrame();
        frame.start();
    }
}
