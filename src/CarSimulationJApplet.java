import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import javax.swing.*;

public class CarSimulationJApplet extends JApplet {
    private Highway highway = null;
    private int lane_num = 3;
    private boolean start = true, end = false;
    private JLabel label;
    private boolean stop_add_car = false;
    private JButton stop_button;

    private BufferedImage []carImage = new BufferedImage[4];

    @Override
    public void init() {
        String []option = {"1", "2", "3"};
        String choice = (String) JOptionPane.showInputDialog(this, "Choose Lane Number", "CarSimulation", JOptionPane.PLAIN_MESSAGE, null, option, "3");
        lane_num = Integer.parseInt(choice);

        for(int i=0; i<4; i++)
            carImage[i] = MyUtil.getImage(String.format("img/car%d.png", i));
        
        setLayout(new FlowLayout());
        highway = new Highway(getWidth()-48, 20, lane_num);
        
        highway.setPreferredSize(new Dimension(800, 120*3+20));
        add(highway);
        
        JButton button = new JButton("ADD Cars@1");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( highway.car_join(new Cars(highway, carImage), 1, 0) == false )
                    label.setText("Can't add, plz wait");
                else label.setText("add Success!!");
            }
        });
        add(button);
        
        JButton button2 = new JButton("ADD Cars@200");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( highway.car_join(new Cars(highway, carImage), 200, 0) == false)
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
        
        stop_button = new JButton("stopAutoAddCars");
        stop_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop_add_car = !stop_add_car;
                if(stop_button.getText().equals("stopAutoAddCars"))stop_button.setText("startAutoAddCars");
                else stop_button.setText("stopAutoAddCars");
            }
        });
        add(stop_button);
        
        JButton stop = new JButton("stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start = false;
            }
        });
        add(stop);
        
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
        
        while(end == false){
            int count = 0;
            while(start){
                try {
                    Thread.sleep(64);
                } catch (InterruptedException ex) {}
                count = (count + 1)%1000;

                if(stop_add_car == false && count % 5 == 0){
                    if(Math.random()*10 < 1)
                        highway.car_join(new Cars(highway, carImage), 0, 0);
                    if(Math.random()*10 < 3)
                        highway.car_join(new Cars(highway, carImage), 0, 1);
                    if(Math.random()*10 < 6)
                        highway.car_join(new Cars(highway, carImage), 0, 2);
                }

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