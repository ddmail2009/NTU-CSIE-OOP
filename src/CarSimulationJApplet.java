import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class CarSimulationJApplet extends JApplet {
    private Highway highway = null;
    private int lane_num = 3;
    private boolean start = true, end = false;
    private JLabel label;
    
    @Override
    public void init() {
        String choice = JOptionPane.showInputDialog(this, "Lane Number", "3");
        lane_num = Integer.parseInt(choice);
        
        setLayout(new FlowLayout());
        highway = new Highway(800, 20, lane_num, getCodeBase());
        
        highway.setPreferredSize(new Dimension(800, 120*3+20));
        add(highway);
        
        JButton button = new JButton("ADD Cars@1");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( highway.car_join(new Cars(highway, MyUtil.getImage(getCodeBase(), "img/car.png")), 1) == false )
                    label.setText("Can't add, plz wait");
                else label.setText("add Success!!");
            }
        });
        add(button);
        
        JButton button2 = new JButton("ADD Cars@200");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( highway.car_join(new Cars(highway, MyUtil.getImage(getCodeBase(), "img/car2.png")), 200) == false)
                    label.setText("Can't add, plz wait");   
                else label.setText("add Success!!"); 
            }
        });
        add(button2);
        
        JButton button3 = new JButton("restart");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highway.init(lane_num);
                start = true;
            }
        });
        add(button3);
        
        label = new JLabel();
        add(label);
        this.setVisible(true);
        getContentPane().repaint();
        
    }
   
    public static void main(String[] args){
    }
    
    @Override
    public void start(){
        System.err.println("start");
        
        BufferedImage []img = new BufferedImage[2];
        img[0] = MyUtil.getImage(getCodeBase(), "img/car.png");
        img[1] = MyUtil.getImage(getCodeBase(), "img/car2.png");
        
        while(end == false){
            int count = 0;
            while(start){
                try {
                    Thread.sleep(64);
                } catch (InterruptedException ex) {}
                count = (count + 1)%1000;

                if(count %10 == 0)highway.car_join(new Cars(highway, img[0]), 1);
//                highway.car_join(new Cars(highway, img[1]), 200);

                if( highway.check() == false )
                    start = false;
                repaint();
            }
//            if(start == false && count != 0)
//                highway.init(lane_num);
        }
        
    }
    
    public void stop(){
        System.err.println("ASD");
        end = true;
    }
   
    // TODO overwrite start(), stop() and destroy() methods
}