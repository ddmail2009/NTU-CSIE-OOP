
import java.awt.Image;
import java.awt.image.*;

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
    
    public final static int max_speed = 10;
    private Highway highway;
    
	public int begin = 0;
    
    public int lane_id;
    protected boolean stop = false;
    private double accerleration;

    private BufferedImage []img;

	/** Given the ID, and initial the count_down number*/
	public Cars(Highway highway, BufferedImage img[]){
		this.id = total_car_num++;
        this.highway = highway;

        this.img = img;
		speed = 0;
        accerleration = 0;
        begin = 0;
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
	public int decide( int distance ){
		return (distance-accerleration)/2 > max_speed ? max_speed : (int)(distance-accerleration)/2;
	}

	/** Decide its decision based on the distance ahead
		@param distance distance ahead
	*/	
	public void decision(){
        int lane = highway.getProsperLane(this);
        if(lane != lane_id) highway.changeLane(this, lane);
        
        int distance = highway.getLane(lane_id).distance_ahead(position);
		int tmp = decide(distance);

        if( tmp > speed+accerleration ) accerleration += 0.08;
        else if( tmp < speed+accerleration )accerleration -= (speed - tmp);
	}

	/** Print the current state of this Car */
	public void print(){
		System.err.printf("Cars[%2d]@lane[%d]@%3d speed: %2d, accerleration: %5.2f\n", id, lane_id, position, speed, accerleration);
	}

	/** Move the car based on the current speed, update its mileage and position 
		@return the distance between two time stamp
	*/
    public int drive() {
        speed += accerleration;
        if(speed < 0){
            speed = 0;
            accerleration = 0;
        }
        else if(speed > max_speed){
            speed = max_speed;
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
            if(lane_id == 2 && speed > 0 && accerleration < 0)print();
        }
        System.err.printf("Cars[%d] stop\n", getID());

    }

    Image show() {
        return img[speed*3/max_speed];
    }

    void setSpeed(int decide) {
        speed = decide;
    }
}