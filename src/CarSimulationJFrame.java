
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

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
