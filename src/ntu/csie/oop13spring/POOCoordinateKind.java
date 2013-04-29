package ntu.csie.oop13spring;

public class POOCoordinateKind extends POOCoordinate{
	public POOCoordinateKind(int x, int y){
		this.x = x;
		this.y = y;
	}

        @Override
	public boolean equals(POOCoordinate other){
		return x == other.x && y == other.y;
	}
}