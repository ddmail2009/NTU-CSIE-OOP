
import java.awt.*;
import java.awt.image.BufferedImage;

/** Class Description of Cars */
public class Cars implements Runnable{
	/** The number of cars have been created */
	private static int total_car_num;
	/** The Car's Current Speed */
	private int speed;
	/** This Car's ID based on when it was created */
	private int id;
	/** The Current position of the car*/
	public int position;
    
    private static int max_speed = 10;
    private Highway highway;
    
	public int begin = 0;
    
    public int lane_id;
    private BufferedImage img = null;
    protected boolean stop = false;
    private int accerleration;

	/** Given the ID, and initial the count_down number*/
	public Cars(Highway highway, BufferedImage img){
		this.id = total_car_num++;
        this.highway = highway;
        this.img = img;
		speed = 0;
        accerleration = 0;
	}

	/** Return the Car's current speed
		@return Current speed
	*/
	public int getSpeed(){
		return speed;
	}

	/** Return the Car's specific ID
		@return The Car's ID
	*/
	public int getID(){
		return id;
	}

	/** Find the max(distance/2, 4)
		@param distance the distance ahead of car
		@return the expect speed pursuant to the distance*/
	public static int decide( int distance ){
		return distance/2 > max_speed ? max_speed : distance/2;
	}

	/** Decide its decision based on the distance ahead
		@param distance distance ahead
	*/	
	public void decision(){
        int lane = highway.getProsperLane(this);
        if(lane != lane_id) highway.changeLane(this, lane);
        
        int distance = highway.getLane(lane_id).distance_ahead(position);
		int tmp = decide(distance);

		// If the car was just car_joined the highway, speed up immediately
//		if( begin == true && tmp >= 1 ) accerleration += tmp;
        if( tmp > speed )accerleration += 2;
        else if( tmp < speed )accerleration -= 1;
        
        if(speed + accerleration > tmp) accerleration = tmp - speed;
	}

	/** Print the current state of this Car */
	public void print(){
		System.err.printf("Cars[%2d]@lane[%d]@%3d speed: %2d, accerleration: %5d\n", id, lane_id, position, speed, accerleration);
	}

	/** Move the car based on the current speed, update its mileage and position 
		@return the distance between two time stamp
	*/
    public int drive() {
        speed += accerleration;
        if(speed > max_speed){
            speed = max_speed;
            accerleration -= 1;
        }
        else if(speed < 0){
            speed = 0;
        }
        
        if(Math.random()*10 < 1){
            speed /= 2;
            accerleration = 0;
        }
        position += speed;
        begin += 1;
        return speed;
    }
    
    @Override
    public void run() {
        while(stop == false){
            try {
                Thread.sleep(32);
            } catch (InterruptedException ex) { }
            
            if(position > highway.getLane(lane_id).getLength()){
                break;
            }
            else{
                decision();
                drive();
            }
            print();
        }
        System.err.printf("Cars[%d] stop\n", getID());

    }

    Image show() {
        return img;
    }
}