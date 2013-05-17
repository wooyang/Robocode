package wilson;

/**
 * Position - a support class
 */
public class Position {
	long x;
	long y;
	
	public Position(long x,long y){
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof Position) {
			Position p = (Position)obj;
			return this.x==p.x && this.y==p.y;
		}
		return super.equals(obj);
	}
	
	public int hashCode(){
		return (int)(x+y);
	}
}
	