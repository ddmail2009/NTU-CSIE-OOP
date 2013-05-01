package ntu.csie.oop13spring;

public class Coordinate extends POOCoordinate{
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
}