package ntu.csie.oop13spring;

public class Coordinate extends POOCoordinate {
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
    
    public Coordinate(POOCoordinate coor){
        this.x = coor.x;
        this.y = coor.y;
    }

    @Override
	public boolean equals(POOCoordinate other){
		return x == other.x && y == other.y;
	}

    @Override
    public String toString() {
        return "Coordinate: x="+x+" y="+y;
    }
    
    public double distance(POOCoordinate coor){
        return Math.pow(x-coor.x, 2) + Math.pow(y-coor.y, 2);
    }
}