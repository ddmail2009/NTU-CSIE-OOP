
import java.util.*;

/** Class Description of inner class Lane of Highway */
public class Lane{
    /** The Car list ordered by position */
    private ArrayList<Cars> cars;
    private int length;
    
    /** Constructor of Lane
        @param max_cars The maximum cars Lane can hold 
    */
    public Lane( int max_cars, int length ){
        this.length = length;
        cars = new ArrayList<>(max_cars);
    }

    synchronized public Cars getCars(int i){
        return cars.get(i);
    }
    
    synchronized public int getLoads(){
        return cars.size();
    }

    /** Join a car into the Lane
        @param car The car who wants to join in
        @param position The Entering position
        @return false when the car can't join in
    */
    synchronized public boolean car_join( Cars car, int position ){
        if( car.decide(distance_ahead(position)) > 0 ){
            for(int i=0; i<car.getSpeed()*2+48; i++)
                if(position_search(position-i) != -1)return false;
            for(int i=0; i<48; i++)
                if(position_search(position+i) != -1)return false;
            cars.add(car);
            car.setPosition(position);
            car.setSpeed(car.decide(distance_ahead(position)));
            position_sort();
            return true;
        }
        else return false;
    }

    /** Kick a car out of the Lane
        @param car The car who wants to leave
        @return false when can't find the car in this lane
    */
    synchronized public boolean car_leave( Cars car ){
        return cars.remove(car);
    }

    /** Let the list be ordered */
    private void position_sort(){
        Collections.sort(cars, new Comparator<Cars>() {
            @Override
            public int compare(Cars o1, Cars o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
    }

    /** The empty distance ahead of the specific position
        @param position The specific position
        @return The distance ahead of the specific position
    */
    synchronized public int distance_ahead( int position ){
        for( int i=0; i<cars.size(); i++ ){
            if( cars.get(i).getPosition() > position )
                return cars.get(i).getPosition() - position - 48;
        }
        return length;
    }

    /** Search whether or not there a car in the specific position
        @param position The specific position
        @return the car's index in the lane's list
    */
    synchronized public int position_search( int position ){
        for( int i=0; i<cars.size(); i++ ){
            if( cars.get(i).getPosition() == position )return i;
            else if( cars.get(i).getPosition() > position )break;
        }
        return -1;
    }
    
    /** Get the crash position
        @return -1 if no crash happened
    */
    synchronized public int getCrashPosition(){
        for( int i=0; i<cars.size(); i++ )
            for( int j=i+1; j<cars.size(); j++ ){
                if( cars.get(i).getPosition()+48 > cars.get(j).getPosition() ){
                    return (cars.get(i).getPosition() + cars.get(j).getPosition())/2;
                }
            }
        return -1;
    }
    
    
    synchronized public void print(){
        for (Cars car : cars) {
            car.print();
        }
    }

    synchronized void checkCarLeave() {
        for( int j=0; j<cars.size(); j++ )
            if( cars.get(j).getPosition() > length )
                car_leave(cars.get(j));
    }

    public int getLength() {
        return length;
    }
};