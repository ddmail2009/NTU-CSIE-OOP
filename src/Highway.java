
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;

/** Class Description of Highway */
public class Highway extends JPanel{
	private int length;
	private Lane[] lane;
    
    private BufferedImage img;
    private BufferedImage car;
    private URL codebase;
    public Object drive_lock = new Object(), decide_lock = new Object();
    public int ready_car = 0;
    private int crash_lane = -1;

    @Override
    synchronized public void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, this);
        for( int i=0; i<lane.length; i++ ){
			for( int j=1; j<=length; j++ ){
                int index = lane[i].position_search(j);
                if( index >= 0 ){
					g.drawImage(lane[i].getCars(index).show(), (j-1)  , 120*i+50, 48, 39, this);
                }
            }
		}
        
        if(crash_lane != -1){
            int crash_position = lane[crash_lane].getCrashPosition();
            System.err.printf("crash_position = %d\n", crash_position);
            g.drawImage(MyUtil.getImage(codebase, "img/boom.png"), crash_position-1-24, 120*crash_lane+50, 96, 78, this);
            
        }
    }
    
	/** Construtor of Class Highway
		@param length road_length
		@param max_cars total number of cars expected
	*/
	public Highway( int length, int max_cars, int num_lane, URL codebase){
        this.codebase = codebase;
 
        this.length = length;
		lane = new Lane[num_lane];  
		for( int i=0; i<num_lane; i++ )
			lane[i] = new Lane(max_cars, length);
        init(num_lane);
	}
    
    synchronized public void init(int lane_num){
        System.err.println("init");
        if(lane != null){
            for(int i=0; i<lane.length; i++)
                for(int j=0; j<lane[i].getLoads(); j++)
                    lane[i].getCars(j).stop = true;
        }
		lane = new Lane[lane_num];
		for( int i=0; i<lane_num; i++ )
			lane[i] = new Lane(5, length);
        crash_lane = -1;
        
        BufferedImage []tmp = new BufferedImage[4];
        tmp[0] = MyUtil.getImage(codebase, "img/other.png");
        tmp[1] = MyUtil.getImage(codebase, "img/lane.png");
        tmp[2] = MyUtil.getImage(codebase, "img/seperator.png");
        car = MyUtil.getImage(codebase, "img/car.png");
        
        img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.drawImage(tmp[0], 0, 0, 800, 20, this);
        g.drawImage(tmp[1], 0, 20, 800, 100, this);
        for(int i=1; i<lane.length; i++){
            g.drawImage(tmp[2], 0, 120*i, 800, 20, this);
            g.drawImage(tmp[1], 0, 120*i+20, 800, 100, this);
        }
        g.drawImage(tmp[0], 0, 120*lane.length, 800, 20, this);
    }
    
    public Lane getLane(int id){
        return lane[id];
    }
    
    public int getLaneNum(){
        return lane.length;
    }
    
    public boolean changeLane(Cars car, int lane_id){
        if(car.begin > 10 || lane[lane_id].car_join(car, car.position) == false) return false;
        lane[car.lane_id].car_leave(car);
        car.lane_id = lane_id;
        return true;
    }
    
    synchronized int getProsperLane(Cars car){
        int left     = Cars.decide( car.lane_id>0               ? getLane(car.lane_id-1).distance_ahead(car.position) : 0 );
        int right    = Cars.decide( car.lane_id+1<getLaneNum()  ? getLane(car.lane_id+1).distance_ahead(car.position) : 0 );
        int current  = Cars.decide(                               getLane(car.lane_id).distance_ahead(car.position) );

        if( left > right && left > current ) return car.lane_id-1;
        else if( right > left && right >= current ) return car.lane_id + 1;
        else return car.lane_id;
    }

	/** Check car crash or car leave
		@return false if car crash has been detect
	*/
	synchronized public boolean check(){
        crash_lane = IsPeace();
		if( crash_lane == -1 ){
			for( int i=0; i<lane.length; i++ )
                lane[i].checkCarLeave();
			return true;
		}
		else return false;
	}

	/** Join Car into the highway 
		@param car The car waiting for join
		@param position The entering point of the car
		@return false if the position on the highway have another car or the car's start speed less than 1
	*/
	public boolean car_join( Cars car, int position ){
		boolean joined = lane[0].car_join(car, position);
        if(joined){
            car.lane_id = 0;
            new Thread(car).start();
        }
        return joined;
	}

    /** Make the car leaves the highway immediately
        @param car the car who wants to leave
    */
	public void car_leave( Cars car ){
		for( int i=0; i<lane.length; i++ )
			lane[i].car_leave(car);
	}
    
	/** Returns How many cars on the highway
		@return how many cars still on the highway 
	*/
	public int loads(){
		int sum = 0;
		for( int i=0; i<lane.length; i++ )
			sum += lane[i].getLoads();
		return sum;
	}

	/** @return crash_lane if crash happened, otherwise false */
	synchronized private int IsPeace(){
		for( int i=0; i<lane.length; i++ ){
			if( lane[i].getCrashPosition() != -1){
				System.err.printf("crashPosition = %d on lane[%2d]\n", lane[i].getCrashPosition(),  i);
				return i;
			}
		}
		return -1;
	}
}