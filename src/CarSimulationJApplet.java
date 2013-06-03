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
    private JButton stopauto_button;
    private JButton stopAndResume;

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
        
        JButton button2 = new JButton("ADD Cars@300");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( highway.car_join(new Cars(highway, carImage), 300, 0) == false)
                    label.setText("Can't add, plz wait");   
                else label.setText("add Success!!"); 
            }
        });
        add(button2);
                
        stopauto_button = new JButton("stopAutoAddCars");
        stopauto_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop_add_car = !stop_add_car;
                if(stopauto_button.getText().equals("stopAutoAddCars"))stopauto_button.setText("startAutoAddCars");
                else stopauto_button.setText("stopAutoAddCars");
            }
        });
        add(stopauto_button);
        
        stopAndResume = new JButton("stop");
        stopAndResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stopAndResume.getText().equals("stop")){
                    start = false;
                    highway.pause();
                    stopAndResume.setText("resume");
                }
                else{
                    start = true;
                    highway.resume();
                    stopAndResume.setText("stop");
                }
            }
        });
        add(stopAndResume);
        
        JButton restart = new JButton("restart");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start = true;
                highway.init(lane_num);
            }
        });
        add(restart);
        
        label = new JLabel();
        add(label);
        this.setVisible(true);
        getContentPane().repaint();
        
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

                if(stop_add_car == false){
                    if(count%1 == 0 && Math.random()*100 < 5)
                        highway.car_join(new Cars(highway, carImage), 0, 0);
                    if(count%2 == 0 && Math.random()*10 < 1)
                        highway.car_join(new Cars(highway, carImage), 0, 1);
                    if(count%3 == 0 && Math.random()*10 < 7)
                        highway.car_join(new Cars(highway, carImage), 0, 2);
                }

                if( highway.check() == false )
                    start = false;
                repaint();
            }
            stopAndResume.setText("resume");
        }
        
    }

    // TODO overwrite start(), stop() and destroy() methods
}