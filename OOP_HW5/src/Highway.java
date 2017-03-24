
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

/** Class Description of Highway */
public class Highway extends JPanel{
	private int length;
	private Lane[] lane;
	
	private BufferedImage img;
	private int crash_lane = -1;
	private int crash_position = -1;
	private int changeLaneDuration = 20;

	@Override
	synchronized public void paintComponent(Graphics g){
		g.drawImage(img, 0, 0, this);
		for( int i=0; i<getLaneNum(); i++ ){
			for( int j=1; j<=getLane(i).getLength(); j++ ){
				int index = getLane(i).position_search(j);
				if( index >= 0 ){
					Cars car = getLane(i).getCars(index);
					if(car.begin > changeLaneDuration)
						g.drawImage(car.show(), (j-1)  , 120*i+50, 48, 39, this);
					else{
						int position = (int) (120*(car.lane_id[0]*car.begin*1.0/changeLaneDuration + car.lane_id[1]*(1-car.begin*1.0/changeLaneDuration))) + 50;
						g.drawImage(car.show(), (j-1), position, 48, 39, this);
					}
				}
			}
		}
		
		if(crash_lane != -1){
			System.err.printf("crash_lane = %d, crash_position = %d\n", crash_lane, crash_position);
			if(crash_position != -1)
				g.drawImage(MyUtil.getImage("img/boom.png"), crash_position-1-24, 120*crash_lane+50, 96, 78, this);
		}
	}
	
	/** Construtor of Class Highway
		@param length road_length
		@param max_cars total number of cars expected
	*/
	public Highway( int length, int max_cars, int num_lane){
		this.length = length;
		lane = new Lane[num_lane];  
		for( int i=0; i<num_lane; i++ )
			lane[i] = new Lane(max_cars, length);
		init(num_lane);
	}
	
	synchronized public void pause(){
		if(lane != null){
			for(int i=0; i<getLaneNum(); i++)
				for(int j=0; j<getLane(i).getLoads(); j++)
					getLane(i).getCars(j).stop = true;
		}
	}
	
	synchronized public void resume(){
		if(lane != null){
			for(int i=0; i<getLaneNum(); i++)
				for(int j=0; j<getLane(i).getLoads(); j++)
					getLane(i).getCars(j).stop = false;
		}
	}
	
	synchronized public void init(int lane_num){
		pause();
		if(lane != null){
			for(int i=0; i<getLaneNum(); i++)
				for(int j=0; j<getLane(i).getLoads(); j++)
					getLane(i).getCars(j).end = true;
		}
		lane = new Lane[lane_num];
		for( int i=0; i<lane_num; i++ )
			lane[i] = new Lane(5, length);
		crash_lane = -1;
		
		BufferedImage []tmp = new BufferedImage[4];
		tmp[0] = MyUtil.getImage("img/other.png");
		tmp[1] = MyUtil.getImage("img/lane.png");
		tmp[2] = MyUtil.getImage("img/seperator.png");
		
		img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(tmp[0], 0, 0, 800, 20, this);
		g.drawImage(tmp[1], 0, 20, 800, 100, this);
		for(int i=1; i<getLaneNum(); i++){
			g.drawImage(tmp[2], 0, 120*i, 800, 20, this);
			g.drawImage(tmp[1], 0, 120*i+20, 800, 100, this);
		}
		g.drawImage(tmp[0], 0, 120*getLaneNum(), 800, 20, this);
	}
	
	public Lane getLane(int id){
		return lane[id];
	}
	
	public int getLaneNum(){
		return lane.length;
	}
	
	synchronized public boolean changeLane(Cars car, int lane_id){
		if(lane_id >= getLaneNum() || lane_id < 0) return false;
		
		int origin_lane = car.lane_id[0];
		if(car.begin < changeLaneDuration*2 || car.getSpeed() < car.getMax_speed()) return false;
		if(car.getAccerleration() < 0) return false;
		if(getLane(lane_id).car_join(car, car.getPosition()) == false) return false;
		if(getLane(car.lane_id[0]).car_leave(car) == false){
			getLane(lane_id).car_leave(car);
			return false;
		}
		
		car.begin = 0;
		car.lane_id[1] = origin_lane;   
		car.lane_id[0] = lane_id;
		car.setMax_speed();
		return true;
	}
	
	synchronized int getProsperLane(Cars car){
		int left     = car.decide( car.lane_id[0]>0               ? getLane(car.lane_id[0]-1).distance_ahead(car.getPosition()) : 0 );
		int right    = car.decide( car.lane_id[0]+1<getLaneNum()  ? getLane(car.lane_id[0]+1).distance_ahead(car.getPosition()) : 0 );
		int current  = car.decide(                                  getLane(car.lane_id[0]).distance_ahead(car.getPosition()) );

		if( right > left && right > current ) return car.lane_id[0] + 1;
		else if(right >= left && right == current && Math.random()<0.1) return car.lane_id[0] + 1;
		else if( left > right && left > current ) return car.lane_id[0]-1;
		else return car.lane_id[0];
	}

	/** Check car crash or car leave
		@return false if car crash has been detect
	*/
	synchronized public boolean check(){
		crash_lane = IsPeace();
		if( crash_lane == -1 ){
			for( int i=0; i<getLaneNum(); i++ )
				getLane(i).checkCarLeave();
			return true;
		}
		else{
			pause();
			return false;
		}
	}

	/** Join Car into the highway 
		@param car The car waiting for join
		@param position The entering point of the car
		@return false if the position on the highway have another car or the car's start speed less than 1
	*/
	public boolean car_join( Cars car, int position, int lane_id ){
		if(lane_id < 0 || lane_id >= getLaneNum() ) return false;
		
		boolean joined = getLane(lane_id).car_join(car, position);
		if(joined){
			car.lane_id[0] = lane_id;
			car.lane_id[1] = lane_id;
			car.setMax_speed();
			new Thread(car).start();
		}
		return joined;
	}
	
	/** Returns How many cars on the highway
		@return how many cars still on the highway 
	*/
	public int loads(){
		int sum = 0;
		for( int i=0; i<getLaneNum(); i++ )
			sum += getLane(i).getLoads();
		return sum;
	}

	/** @return crash_lane if crash happened, otherwise false */
	synchronized private int IsPeace(){
		for( int i=0; i<getLaneNum(); i++ ){
			if( getLane(i).getCrashPosition() != -1){
				crash_position = getLane(i).getCrashPosition();
				System.err.printf("crashPosition = %d on lane[%2d]\n", crash_position,  i);
				return i;
			}
		}
		return -1;
	}
}