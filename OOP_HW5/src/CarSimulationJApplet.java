import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.*;
import javax.swing.*;

public class CarSimulationJApplet extends JApplet {
	private Highway highway = null;
	private int lane_num = 3;
	private boolean stop_add_car = false;
	
	private boolean crashed = false;
	
	private JLabel label;
	private JButton stopauto_button, stopAndResume;
	private int simulationRate = 32;
	
	Timer CarApp;

	private BufferedImage []carImage = new BufferedImage[4];

	@Override
	public void init() {
		String []option = {"1", "2", "3"};
		String choice = (String) JOptionPane.showInputDialog(this, "Choose Lane Number", "CarSimulation", JOptionPane.PLAIN_MESSAGE, null, option, "3");
		try{
			lane_num = Integer.parseInt(choice);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}

		for(int i=0; i<4; i++)
			carImage[i] = MyUtil.getImage(String.format("img/car%d.png", i));
		
		setLayout(new FlowLayout());
		highway = new Highway(getWidth(), 20, lane_num);
		
		highway.setPreferredSize(new Dimension(800, 120*3+20));
		add(highway);
		
		JButton button = new JButton("ADD Cars@1");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if( highway.car_join(new Cars(highway, carImage, simulationRate), 1, 0) == false )
					label.setText("Can't add, plz wait");
				else label.setText("add Success!!");
			}
		});
		add(button);
		
		JButton button2 = new JButton("ADD Cars@300");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if( highway.car_join(new Cars(highway, carImage, simulationRate), 300, 0) == false)
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
				if(stopAndResume.getText().equals("stop")) stop();
				else start();
			}
		});
		add(stopAndResume);
		
		JButton restart = new JButton("restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				crashed = false;
				highway.init(lane_num);
				start();
			}
		});
		add(restart);
		
		add(new JLabel("SimulationRate"));
		JComboBox<String> box = new JComboBox<>(new String[]{"1x", "2x", "4x"});
		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.err.println(e.getItem());
				System.err.println(e.getStateChange());
				String item = (String)e.getItem();
				if(item.equals("1x")) simulationRate = 32;
				else if(item.equals("2x")) simulationRate = 16;
				else if(item.equals("4x")) simulationRate = 8;
			}
		});
		add(box);
		
		label = new JLabel("CarSimulationJApplet");
		add(label);
		this.setVisible(true);
		
		CarApp = new Timer(16, new ActionListener() {
			int count = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(crashed == true)return;
				
				count = count + 1;
				if(stop_add_car == false){
					if(count%3 == 0 && Math.random()*100 < 3)
						highway.car_join(new Cars(highway, carImage, simulationRate), 0, 0);
					if(count%2 == 0 && Math.random()*100 < 5)
						highway.car_join(new Cars(highway, carImage, simulationRate), 0, 1);
					if(count%1 == 0 && Math.random()*100 < 1)
						highway.car_join(new Cars(highway, carImage, simulationRate), 0, 2);
				}

				if( highway.check() == false ) crashed = true;
				repaint();
			}
		});
	}
	
	@Override
	public void start(){
		System.err.println("start");
		stopAndResume.setText("stop");
		highway.resume();
		CarApp.start();
	}
	
	@Override
	public void stop(){
		System.err.println("stop");
		stopAndResume.setText("resume");
		highway.pause();
		CarApp.stop();
	}
	

	// TODO overwrite start(), stop() and destroy() methods
}