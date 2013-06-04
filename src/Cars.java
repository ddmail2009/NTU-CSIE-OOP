
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
	private int position;
    private Highway highway;
    
    private int max_speed = 10;
	public int begin = 0;
    
    public int []lane_id = new int[2];
    protected boolean stop = false;
    protected boolean end = false;
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
		return (distance-getAccerleration())/2 > getMax_speed() ? getMax_speed() : (int)(distance-getAccerleration())/2;
	}

	/** Decide its decision based on the distance ahead
		@param distance distance ahead
	*/	
	public void decision(){
        int lane = highway.getProsperLane(this);
        if(lane != lane_id[0]) highway.changeLane(this, lane);
        
        int distance = highway.getLane(lane_id[0]).distance_ahead(getPosition());
		int tmp = decide(distance);

        if( tmp > speed+getAccerleration() ) accerleration += 0.2;
        else if( tmp < speed+getAccerleration() )accerleration -= (speed - tmp);
	}

	/** Print the current state of this Car */
	public void print(){
		System.err.printf("Cars[%2d]@lane[%d]@%3d speed: %2d, accerleration: %5.2f\n", getID(), lane_id[0], getPosition(), speed, getAccerleration());
	}

	/** Move the car based on the current speed, update its mileage and position 
		@return the distance between two time stamp
	*/
    public int drive() {
        speed += getAccerleration();
        if(speed < 0){
            speed = 0;
            accerleration = 0;
        }
        else if(speed > getMax_speed()){
            speed = getMax_speed();
        }
        
        setPosition(getPosition() + speed);
        begin += 1;
        return speed;
    }
    
    @Override
    public void run() {
        System.err.printf("Cars[%d] add\n", getID());
        while(end == false){
            while(stop == false && end == false){
                try {
                    Thread.sleep(32);
                } catch (InterruptedException ex) { }

                if(getPosition() > highway.getLane(lane_id[0]).getLength()){
                    end = true;
                }
                else{
                    decision();
                    drive();
                }
            }
            try {
                Thread.sleep(32);
            } catch (InterruptedException ex) { }
        }
        System.err.printf("Cars[%d] leave\n", getID());
    }

    Image show() {
        return img[speed*3/getMax_speed()];
    }

    void setSpeed(int decide) {
        speed = decide;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the max_speed
     */
    public int getMax_speed() {
        return max_speed;
    }

    /**
     * @param max_speed the max_speed to set
     */
    public void setMax_speed() {
        max_speed = 8+2*lane_id[0];
    }

    /**
     * @return the accerleration
     */
    public double getAccerleration() {
        return accerleration;
    }
}